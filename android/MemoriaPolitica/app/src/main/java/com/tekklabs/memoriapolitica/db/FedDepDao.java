package com.tekklabs.memoriapolitica.db;

import android.content.Context;

import com.tekklabs.memoriapolitica.domain.Party;
import com.tekklabs.memoriapolitica.domain.Politician;

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
        JSONObject headerObj = jArray.getJSONObject(0);
        String jsonCpfKey = headerObj.getString(JsonKeys.CPF_OLD_KEY);
        String jsonPartyKey = headerObj.getString(JsonKeys.PARTY_OLD_KEY);
        String jsonStatusKey = headerObj.getString(JsonKeys.STATUS_OLD_KEY);
        String jsonEmailKey = headerObj.getString(JsonKeys.EMAIL_OLD_KEY);

        List<Politician> politicians = new ArrayList<>();

        for (int i = 1; i < jArray.length(); i++) {
            JSONObject jsonPol = jArray.getJSONObject(i);
            String cpf = jsonPol.getString(jsonCpfKey);
            String partyAcronym = jsonPol.getString(jsonPartyKey);
            String status = jsonPol.getString(jsonStatusKey);
            String email = jsonPol.getString(jsonEmailKey);

            Politician politician = new Politician(cpf);
            Party party = getPartyByAcronym(partyAcronym);
            politician.setParty(party);

            /*
            String photoNumber;
            int photoIndex = i-1;
            if (photoIndex < 10) photoNumber = "00";
            else if (photoIndex < 100) photoNumber = "0";
            else photoNumber = "";

            String fileName = "carometro_legislatura55-" + photoNumber + photoIndex;
            politician.setPhotoPath("photos/federal_deputies/" + fileName + ".jpg");
            */

            politicians.add(politician);
        }

        return politicians;
    }
}
