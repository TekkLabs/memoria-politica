package com.tekklabs.memoriapolitica.domain.politiciancomparator;

import com.tekklabs.memoriapolitica.domain.Approval;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.SortMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taciosd on 4/20/16.
 */
public class ComparePoliticianByApproval implements Comparator<PoliticianClassification> {

    private final SortMode sortMode;

    public ComparePoliticianByApproval(SortMode sortMode) {
        if (!sortMode.equals(SortMode.BY_APPROVED_FIRST)
                && !sortMode.equals(SortMode.BY_NEUTRAL_FIRST)
                && !sortMode.equals(SortMode.BY_REPROVED_FIRST)) {
            throw new IllegalArgumentException("Modo de ordenacão inválido!");
        }

        this.sortMode = sortMode;
    }

    @Override
    public int compare(PoliticianClassification lhs, PoliticianClassification rhs) {
        int compareApproval = new CompareApproval(sortMode).compare(lhs.getApproval(), rhs.getApproval());
        if (compareApproval == 0) {
            return new ComparePoliticianByName().compare(lhs, rhs);
        }
        else {
            return compareApproval;
        }
    }
}
