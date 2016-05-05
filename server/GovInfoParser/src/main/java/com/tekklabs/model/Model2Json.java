package com.tekklabs.model;

import com.tekklabs.model.entities.FedDep;
import com.tekklabs.model.entities.JsonAttributes;
import com.tekklabs.model.entities.Candidate;
import com.tekklabs.model.entities.Senator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by PC on 24/07/2015.
 */
public class Model2Json {

    public JSONObject cand2Json(Candidate pol) {
        JSONObject jObj = new JSONObject();
        jObj.put(JsonAttributes.NOME_CIVIL, pol.civilName);
        jObj.put(JsonAttributes.NOME_POLITICO, pol.politicianName);
        jObj.put(JsonAttributes.CPF_CANDIDATO, pol.cpf);
        jObj.put(JsonAttributes.NUM_TITULO_ELEITORAL_CANDIDATO, pol.numTituloEleitoral);
        jObj.put(JsonAttributes.PARTIDO, pol.party);
        jObj.put(JsonAttributes.SIGLA_UF, pol.uf);
        jObj.put(JsonAttributes.STATUS, pol.uf);
        return jObj;
    }

    public JSONArray candList2Json(List<Candidate> candList) {
        JSONArray jArr = new JSONArray();
        for (Candidate cand: candList) {
            JSONObject jObj = cand2Json(cand);
            jArr.add(jObj);
        }
        return jArr;
    }

    public JSONObject fedDep2Json(FedDep fedDep) {
        JSONObject jObj = new JSONObject();
        jObj.put(JsonAttributes.NOME_POLITICO, fedDep.politicianName);
        jObj.put(JsonAttributes.NOME_CIVIL, fedDep.civilName);
        jObj.put(JsonAttributes.CPF_CANDIDATO, fedDep.cpf);
        jObj.put(JsonAttributes.SEXO, fedDep.sex);
        jObj.put(JsonAttributes.STATUS, fedDep.status);
        jObj.put(JsonAttributes.PARTIDO, fedDep.party);
        jObj.put(JsonAttributes.EMAIL, fedDep.email);
        jObj.put(JsonAttributes.TELEFONE, fedDep.phone);
        jObj.put(JsonAttributes.GABINETE, fedDep.office);
        jObj.put(JsonAttributes.ANEXO, fedDep.annex);
        return jObj;
    }

    public JSONArray fedDepList2Json(List<FedDep> fedDepList) {
        JSONArray jArr = new JSONArray();
        for (FedDep dep: fedDepList) {
            JSONObject jObj = fedDep2Json(dep);
            jArr.add(jObj);
        }
        return jArr;
    }

    public JSONObject senator2Json(Senator senator) {
        JSONObject jObj = new JSONObject();
        jObj.put(JsonAttributes.CPF_CANDIDATO, senator.cpf);
        jObj.put(JsonAttributes.NOME_POLITICO, senator.politicianName);
        jObj.put(JsonAttributes.PARTIDO, senator.party);
        jObj.put(JsonAttributes.SIGLA_UF, senator.uf);
        jObj.put(JsonAttributes.PHONE, senator.phone);
        jObj.put(JsonAttributes.FAX, senator.fax);
        return jObj;
    }

    public JSONArray senatorList2Json(List<Senator> senatorList) {
        JSONArray jArr = new JSONArray();
        for (Senator sen: senatorList) {
            JSONObject jObj = senator2Json(sen);
            jArr.add(jObj);
        }
        return jArr;
    }
}
