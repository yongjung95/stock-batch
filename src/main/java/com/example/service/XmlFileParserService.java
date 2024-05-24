package com.example.service;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import com.example.domain.Stock;
import com.example.dto.StockDto;
import com.example.respository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

@Service
@RequiredArgsConstructor
public class XmlFileParserService {

    private final StockRepository stockRepository;

    public void parseXmlFile(String filePath) {
        try {
            File inputFile = new File(filePath);

            inputFile.setReadable(true, false);
            inputFile.setWritable(true, false);
            inputFile.setExecutable(true, false);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("list");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    StockDto stockDto = StockDto.builder()
                            .corpCode(eElement.getElementsByTagName("corp_code").item(0).getTextContent())
                            .corpName(eElement.getElementsByTagName("corp_name").item(0).getTextContent())
                            .dataModifyDate(eElement.getElementsByTagName("modify_date").item(0).getTextContent())
                            .stockCode(eElement.getElementsByTagName("stock_code").item(0).getTextContent())
                            .build();

                    if (StringUtils.hasText(stockDto.getStockCode())){
                        Stock stock = stockRepository.findByCorpNameAndStockCode(stockDto.getCorpName(), stockDto.getStockCode());

                        if (stock == null) {
                            Stock saveStock = Stock.builder()
                                    .corpCode(stockDto.getCorpCode())
                                    .corpName(stockDto.getCorpName())
                                    .dataModifyDate(stockDto.getDataModifyDate())
                                    .stockCode(stockDto.getStockCode())
                                    .build();

                            stockRepository.save(saveStock);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

