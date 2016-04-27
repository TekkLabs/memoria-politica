package com.tekklabs.memoriapolitica.db;

import android.content.Context;
import android.util.JsonReader;

import com.tekklabs.memoriapolitica.domain.Party;
import com.tekklabs.memoriapolitica.domain.Politician;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taciosd on 7/31/15.
 */
public class CandidateDao extends Dao {

    private final PartyDao partyDao;
    private List<Party> parties;

    private String jsonCivilNameKey = JsonKeys.CIVIL_NAME_KEY;
    private String jsonPoliticianNameKey = JsonKeys.POLITICIAN_NAME_KEY;
    private String jsonPartyKey = JsonKeys.PARTY_KEY;
    private String jsonUfKey = JsonKeys.UF_KEY;



    public CandidateDao(Context aContext, PartyDao partyDao) {
        super(aContext);
        this.partyDao = partyDao;
        this.parties = partyDao.parseParties();
    }

    private Party getPartyByAcronym(String acronym) {
        for (Party party : parties) {
            if (party.getAcronym().equalsIgnoreCase(acronym)) {
                return party;
            }
        }

        return null;
    }

    public List<Politician> findCandidates(List<String> cpfList) {
        JsonReader reader = null;

        try {
            reader = ResourceUtil.getJsonReaderFromFile(getContext(), "all_candidates");
            return findCandidates(cpfList, reader);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new IllegalStateException("Algo deu errado na laitura das informacoes dos candidatos.");
    }

    private List<Politician> findCandidates(List<String> cpfList, JsonReader reader) throws IOException {
        reader.beginArray();

        List<Politician> candidates = new ArrayList<>();
        while (reader.hasNext()) {
            reader.beginObject();
            String polCpf = reader.nextName();
            int index = cpfList.indexOf(polCpf);
            if (index != -1) {
                Politician pol = new Politician(polCpf);
                readJsonObject(reader, pol);
                candidates.add(pol);
            }
            else {
                reader.skipValue();
            }
            reader.endObject();
        }

        reader.endArray();
        return candidates;
    }

    private Politician readJsonObject(JsonReader reader, Politician pol) throws IOException {
        String civilName = null,
               polName = null,
               party = null,
               uf = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals(jsonCivilNameKey)) {
                civilName = reader.nextString();
            }
            else if (key.equals(jsonPoliticianNameKey)) {
                polName = reader.nextString();
            }
            else if (key.equals(jsonPartyKey)) {
                party = reader.nextString();
            }
            else if (key.equals(jsonUfKey)) {
                uf = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();

        pol.setParty(getPartyByAcronym(party));
        pol.setPoliticianName(polName);
        pol.setCivilName(civilName);
        pol.setUf(uf);
        return pol;
    }
}
