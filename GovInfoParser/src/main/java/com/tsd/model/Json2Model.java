package com.tsd.model;

import com.tsd.model.entities.Candidate;
import com.tsd.model.entities.JsonAttributes;
import com.tsd.model.entities.FedDep;
import com.tsd.model.entities.Senator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by PC on 24/07/2015.
 */
public class Json2Model {

    public static List<Candidate> json2Candidate(JSONArray jArray) {
        List<Candidate> list = new ArrayList<Candidate>();

        Iterator it = jArray.iterator();
        while (it.hasNext()) {
            JSONObject jObj = (JSONObject) it.next();
            Candidate pol = createCandidate(jObj);
            list.add(pol);
        }

        return list;
    }

    private static Candidate createCandidate(JSONObject jObj) {
        String cpf = (String) jObj.keySet().iterator().next();
        JSONObject jCandInfo = (JSONObject) jObj.get(cpf);

        String civilName = (String) jCandInfo.get(JsonAttributes.NOME_CIVIL);
        if (civilName == null) {
            throw new IllegalStateException("O objeto JSON não tem o campo obrigatório NOME_CIVIL.");
        }

        String polName = (String) jCandInfo.get(JsonAttributes.NOME_POLITICO);

        String numTitulo = (String) jCandInfo.get(JsonAttributes.NUM_TITULO_ELEITORAL_CANDIDATO);
        String uf = (String) jCandInfo.get(JsonAttributes.SIGLA_UF);
        String party = (String) jCandInfo.get(JsonAttributes.PARTIDO);

        Candidate pol = new Candidate(civilName, cpf);
        pol.politicianName = polName;
        pol.cpf = cpf;
        pol.numTituloEleitoral = numTitulo;
        pol.uf = uf;
        pol.party = party;

        return pol;
    }

    public static List<FedDep> json2FedDep(JSONArray jArray) {
        List<FedDep> list = new ArrayList<FedDep>();

        Iterator it = jArray.iterator();
        while (it.hasNext()) {
            JSONObject jObj = (JSONObject) it.next();
            FedDep pol = createFedDep(jObj);
            list.add(pol);
        }

        return list;
    }

    private static FedDep createFedDep(JSONObject jObj) {
        String civilName = (String) jObj.get(JsonAttributes.NOME_CIVIL);
        if (civilName == null) {
            throw new IllegalStateException("O objeto JSON n�o tem o campo obrigat�rio NOME_CIVIL.");
        }

        String email = (String) jObj.get(JsonAttributes.EMAIL);
        String party = (String) jObj.get(JsonAttributes.PARTIDO);
        String status = (String) jObj.get(JsonAttributes.STATUS);

        FedDep pol = new FedDep();
        pol.civilName = civilName;
        pol.email = email;
        pol.party = party;
        pol.status = status;

        return pol;
    }

    public static List<Senator> json2Senator(JSONArray jsonArray) {
        List<Senator> list = new ArrayList<Senator>();

        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject jObj = (JSONObject) it.next();
            Senator pol = createSenator(jObj);
            list.add(pol);
        }

        return list;
    }

    private static Senator createSenator(JSONObject jObj) {
        String politicianName = (String) jObj.get(JsonAttributes.NOME_POLITICO);
        if (politicianName == null) {
            throw new IllegalStateException("O objeto JSON nao tem o campo obrigatorio NOME_POLITICO.");
        }

        String party = (String) jObj.get(JsonAttributes.PARTIDO);
        String uf = (String) jObj.get(JsonAttributes.SIGLA_UF);
        String phone = (String) jObj.get(JsonAttributes.PHONE);
        String fax = (String) jObj.get(JsonAttributes.FAX);

        Senator pol = new Senator();
        pol.politicianName = politicianName;
        pol.party = party;
        pol.uf = uf;
        pol.phone = phone;
        pol.fax = fax;

        return pol;
    }
}
