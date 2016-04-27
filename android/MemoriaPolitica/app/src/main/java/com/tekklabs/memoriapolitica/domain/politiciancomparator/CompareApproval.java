package com.tekklabs.memoriapolitica.domain.politiciancomparator;

import com.tekklabs.memoriapolitica.domain.Approval;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.SortMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taciosd on 4/25/16.
 */
public class CompareApproval implements Comparator<Approval> {

    private final SortMode sortMode;

    public CompareApproval(SortMode sortMode) {
        this.sortMode = sortMode;
    }

    @Override
    public int compare(Approval lhs, Approval rhs) {
        List<Approval> order = new ArrayList<>();

        switch (sortMode) {
            case BY_APPROVED_FIRST:
                order.add(Approval.APPROVED);
                order.add(Approval.NEUTRAL);
                order.add(Approval.REPROVED);
                return compareUsingOrder(lhs, rhs, order);
            case BY_NEUTRAL_FIRST:
                order.add(Approval.NEUTRAL);
                order.add(Approval.APPROVED);
                order.add(Approval.REPROVED);
                return compareUsingOrder(lhs, rhs, order);
            case BY_REPROVED_FIRST:
                order.add(Approval.REPROVED);
                order.add(Approval.APPROVED);
                order.add(Approval.NEUTRAL);
                return compareUsingOrder(lhs, rhs, order);
        }

        throw new IllegalStateException("O modo de ordenacão é inválido. " + sortMode);
    }

    private int compareUsingOrder(Approval lhs,
                                  Approval rhs,
                                  List<Approval> order) {
        int lhsIndex = order.indexOf(lhs);
        int rhsIndex = order.indexOf(rhs);

        return (lhsIndex - rhsIndex);
    }
}
