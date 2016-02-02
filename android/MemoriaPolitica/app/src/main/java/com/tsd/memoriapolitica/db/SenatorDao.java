package com.tsd.memoriapolitica.db;

import android.content.Context;

import com.tsd.memoriapolitica.domain.Politician;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by PC on 30/06/2015.
 */
public class SenatorDao extends PoliticianDao {

    public SenatorDao(Context aContext, CandidateDao candidateDao) {
        super(aContext, candidateDao,  "senators");
    }

    @Override
    protected List<Politician> parseJsonArray(JSONArray jArray) throws JSONException, IOException {
        JSONObject headerObj = jArray.getJSONObject(0);
        String jsonCpfKey = headerObj.getString(JsonKeys.CPF_KEY);
        String jsonPartyKey = headerObj.getString(JsonKeys.PARTY_KEY);
        String jsonStatusKey = headerObj.getString(JsonKeys.STATUS_KEY);
        String jsonEmailKey = headerObj.getString(JsonKeys.EMAIL_KEY);

        List<Politician> politicians = new ArrayList<>();

        for (int i = 1; i < jArray.length(); i++) {
            JSONObject jsonPol = jArray.getJSONObject(i);
            String cpf = jsonPol.getString(jsonCpfKey);
            String party = jsonPol.getString(jsonPartyKey);
            String status = jsonPol.getString(jsonStatusKey);
            String email = jsonPol.getString(jsonEmailKey);

            Politician politician = new Politician(cpf);
            politician.setPartyName(party);

            //politician.setPhotoPath("photos/senators/" + senatorName + ".jpg");


            politicians.add(politician);
        }

        return politicians;
    }
}
