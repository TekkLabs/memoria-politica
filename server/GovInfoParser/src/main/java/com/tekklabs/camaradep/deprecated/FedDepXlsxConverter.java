package com.tekklabs.camaradep.deprecated;

import com.tekklabs.camaradep.deprecated.FedDepJsonObjectCreator;
import com.tekklabs.util.JSONWriter;
import com.tekklabs.util.JsonObjectCreator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by taciosd on 4/30/16.
 */
public class FedDepXlsxConverter {

    private static final String HELP_TAG = "--help";
    private static final String XLSX2JSON_TAG = "--xlsx2json";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Argumentos insuficientes. Utilize '" + HELP_TAG + "' para menu de ajuda.");
            return;
        }

        String command = args[0];
        boolean result = true;
        if (command.equals(HELP_TAG)) {
            printHelp();
        }
        else if (command.equals(XLSX2JSON_TAG)) {
            try {
                result = convertXlsx(args);
            }
            catch (Exception e) {
                System.out.println("Ocorreu um erro na conversão.");
                e.printStackTrace();
                result = false;
            }
        }
        else {
            result = false;
        }

        if (!result) {
            System.out.println("Operação terminou com erro.");
        }
        else {
            System.out.println("Operação terminou com sucesso.");
        }
    }

    private static void printHelp() {
        System.out.println("===== Ajuda do utilitário de conversão do arquivo XLS dos deputados federais =====");
        System.out.println();
        System.out.println("Modo de uso: ./FedDepXlsxConverter " + XLSX2JSON_TAG + " <xlsFilePath> <outputFilePath>");
        System.out.println("======================================");
    }

    private static boolean convertXlsx(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Número de argumentos incorreto. Utilize '" + HELP_TAG + "' para ajuda.");
        }

        File xlsxFile = new File(args[1]);
        File outputFile = new File(args[2]);

        if (outputFile.isDirectory()) {
            System.out.println("O caminho de destino deve ser um arquivo e não um diretório.");
            return false;
        }

        System.out.println("Loading XLSX file...");
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(xlsxFile));

        System.out.println("Extracting data fom XLSX...");
        JSONArray jArray = extractDataFromXls(wb);

        System.out.println("Exporting data to JSON...");
        exportDataToJson(jArray, outputFile);

        return true;
    }

    private static JSONArray extractDataFromXls(XSSFWorkbook wb) {
        XSSFSheet sheet = wb.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getPhysicalNumberOfCells();
        int startRow = 1; // Pular cabecalho.

        JsonObjectCreator jsonObjCreator = new FedDepJsonObjectCreator();
        JSONArray jArr = new JSONArray();
        for(int row = startRow; row < rows; row++) {
            XSSFRow xssfRow = sheet.getRow(row);
            if(xssfRow != null) {

                String[] rowData = new String[cols];
                for(int col = 0; col < cols; col++) {
                    XSSFCell cell = xssfRow.getCell(col);
                    if(cell != null) {
                        String datum;
                        if (XSSFCell.CELL_TYPE_STRING == cell.getCellType()) {
                            datum = cell.getStringCellValue();
                        }
                        else if (XSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                            datum = String.valueOf(cell.getNumericCellValue());
                        }
                        else {
                            System.out.println("A célula: [" + row + "," + col + "] não contém um String e nem um Número.");
                            datum = "";
                        }

                        rowData[col] = datum;
                    }
                }

                JSONObject jObj = jsonObjCreator.processLine(rowData);
                jArr.add(jObj);
            }
        }

        return jArr;
    }

    private static void exportDataToJson(JSONArray jArray, File outputFile) throws IOException {
        JSONWriter writer = new JSONWriter();
        jArray.writeJSONString(writer);
        String fedDepArrayStr = writer.toString();

        outputFile.getParentFile().mkdirs();
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        PrintWriter printWriter = new PrintWriter(outputFile, "UTF-8");
        printWriter.print(fedDepArrayStr);
        printWriter.close();
    }
}
