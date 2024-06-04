package com.example.service;

import com.example.domain.WorldStock;
import com.example.respository.WorldStockRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class WorldStockService {

    private final WorldStockRepository worldStockRepository;

    private static final String API_URL = "https://api.nasdaq.com/api/screener/stocks?tableonly=true&download=true&exchange=";

    @Value("${korea.stock.appkey}")
    private String appKey;

    @Value("${korea.stock.appsecret}")
    private String appSecret;

    public void getStockInfo(String exchange) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + exchange))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject result = new JSONObject(response.body());

            JSONArray rows = result.getJSONObject("data").getJSONArray("rows");

            for (int j = 0; j < rows.length(); j++) {
                JSONObject jsonObject = rows.getJSONObject(j);

                String symbol = jsonObject.getString("symbol");

                WorldStock findWorldStock = worldStockRepository.findBySymbol(symbol);

                if (findWorldStock == null) {
                    WorldStock worldStock = WorldStock.builder()
                            .symbol(symbol)
                            .exchange(exchange)
                            .englishName(jsonObject.getString("name"))
                            .build();

                    worldStockRepository.save(worldStock);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
