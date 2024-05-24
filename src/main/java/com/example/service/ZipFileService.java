package com.example.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.*;

@Service
public class ZipFileService {

    public void downloadZipFile(String fileUrl, String destPath) throws IOException {
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = newFile(dir, entry);
                if (entry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
            zis.closeEntry();
        }
    }

    private File newFile(File destDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destDir, zipEntry.getName());
        String destDirPath = destDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
}

