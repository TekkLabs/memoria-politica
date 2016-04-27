package com.tekklabs.memoriapolitica.domain.politiciancomparator;

import com.tekklabs.memoriapolitica.domain.PoliticianClassification;

import java.util.Comparator;

/**
 * Created by taciosd on 4/20/16.
 */
public class ComparePoliticianByParty implements Comparator<PoliticianClassification> {

    @Override
    public int compare(PoliticianClassification lhs, PoliticianClassification rhs) {
        int compareParty = lhs.getPolitician().getParty().getAcronym()
                            .compareTo(rhs.getPolitician().getParty().getAcronym());
        if (compareParty == 0) {
            return new ComparePoliticianByName().compare(lhs, rhs);
        }
        else {
            return compareParty;
        }
    }
}
