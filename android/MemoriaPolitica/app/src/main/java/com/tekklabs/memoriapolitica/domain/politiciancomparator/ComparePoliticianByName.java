package com.tekklabs.memoriapolitica.domain.politiciancomparator;

import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.util.StringUtil;

import java.util.Comparator;

/**
 * Created by taciosd on 4/20/16.
 */
public class ComparePoliticianByName implements Comparator<PoliticianClassification> {

    @Override
    public int compare(PoliticianClassification lhs, PoliticianClassification rhs) {
        String lhsName = lhs.getPolitician().getPoliticianNameWithNoAccents();
        String rhsName = rhs.getPolitician().getPoliticianNameWithNoAccents();
        return lhsName.compareToIgnoreCase(rhsName);
    }
}
