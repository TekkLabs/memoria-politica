package com.tekklabs.memoriapolitica.db;

import android.content.Context;
import android.util.JsonReader;

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

    private static List<Politician> allCandidatesCache = new ArrayList<>();

    private String jsonCpfKey = JsonKeys.CPF_KEY;
    private String jsonCivilNameKey = JsonKeys.CIVIL_NAME_KEY;
    private String jsonPoliticianNameKey = JsonKeys.POLITICIAN_NAME_KEY;
    private String jsonPartyKey = JsonKeys.PARTY_KEY;
    private String jsonUfKey = JsonKeys.UF_KEY;



    public CandidateDao(Context aContext) {
        super(aContext);
    }
/*
    List<Politician> getAllCandidates() throws IOException, JSONException {
        synchronized (allCandidatesCache) {
            if (allCandidatesCache.isEmpty()) {
                JSONArray jArray = ResourceUtil.getJsonReaderFromFile(getContext(), "all_candidates");
                allCandidatesCache.addAll(readArray(jArray));
            }
        }

        return allCandidatesCache;
    }
*/
    private List<Politician> readArray(JSONArray jArray) throws JSONException, IOException {
        JSONObject headerObj = jArray.getJSONObject(0);
        String jsonCpfKey = headerObj.getString(JsonKeys.CPF_KEY);
        String jsonCivilNameKey = headerObj.getString(JsonKeys.CIVIL_NAME_KEY);
        String jsonPoliticianNameKey = headerObj.getString(JsonKeys.POLITICIAN_NAME_KEY);
        String jsonPartyKey = headerObj.getString(JsonKeys.PARTY_KEY);
        String jsonUfKey = headerObj.getString(JsonKeys.UF_KEY);
        //String jsonStatusKey = headerObj.getString(statusKey);
        //String jsonJobsKey = headerObj.getString(jobsKey);

        List<Politician> politicians = new ArrayList<>();

        for (int i = 1; i < jArray.length(); i++)
        {
            JSONObject jsonPol = jArray.getJSONObject(i);
            String cpf = jsonPol.getString(jsonCpfKey);
            String polName = jsonPol.getString(jsonPoliticianNameKey);
            String party = jsonPol.getString(jsonPartyKey);

            Politician politician = new Politician(cpf);
            politician.setPoliticianName(polName);
            politician.setPartyName(party);

            String uf = jsonPol.getString(jsonUfKey);
            // ...

            politicians.add(politician);
        }

        return politicians;
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

        return null;
    }

    private List<Politician> findCandidates(List<String> cpfList, JsonReader reader) throws IOException {
        resetKeys();

        reader.beginArray();

        if (reader.hasNext()) {
            reader.beginObject();
            jsonCpfKey = reader.nextName();
            Politician metadata = new Politician(jsonCpfKey);
            readJsonObject(reader, metadata);

            jsonCivilNameKey = metadata.getCivilName();
            jsonPartyKey = metadata.getPartyName();
            jsonPoliticianNameKey = metadata.getPoliticianName();
            //jsonUfKey = metadata.get
            reader.endObject();
        }

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

    private void resetKeys() {
        this.jsonCpfKey = JsonKeys.CPF_KEY;
        this.jsonCivilNameKey = JsonKeys.CIVIL_NAME_KEY;
        this.jsonPoliticianNameKey = JsonKeys.POLITICIAN_NAME_KEY;
        this.jsonPartyKey = JsonKeys.PARTY_KEY;
        this.jsonUfKey = JsonKeys.UF_KEY;
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

        pol.setPartyName(party);
        pol.setPoliticianName(polName);
        pol.setCivilName(civilName);
        return pol;
    }
}
