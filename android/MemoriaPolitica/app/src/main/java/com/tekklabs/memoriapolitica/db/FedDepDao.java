package com.tekklabs.memoriapolitica.db;

import android.content.Context;

import com.tekklabs.memoriapolitica.domain.Party;
import com.tekklabs.memoriapolitica.domain.Politician;
import com.tekklabs.memoriapolitica.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 30/06/2015.
 */
public class FedDepDao extends PoliticianDao {

    public FedDepDao(Context aContext, CandidateDao candidateDao, PartyDao partyDao) {
        super(aContext, candidateDao, partyDao, "federal_deputies");
    }

    @Override
    protected List<Politician> parseJsonArray(JSONArray jArray) throws JSONException, IOException {
        List<Politician> politicians = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jsonPol = jArray.getJSONObject(i);
            String cpf = jsonPol.getString(JsonKeys.CPF_KEY);
            String partyAcronym = jsonPol.getString(JsonKeys.PARTY_KEY);
            //String status = jsonPol.getString(JsonKeys.STATUS_KEY);
            String email = jsonPol.getString(JsonKeys.EMAIL_KEY);
            String politicianName = jsonPol.getString(JsonKeys.POLITICIAN_NAME_KEY);

            Politician politician = new Politician(cpf);
            Party party = getPartyByAcronym(partyAcronym);
            politician.setParty(party);
            politician.setEmail(email);
            politician.setPoliticianName(politicianName);

            String fileName = politician.getPoliticianName().toUpperCase();
            fileName = StringUtil.stripAccents(fileName);
            politician.setPhotoPath("photos/federal_deputies/" + fileName + ".jpg");

            politicians.add(politician);
        }

        return politicians;
    }
}
