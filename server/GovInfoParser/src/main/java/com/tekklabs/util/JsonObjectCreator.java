package com.tekklabs.util;

import org.json.simple.JSONObject;

/**
 * Created by taciosd on 4/30/16.
 */
public interface JsonObjectCreator {

    JSONObject processLine(String[] items);
}
