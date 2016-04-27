package com.tekklabs.memoriapolitica.db;

import android.content.Context;

import com.tekklabs.memoriapolitica.domain.Party;
import com.tekklabs.memoriapolitica.domain.Politician;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by taciosd on 8/2/15.
 */
public abstract class PoliticianDao extends Dao {

    private static List<Politician> politiciansCache = new ArrayList<>();
    private final PartyDao partyDao;
    private CandidateDao candidateDao;
    private String resourceName;

    public PoliticianDao(Context aContext, CandidateDao candidateDao, PartyDao partyDao, String resourceName) {
        super(aContext);
        this.candidateDao = candidateDao;
        this.partyDao = partyDao;
        this.resourceName = resourceName;
    }

    protected Party getPartyByAcronym(String partyAcronym) {
        List<Party> parties = partyDao.parseParties();
        for (Party party : parties) {
            if (party.getAcronym().equalsIgnoreCase(partyAcronym)) {
                return party;
            }
        }

        return null;
    }

    public List<Politician> getAll() {
        synchronized (politiciansCache) {
            if (politiciansCache.isEmpty()) {
                PoliticianDao.politiciansCache.addAll(parsePoliticiansFromJsonFile());
            }
        }

        return PoliticianDao.politiciansCache;
    }

    public Politician getByCpf(String cpf) {
        List<Politician> allPol = getAll();

        for (Politician pol : allPol) {
            if (pol.getCpf().equals(cpf)) {
                return pol;
            }
        }

        return null;
    }

    private List<Politician> parsePoliticiansFromJsonFile() {

        try {
            JSONArray fedDepArray = ResourceUtil.getJsonArrayFromFile(getContext(), resourceName);
            List<Politician> fedDepList = parseJsonArray(fedDepArray);
            fillPoliticiansWithCandidateInfo(fedDepList);
            return fedDepList;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private void fillPoliticiansWithCandidateInfo(List<Politician> fedDepList) throws IOException, JSONException {
        List<String> cpfList = new ArrayList<>();
        for (Politician pol : fedDepList) {
            cpfList.add(pol.getCpf());
        }
        List<Politician> candidates = candidateDao.findCandidates(cpfList);

        for (Politician pol : fedDepList) {
            int index = candidates.indexOf(pol);
            pol.fillWithCandidateInfo(candidates.get(index));
        }
    }

    protected abstract List<Politician> parseJsonArray(JSONArray jsonArray) throws JSONException, IOException;
}
