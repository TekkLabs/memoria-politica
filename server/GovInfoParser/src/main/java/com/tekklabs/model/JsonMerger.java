package com.tekklabs.model;

import com.tekklabs.model.entities.Candidate;
import com.tekklabs.model.entities.FedDep;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 24/07/2015.
 */
public class JsonMerger {

    private static final String HELP_TAG = "--help";
    private static final String MERGE_TAG = "--merge";


    public static void main(String[] args) throws IOException {
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
            result = merge(args);
        }
        else {
            System.out.println("Comando inválido. Utilize '--help' para ajuda.");
            result = false;
        }

        if (!result) {
            System.out.println("Operacão terminou com erro.");
        }
        else {
            System.out.println("Operacão terminou com sucesso.");
        }
    }

    private static void printHelp() {
        System.out.println("===== Ajuda do Leitor de dados dos deputados federais =====");
        System.out.println();
        System.out.println("Modo de uso: ./JsonMerger " + MERGE_TAG + " <jsonFedDepFilePath> <jsonCandTseFilePath> <jsonResultFile>");
        System.out.println("======================================");
    }

    private static boolean merge(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        FileInputStream fis = new FileInputStream(args[1]);
        byte[] buffer = new byte[5000000];
        int size = fis.read(buffer);
        byte[] bufferReduced = Arrays.copyOf(buffer, size);
        String fedDepFileContent = new String(bufferReduced, 0, size, "UTF-8");
        fis.close();

        JSONArray fedDepJson = (JSONArray) JSONValue.parse(fedDepFileContent);
        List<FedDep> fedDepList = Json2Model.json2FedDep(fedDepJson);

        fis = new FileInputStream(args[2]);
        buffer = new byte[5000000];
        size = fis.read(buffer);
        bufferReduced = Arrays.copyOf(buffer, size);
        String tseFileContent = new String(bufferReduced, 0, size, "UTF-8");
        fis.close();

        JSONArray tseJson = (JSONArray) JSONValue.parse(tseFileContent);
        List<Candidate> tseCandList = Json2Model.json2Candidate(tseJson);
/*
        List<Candidate> mergedList = mergeLists(fedDepList, tseCandList);
        JSONArray mergedJson = list2Json(mergedList);
*/
        String jsonResultPath = args[3];
/*
        JSONWriter jsonWriter = new JSONWriter();
        mergedJson.writeJSONString(jsonWriter);
        String jsonContent = jsonWriter.toString();

        PrintWriter writer = new PrintWriter(jsonResultPath, "UTF-8");
        writer.print(jsonContent);
        writer.close();
*/
        return true;
    }
/*
    public static List<Candidate> mergeLists(List<Candidate>... lists) {
        if (lists.length == 0) {
            return Collections.emptyList();
        }

        List<Candidate> mergedList = lists[0];
        for (int i = 1; i < lists.length; i++) {
            List<Candidate> newList = lists[i];
            mergedList = mergeListWith(mergedList, newList);
        }

        return mergedList;
    }

    private static List<Candidate> mergeListWith(List<Candidate> originalList, List<Candidate> newList) {
        List<Candidate> mergedList = new ArrayList<Candidate>();
        List<Candidate> newListCopy = new ArrayList<Candidate>();
        newListCopy.addAll(newList);

        for (Candidate pol : originalList) {
            String civilName = pol.civilName;
            Candidate foundPol = searchList(civilName, newListCopy);
            newListCopy.remove(foundPol);

            String cpf = (pol.cpf == null) ? foundPol.cpf : pol.cpf;

            Candidate newPol = new Candidate(civilName, cpf);

            if (pol.status == null) newPol.status = foundPol.status;
            else newPol.status = pol.status;

            if (pol.numTituloEleitoral == null) newPol.numTituloEleitoral = foundPol.numTituloEleitoral;
            else newPol.numTituloEleitoral = pol.numTituloEleitoral;

            if (pol.email == null) newPol.email = foundPol.email;
            else newPol.email = pol.email;

            if (pol.party == null) newPol.party = foundPol.party;
            else newPol.party = pol.party;

            if (pol.uf == null) newPol.uf = foundPol.uf;
            else newPol.uf = pol.uf;

            if (pol.politicianName == null) newPol.politicianName = foundPol.politicianName;
            else newPol.politicianName = pol.politicianName;

            mergedList.add(newPol);
        }

        mergedList.addAll(newListCopy);

        return mergedList;
    }

    private static Candidate searchList(String civilName, List<Candidate> list) {
        for (Candidate pol : list) {
            if (civilName.equals(pol.civilName)) {
                return pol;
            }
        }

        return new Candidate(null, null);
    }

    private static JSONArray list2Json(List<Candidate> mergedList) {
        JSONArray jArray = new JSONArray();
        Politician2Json converter = new Politician2Json();

        for (Candidate pol : mergedList) {
            jArray.add(converter.cand2Json(pol));
        }

        return jArray;
    }*/
}
