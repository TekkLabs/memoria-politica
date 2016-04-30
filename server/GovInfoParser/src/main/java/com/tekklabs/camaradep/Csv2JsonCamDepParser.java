package com.tekklabs.camaradep;

import com.tekklabs.model.entities.JsonAttributes;
import com.tekklabs.util.Csv2JsonParser;

import org.json.simple.JSONObject;

/**
 * Created by PC on 23/07/2015.
 */
public class Csv2JsonCamDepParser extends Csv2JsonParser {

    public Csv2JsonCamDepParser(String separator, boolean skipFirstLine) {
        super(separator, skipFirstLine);
    }

    @Override
    protected JSONObject processLine(String[] items) {
        FedDepCsvColumns columnsQuery = new FedDepCsvColumns();
        int civilNameColumn = columnsQuery.getColumn(JsonAttributes.NOME_CIVIL);
        int polNameColumn = columnsQuery.getColumn(JsonAttributes.NOME_POLITICO);
        int ufColumn = columnsQuery.getColumn(JsonAttributes.SIGLA_UF);
        int partyColumn = columnsQuery.getColumn(JsonAttributes.PARTIDO);
        int statusColumn = columnsQuery.getColumn(JsonAttributes.STATUS);
        int emailColumn = columnsQuery.getColumn(JsonAttributes.EMAIL);

        JSONObject jObj = new JSONObject();
        jObj.put(JsonAttributes.NOME_CIVIL, items[civilNameColumn]);
        jObj.put(JsonAttributes.NOME_POLITICO, items[polNameColumn]);
        jObj.put(JsonAttributes.SIGLA_UF, items[ufColumn]);
        jObj.put(JsonAttributes.PARTIDO, items[partyColumn]);
        jObj.put(JsonAttributes.STATUS, items[statusColumn]);
        jObj.put(JsonAttributes.EMAIL, items[emailColumn]);
        return jObj;
    }
}
