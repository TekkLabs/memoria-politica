package com.tekklabs.memoriapolitica.domain;

import java.io.Serializable;

/**
 * Created by taciosd on 4/20/16.
 */
public class Party implements Serializable {

    private int code;
    private String acronym;
    private String name;

    public Party(int code, String acronym, String name) {
        this.code = code;
        this.acronym = acronym;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getName() {
        return name;
    }

    public String getPartyNameFirstLetter() {
        return getName().substring(0, 0);
    }
}
