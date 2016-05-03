package com.tekklabs.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 23/07/2015.
 */
public class FileReader {

    public static List<String> readCsv(String csvFilePath, String charset) throws IOException {
        Path filePath = new File(csvFilePath).toPath();
        Charset charsetObj = Charset.forName(charset);
        return Files.readAllLines(filePath, charsetObj);
    }

    public static Object readJsonFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[8000000];
        int size = fis.read(buffer);
        byte[] bufferReduced = Arrays.copyOf(buffer, size);
        String fileContent = new String(bufferReduced, 0, size, "UTF-8");
        fis.close();

        return JSONValue.parse(fileContent);
    }
}
