package com.tekklabs.memoriapolitica.domain;

/**
 * Created by taciosd on 7/31/15.
 */
public enum Approval {
    APPROVED("Aprovado"),
    NEUTRAL("Neutro"),
    REPROVED("Reprovado");

    private String approvalStr;
    Approval(String str) {
        this.approvalStr = str;
    }

    @Override
    public String toString() {
        return approvalStr;
    }
}
