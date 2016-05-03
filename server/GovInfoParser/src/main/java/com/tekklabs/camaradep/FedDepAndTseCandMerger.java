package com.tekklabs.camaradep;

import com.tekklabs.model.Json2Model;
import com.tekklabs.model.Model2Json;
import com.tekklabs.model.entities.Candidate;
import com.tekklabs.model.entities.FedDep;
import com.tekklabs.util.FileReader;
import com.tekklabs.util.JSONWriter;
import com.tekklabs.util.StringUtil;

import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.List;

/**
 * Created by PC on 23/07/2015.
 */
public class FedDepAndTseCandMerger {

    private static final String HELP_TAG = "--help";
    private static final String MERGE_TAG = "--merge";

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
        else if (command.equals(MERGE_TAG)) {
            result = mergeFiles(args);
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
        System.out.println("Modo de uso: ./FedDepAndTseCandMerger " + MERGE_TAG + " <fedDepFilePath> <tseCandidatesFilePath> <jsonOutputFilePath>");
        System.out.println("======================================");
    }

    private static boolean mergeFiles(String[] args) {
        if (args.length != 4) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        try {
            List<FedDep> fedDepList = readFedDepFile(args[1]);
            List<Candidate> tseCandList = readTseCandidateFile(args[2]);
            File outputFile = new File(args[3]);

            associateCpfInFedDepList(fedDepList, tseCandList);
            writeFedDepListToFile(outputFile, fedDepList);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static List<FedDep> readFedDepFile(String fedDepFilePath) throws IOException {
        JSONArray jsonArray = (JSONArray) FileReader.readJsonFile(fedDepFilePath);
        return Json2Model.json2FedDep(jsonArray);
    }

    private static List<Candidate> readTseCandidateFile(String tseFilePath) throws IOException {
        JSONArray jsonArray = (JSONArray) FileReader.readJsonFile(tseFilePath);
        return Json2Model.json2Candidate(jsonArray);
    }

    private static void associateCpfInFedDepList(List<FedDep> fedDepList, List<Candidate> tseCandList) {
        for (FedDep fedDep : fedDepList) {
            String cpf = findCpfInList(tseCandList, fedDep);
            if (cpf == null) {
                System.out.println("CANDIDATO NÃO ENCONTRADO!!!");
                System.out.println();
            }
            else {
                System.out.println("Encontrado! Nome: " + fedDep.civilName);
            }
            fedDep.cpf = cpf;
        }
    }

    private static String findCpfInList(List<Candidate> tseCandList, FedDep fedDep) {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);

        for (Candidate cand : tseCandList) {
            int result = collator.compare(cand.civilName, fedDep.civilName);
            if (result == 0) {
                return cand.cpf;
            }
        }

        System.out.println("Candidato " + fedDep.civilName + " não encontrado na primeira tentativa.");
        System.out.println("Tentando novamente de modo mais flexível...");

        return findCpfInListInFlexibleWay(tseCandList, fedDep);
    }

    private static String findCpfInListInFlexibleWay(List<Candidate> tseCandList, FedDep fedDep) {
        for (Candidate cand : tseCandList) {

            String candCivilName = StringUtil.stripAccents(cand.civilName.toUpperCase());
            String fedDepCivilName = StringUtil.stripAccents(fedDep.civilName.toUpperCase());
            if (candCivilName.equals(fedDepCivilName)) {
                return cand.cpf;
            }

            String candPolName = StringUtil.stripAccents(cand.politicianName.toUpperCase());
            String fedDepPolName = StringUtil.stripAccents(fedDep.politicianName.toUpperCase());
            if (candPolName.equals(fedDepPolName)) {
                return cand.cpf;
            }

            if (compareNamesByEachWord(fedDepCivilName, candCivilName)) {
                return cand.cpf;
            }

            if (compareNamesByEachWord(fedDepPolName, candPolName)) {
                return cand.cpf;
            }
        }

        return null;
    }

    private static boolean compareNamesByEachWord(String thisName, String otherName) {
        String[] candCivilNameTerms = otherName.split(" ");
        int totalCandTerms = candCivilNameTerms.length;
        int foundTerms = 0;
        for (String candTerm : candCivilNameTerms) {
            if (thisName.contains(candTerm)) {
                foundTerms++;
            }
        }

        if (foundTerms == totalCandTerms) {
            return true;
        }

        String[] fedDepCivilNameTerms = thisName.split(" ");
        int totalFedDepTerms = fedDepCivilNameTerms.length;
        foundTerms = 0;
        for (String fedDepTerm : fedDepCivilNameTerms) {
            if (otherName.contains(fedDepTerm)) {
                foundTerms++;
            }
        }

        if (foundTerms == totalFedDepTerms) {
            return true;
        }

        return false;
    }

    private static void writeFedDepListToFile(File outputFile, List<FedDep> fedDepList) throws IOException {
        Model2Json pol2json = new Model2Json();
        JSONArray fedDepJsonArray = pol2json.fedDepList2Json(fedDepList);

        JSONWriter writer = new JSONWriter();
        fedDepJsonArray.writeJSONString(writer);
        String fedDepArrayStr = writer.toString();

        if (!outputFile.exists()) {
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            outputFile.createNewFile();
        }

        PrintWriter printWriter = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
        printWriter.print(fedDepArrayStr);
        printWriter.close();
    }
}
