package com.tekklabs.model.entities;

/**
 * Created by PC on 24/07/2015.
 */
public class Candidate {

    public String cpf;
    public String civilName;
    public String politicianName;
    public String numTituloEleitoral;
    public String uf;
    public String party;

    public Candidate(String civilName, String cpf) {
        this.civilName = civilName;
        this.cpf = cpf;
    }
}
