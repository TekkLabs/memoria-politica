package com.tekklabs.senadores;

import com.tekklabs.model.entities.JsonAttributes;
import com.tekklabs.util.Csv2JsonParser;

import org.json.simple.JSONObject;

/**
 * Created by taciosd on 8/2/15.
 */
public class Csv2JsonSenatorsParser extends Csv2JsonParser {

    public Csv2JsonSenatorsParser(String separator, boolean skipFirstLine) {
        super(separator, skipFirstLine);
    }

    @Override
    protected JSONObject processLine(String[] items) {
        SenatorsCsvColumns columnsQuery = new SenatorsCsvColumns();
        int polNameColumn = columnsQuery.getColumn(JsonAttributes.NOME_POLITICO);
        int partyColumn = columnsQuery.getColumn(JsonAttributes.PARTIDO);
        int ufColumn = columnsQuery.getColumn(JsonAttributes.SIGLA_UF);
        int phoneColumn = columnsQuery.getColumn(JsonAttributes.PHONE);
        int faxColumn = columnsQuery.getColumn(JsonAttributes.FAX);

        JSONObject jObj = new JSONObject();
        jObj.put(JsonAttributes.NOME_POLITICO, items[polNameColumn]);
        jObj.put(JsonAttributes.SIGLA_UF, items[ufColumn]);
        jObj.put(JsonAttributes.PARTIDO, items[partyColumn]);
        jObj.put(JsonAttributes.PHONE, items[phoneColumn]);
        jObj.put(JsonAttributes.FAX, items[faxColumn]);
        return jObj;
    }
}
