package com.tsd.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
}
