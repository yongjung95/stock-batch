package com.example.service;

import com.example.domain.WorldStock;
import com.example.respository.WorldStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorldStockService {

    private final WorldStockRepository worldStockRepository;

    @Value("${world.stock.path}")
    private String WORLD_STOCK_PATH;

    @Value("${notify.token.path}")
    private String NOTIFY_TOKEN_PATH;

    @Value("${korea.stock.appkey}")
    private String appKey;

    @Value("${korea.stock.appsecret}")
    private String appSecret;

    public void worldStockInfoUpdate(String exchange) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WORLD_STOCK_PATH + exchange))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject result = new JSONObject(response.body());

            JSONArray rows = result.getJSONObject("data").getJSONArray("rows");

            log.info("API 호출 성공");
            for (int j = 0; j < rows.length(); j++) {
                JSONObject jsonObject = rows.getJSONObject(j);

                String symbol;

                if (jsonObject.getString("symbol").contains("^")) {
                    symbol = jsonObject.getString("symbol").replace('^', '/');
                } else {
                    symbol = jsonObject.getString("symbol");
                }

                WorldStock findWorldStock = worldStockRepository.findBySymbol(symbol);

                if (findWorldStock == null) {

                    log.info("주식 정보 저장 시작");
                    Thread.sleep(50); // 무리한 호출 방지

                    WorldStock worldStock = WorldStock.builder()
                            .symbol(symbol)
                            .exchange(exchange)
                            .englishName(jsonObject.getString("name"))
                            .koreanName(getStockKoreanName(symbol, exchange))
                            .build();

                    worldStockRepository.save(worldStock);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStockKoreanName(String symbol, String exchange) {
        String prdtTypeCd = switch (exchange) {
            case "AMEX" -> "529";
            case "NYSE" -> "513";
            case "NASDAQ" -> "512";
            default -> "";
        };

        String apiURL = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/search-info?" +
                "PDNO=" + symbol + "&PRDT_TYPE_CD=" + prdtTypeCd;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", getToken());
        header.set("appkey", appKey);
        header.set("appsecret", appSecret);
        header.set("tr_id", "CTPF1604R");

        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(apiURL).build();

        ResponseEntity<String> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(resultMap.getBody());


        String prdtName;
        try {
            prdtName = jsonObject.getJSONObject("output").getString("prdt_name");
        } catch (JSONException e) {
            log.error(String.valueOf(jsonObject));
            prdtName = "";
        }

        return prdtName;
    }

    public String getToken() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(NOTIFY_TOKEN_PATH))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject result = new JSONObject(response.body());

            return result.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
