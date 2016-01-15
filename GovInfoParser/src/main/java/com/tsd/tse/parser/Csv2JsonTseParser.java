package com.tsd.tse.parser;

import com.tsd.model.entities.JsonAttributes;
import com.tsd.util.ConsultaCandColumns;
import com.tsd.util.Csv2JsonParser;

import org.json.simple.JSONObject;

/**
 * Created by PC on 23/07/2015.
 */
public class Csv2JsonTseParser extends Csv2JsonParser {

    public Csv2JsonTseParser(String theSeparator, boolean skipFirstLine) {
        super(theSeparator, skipFirstLine);
    }

    @Override
    protected JSONObject processLine(String[] items) {
        ConsultaCandColumns columnsQuery = new ConsultaCandColumns2014();
        int ufColumn = columnsQuery.getColumn(JsonAttributes.SIGLA_UF);
        int civilNameColumn = columnsQuery.getColumn(JsonAttributes.NOME_CIVIL);
        int polNameColumn = columnsQuery.getColumn(JsonAttributes.NOME_POLITICO);
        int cpfColumn = columnsQuery.getColumn(JsonAttributes.CPF_CANDIDATO);
        int numTituloColumn = columnsQuery.getColumn(JsonAttributes.NUM_TITULO_ELEITORAL_CANDIDATO);
        int partyColumn = columnsQuery.getColumn(JsonAttributes.PARTIDO);

        JSONObject jPolObj = new JSONObject();
        jPolObj.put(JsonAttributes.NOME_CIVIL, items[civilNameColumn].replaceAll("\"", ""));
        jPolObj.put(JsonAttributes.NOME_POLITICO, items[polNameColumn].replaceAll("\"", ""));
        jPolObj.put(JsonAttributes.SIGLA_UF, items[ufColumn].replaceAll("\"", ""));
        jPolObj.put(JsonAttributes.NUM_TITULO_ELEITORAL_CANDIDATO, items[numTituloColumn].replaceAll("\"", ""));
        jPolObj.put(JsonAttributes.PARTIDO, items[partyColumn].replaceAll("\"", ""));

        JSONObject jObj = new JSONObject();
        String cpf = items[cpfColumn].replaceAll("\"", "");
        jObj.put(cpf, jPolObj);

        return jObj;
    }
}
