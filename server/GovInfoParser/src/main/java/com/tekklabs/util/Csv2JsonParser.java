package com.tekklabs.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by PC on 22/07/2015.
 */
public class Csv2JsonParser {

    private String separator;
    private boolean skipFirstLine;
    private final JsonObjectCreator objCreator;


    public Csv2JsonParser(String theSeparator, boolean skipFirstLine, JsonObjectCreator objCreator) {
        this.separator = theSeparator;
        this.skipFirstLine = skipFirstLine;
        this.objCreator = objCreator;
    }

    public final JSONArray writeJson(List<String> csvContent) throws IOException {
        JSONArray jArray = new JSONArray();

        if (this.skipFirstLine) {
            csvContent.remove(0);
        }


        for (String line : csvContent) {
            String[] items = line.split(separator + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            JSONObject jsonPol = objCreator.processLine(items);
            jArray.add(jsonPol);
        }

        return jArray;
    }
}
