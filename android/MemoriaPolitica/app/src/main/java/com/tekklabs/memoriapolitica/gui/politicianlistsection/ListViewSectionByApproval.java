package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import com.tekklabs.memoriapolitica.domain.Approval;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.CompareApproval;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByApproval;
import com.tekklabs.memoriapolitica.gui.SortMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taciosd on 4/25/16.
 */
public class ListViewSectionByApproval implements ListViewSectionIndexer {

    private final List<Approval> sectionsToShow = new ArrayList<>();
    private final SortMode sortMode;

    private List<PoliticianClassification> politicians;


    public ListViewSectionByApproval(SortMode sortMode) {
        if (!sortMode.equals(SortMode.BY_APPROVED_FIRST)
                && !sortMode.equals(SortMode.BY_NEUTRAL_FIRST)
                && !sortMode.equals(SortMode.BY_REPROVED_FIRST)) {
            throw new IllegalArgumentException("Modo de ordenacão inválido!");
        }
        this.sortMode = sortMode;
    }

    @Override
    public void updatePoliticianList(List<PoliticianClassification> politicians) {
        this.politicians = politicians;

        sectionsToShow.clear();
        for (PoliticianClassification polClassification : politicians) {
            Approval polApproval = polClassification.getApproval();
            if (!sectionsToShow.contains(polApproval)) {
                sectionsToShow.add(polApproval);
            }
        }
        Collections.sort(sectionsToShow, new CompareApproval(sortMode));
    }

    @Override
    public Object[] getSections() {
        return sectionsToShow.toArray(new Approval[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        Approval section = sectionsToShow.get(sectionIndex);
        Comparator<Approval> comparator = new CompareApproval(sortMode);

        int lesserIndex = 0;
        int index = 0;
        for (PoliticianClassification pol : politicians) {
            Approval polApproval = pol.getApproval();
            if (comparator.compare(polApproval, section) >= 0) {
                return index;
            }

            lesserIndex = index;
            index++;
        }

        return lesserIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        PoliticianClassification pol = politicians.toArray(new PoliticianClassification[0])[position];
        Approval polApproval = pol.getApproval();
        return sectionsToShow.indexOf(polApproval);
    }
}
