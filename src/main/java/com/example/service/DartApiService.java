package com.example.service;

import com.example.respository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DartApiService {

    @Value("${dart.url}")
    private String ZIP_FILE_URL;

    @Value("${zip.file.path}")
    private String ZIP_FILE_PATH;

    @Value("${unzip.dest.dir}")
    private String UNZIP_DEST_DIR;

    @Value("${xml.file.path}")
    private String XML_FILE_PATH;


    private final ZipFileService zipFileService;
    private final XmlFileParserService xmlFileParserService;

    public void stockInfoUpdate() {
        try {
            // Step 1: Download ZIP file
            zipFileService.downloadZipFile(ZIP_FILE_URL, ZIP_FILE_PATH);
            // Step 2: Unzip the downloaded ZIP file
            zipFileService.unzip(ZIP_FILE_PATH, UNZIP_DEST_DIR);
            // Step 3: Parse the extracted XML file
            xmlFileParserService.parseXmlFile(XML_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

