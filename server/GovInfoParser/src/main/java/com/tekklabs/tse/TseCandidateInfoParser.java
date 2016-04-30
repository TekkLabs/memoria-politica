package com.tekklabs.tse;

import com.tekklabs.util.Csv2JsonParser;
import com.tekklabs.util.FileReader;
import com.tekklabs.util.JSONWriter;
import com.tekklabs.util.MergeFilesUtility;
import com.tekklabs.util.UnzipUtility;

import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by PC on 20/07/2015.
 */
public class TseCandidateInfoParser {

    private static final String HELP_TAG = "--help";
    private static final String EXTRACT_TAG = "--extract";
    private static final String MERGE_TAG = "--merge";
    private static final String CSV2JSON_TAG = "--csv2json";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Argumentos insuficientes. Utilize '--help' para menu de ajuda.");
            return;
        }

        String command = args[0];
        boolean result = true;

        if (command.equals(HELP_TAG)) {
                printHelp();
                return;
        }
        else if (command.equals(EXTRACT_TAG)) {
            result = extractFiles(args);
        }
        else if (command.equals(MERGE_TAG)) {
            result = mergeFiles(args);
        }
        else if (command.equals(CSV2JSON_TAG)) {
            result = csv2json(args);
        }
        else {
            System.out.println("Comando inválido. Utilize '--help' para ajuda.");
            result = false;
        }

        if (!result) {
            System.out.println("Operação terminou com erro.");
        }
    }

    private static void printHelp() {
        System.out.println("===== Ajuda do Leitor de dados cadastrais de candidator do TSE =====");
        System.out.println();
        System.out.println("Modo de uso: ./TseCandidateInfoParser " + EXTRACT_TAG + " <filePath.zip> <destDir>");
        System.out.println("Modo de uso: ./TseCandidateInfoParser " + MERGE_TAG + " <dir> <destFilePath>");
        System.out.println("Modo de uso: ./TseCandidateInfoParser " + CSV2JSON_TAG + " <csvFilePath> <jsonFilePath>");
        System.out.println("======================================");
    }

    private static boolean extractFiles(String[] args) {
        if (args.length != 3) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        try {
            String filePath = args[1];
            String destDir = args[2];

            UnzipUtility unzipUtility = new UnzipUtility();
            unzipUtility.unzip(filePath, destDir);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean mergeFiles(String[] args) {
        if (args.length != 3) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        try {
            String dir = args[1];
            String filePath = args[2];
            File[] inputFiles = getInputFiles(dir);
            MergeFilesUtility.mergeFiles(new File(filePath), inputFiles);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static File[] getInputFiles(String dir) {
        File folder = new File(dir);
        File[] txtFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getPath().endsWith(".txt");
            }
        });

        return txtFiles;
    }

    private static boolean csv2json(String[] args) {
        if (args.length != 3) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        try {
            String csvFilePath = args[1];
            String jsonFilePath = args[2];
            List<String> csvContent = FileReader.readCsv(csvFilePath, "ISO-8859-1");
            JSONArray tseJsonArray = new Csv2JsonParser(";", false, new CandidateJsonObjectCreator()).writeJson(csvContent);

            JSONWriter writer = new JSONWriter();
            tseJsonArray.writeJSONString(writer);
            String tseJsonArrayStr = writer.toString();
            writeStringToFile(tseJsonArrayStr, jsonFilePath);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static void writeStringToFile(String jsonContent, String jsonFilePath) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(jsonFilePath, "UTF-8");
        writer.print(jsonContent);
        writer.close();
    }
}
