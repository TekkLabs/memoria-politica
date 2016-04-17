package com.tekklabs.memoriapolitica.db;

import android.content.Context;

import com.tekklabs.memoriapolitica.domain.Politician;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.notebook.Approval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by taciosd on 1/24/16.
 */
public class PoliticianClassificationDao extends Dao {

    protected static final String CPF_KEY = "cpf";
    private String APPROVAL_DESC_KEY = "approval_desc";

    private FedDepDao politicianDao;

    public PoliticianClassificationDao(Context aContext, FedDepDao politicianDao) {
        super(aContext);
        this.politicianDao = politicianDao;
    }

    /**
     * Converte o JSON em um array de classificacões.
     *
     * @param jArray
     * @param approval
     * @return
     * @throws JSONException
     */
    public List<PoliticianClassification> getClassificationsFrom(JSONArray jArray, Approval approval) throws JSONException {
        List<PoliticianClassification> classifications = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jsonObj = jArray.getJSONObject(i);
            PoliticianClassification polClassification = createClassificationFrom(jsonObj, approval);
            classifications.add(polClassification);
        }

        return classifications;
    }

    private PoliticianClassification createClassificationFrom(JSONObject jsonObj, Approval approval) throws JSONException {
        String cpf = jsonObj.getString(CPF_KEY);
        Politician pol = politicianDao.getByCpf(cpf);
        String description = jsonObj.getString(APPROVAL_DESC_KEY);

        PoliticianClassification classification = new PoliticianClassification(pol);
        classification.setApproval(approval);
        classification.setApprovalReason(description);
        return classification;
    }

    /**
     * Converte um conjunto de classificacões para um array em JSON.
     * @param approvedSet
     * @return
     * @throws JSONException
     */
    public JSONArray createJsonArrayFrom(Set<PoliticianClassification> approvedSet) throws JSONException {
        JSONArray jArray = new JSONArray();
        for (PoliticianClassification classification : approvedSet) {
            JSONObject jObj = new JSONObject();
            jObj.put(CPF_KEY, classification.getPolitician().getCpf());
            jObj.put(APPROVAL_DESC_KEY, classification.getReason());
            jArray.put(jObj);
        }

        return jArray;
    }

    /**
     * Retorna uma colećão de classificacoes neutras com todos os políticos.
     * @return
     */
    public Collection<PoliticianClassification> getAllNeutral() {
        ArrayList<PoliticianClassification> classifications = new ArrayList<>();

        Collection<Politician> politicians = politicianDao.getAll();
        for (Politician pol : politicians) {
            PoliticianClassification classification = new PoliticianClassification(pol);
            classification.setApproval(Approval.NEUTRAL);
            classifications.add(classification);
        }

        return classifications;
    }
}
