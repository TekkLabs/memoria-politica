package com.tekklabs.memoriapolitica.domain;

import com.tekklabs.memoriapolitica.gui.notebook.Approval;

import java.io.Serializable;

/**
 * Classe que classifica um político como aprovado, reprovado ou neutro.
 * Essa classificacão pode ter uma justificativa textual.
 *
 * Created by taciosd on 1/23/16.
 */
public class PoliticianClassification implements Comparable, Serializable {

    private Approval approval = Approval.NEUTRAL;
    private String reason = "";
    private Politician politician;

    public PoliticianClassification(Politician aPolitician) {
        this.politician = aPolitician;
    }

    public void setApproval(Approval theApproval) {
        this.approval = theApproval;
    }

    public Approval getApproval() {
        return this.approval;
    }

    public String getReason() {
        return reason;
    }

    public void setApprovalReason(String aReason) {
        this.reason = aReason;
    }

    public Politician getPolitician() {
        return politician;
    }

    @Override
    public int compareTo(Object another) {
        PoliticianClassification anotherPol = (PoliticianClassification) another;
        return politician.compareTo(anotherPol.getPolitician());
    }
}
