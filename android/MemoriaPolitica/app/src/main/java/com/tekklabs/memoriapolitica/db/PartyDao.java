package com.tekklabs.memoriapolitica.db;

import android.content.Context;

import com.tekklabs.memoriapolitica.domain.Party;
import com.tekklabs.memoriapolitica.domain.Politician;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by taciosd on 4/20/16.
 */
public class PartyDao extends Dao {

    private static final String fileName = "parties";
    private static List<Party> parsedParties;

    public PartyDao(Context aContext) {
        super(aContext);
    }

    public List<Party> parseParties() {
        if (parsedParties != null) {
            return parsedParties;
        }

        try {
            JSONArray partyArray = ResourceUtil.getJsonArrayFromFile(getContext(), fileName);
            this.parsedParties = parseJsonArray(partyArray);
            return parsedParties;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private List<Party> parseJsonArray(JSONArray partyArray) throws JSONException {
        List<Party> partyList = new ArrayList<>();

        for (int i = 0; i < partyArray.length(); i++) {
            Party party = createPartyFromJsonObject(partyArray.getJSONObject(i));
            partyList.add(party);
        }

        return partyList;
    }

    private Party createPartyFromJsonObject(JSONObject jsonObject) throws JSONException {
        int code = jsonObject.getInt("code");
        String acronym = jsonObject.getString("acronym");
        String name = jsonObject.getString("name");
        return new Party(code, acronym, name);
    }
}
