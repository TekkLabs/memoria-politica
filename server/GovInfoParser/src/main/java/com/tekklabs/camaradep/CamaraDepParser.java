package com.tekklabs.camaradep;

import com.tekklabs.model.Json2Model;
import com.tekklabs.model.Politician2Json;
import com.tekklabs.model.entities.Candidate;
import com.tekklabs.model.entities.FedDep;
import com.tekklabs.util.FileReader;
import com.tekklabs.util.JSONWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 23/07/2015.
 */
public class CamaraDepParser {

    private static final String HELP_TAG = "--help";
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
        else if (command.equals(CSV2JSON_TAG)) {
            result = csv2json(args);
        }
        else {
            System.out.println("Comando invalido. Utilize '--help' para ajuda.");
            result = false;
        }

        if (!result) {
            System.out.println("Operacao terminou com erro.");
        }
        else {
            System.out.println("Operacao terminou com sucesso.");
        }
    }

    private static void printHelp() {
        System.out.println("===== Ajuda do Leitor de dados dos deputados federais =====");
        System.out.println();
        System.out.println("Modo de uso: ./CamaraDepParser " + CSV2JSON_TAG + " <csvFilePath> <tseCandidatesFilePath> <jsonFilePath>");
        System.out.println("======================================");
    }

    private static boolean csv2json(String[] args) {
        if (args.length != 4) {
            System.out.println("Numero de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        try {
            String csvFilePath = args[1];
            List<String> csvContent = FileReader.readCsv(csvFilePath, "ISO-8859-1");
            JSONArray jsonArray = new Csv2JsonCamDepParser(",", true).writeJson(csvContent);
            List<FedDep> fedDepList = Json2Model.json2FedDep(jsonArray);

            FileInputStream fis = new FileInputStream(args[2]);
            byte[] buffer = new byte[8000000];
            int size = fis.read(buffer);
            byte[] bufferReduced = Arrays.copyOf(buffer, size);
            String tseFileContent = new String(bufferReduced, 0, size, "UTF-8");
            fis.close();

            JSONArray tseJson = (JSONArray) JSONValue.parse(tseFileContent);
            List<Candidate> tseCandList = Json2Model.json2Candidate(tseJson);

            associateCpfInFedDepList(fedDepList, tseCandList);
            Politician2Json pol2json = new Politician2Json();
            JSONArray fedDepJsonArray = pol2json.fedDepList2Json(fedDepList);

            JSONWriter writer = new JSONWriter();
            fedDepJsonArray.writeJSONString(writer);
            String fedDepArrayStr = writer.toString();

            String jsonFilePath = args[3];
            PrintWriter printWriter = new PrintWriter(jsonFilePath, "UTF-8");
            printWriter.print(fedDepArrayStr);
            printWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static void associateCpfInFedDepList(List<FedDep> fedDepList, List<Candidate> tseCandList) {
        for (FedDep fedDep : fedDepList) {
            try {
                String cpf = findCpfInList(tseCandList, fedDep.civilName);
                if (cpf == null) {
                    System.out.println("Candidato nao encontrado! Nome: " + fedDep.civilName);
                }
                fedDep.cpf = cpf;
            }
            catch (Exception e) {
                int i = 0;
            }
        }
    }

    private static String findCpfInList(List<Candidate> tseCandList, String civilName) {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);

        for (Candidate cand : tseCandList) {
            int result = collator.compare(cand.civilName, civilName);
            if (result == 0) {
                return cand.cpf;
            }
        }

        return null;
    }
}
