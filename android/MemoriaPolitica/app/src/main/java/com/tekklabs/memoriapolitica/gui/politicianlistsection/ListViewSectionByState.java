package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import com.tekklabs.memoriapolitica.domain.PoliticianClassification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by taciosd on 4/22/16.
 */
public class ListViewSectionByState implements ListViewSectionIndexer {

    private static final List<String> sectionsToShow = new ArrayList<>();
    private List<PoliticianClassification> politicians;

    public ListViewSectionByState() {
    }

    @Override
    public void updatePoliticianList(List<PoliticianClassification> politicians) {
        this.politicians = politicians;

        sectionsToShow.clear();
        for (PoliticianClassification polClassification : politicians) {
            String politicianUf = polClassification.getPolitician().getUf();
            if (!sectionsToShow.contains(politicianUf)) {
                sectionsToShow.add(politicianUf);
            }
        }
        Collections.sort(sectionsToShow);
    }

    @Override
    public Object[] getSections() {
        return sectionsToShow.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        String section = sectionsToShow.get(sectionIndex);

        int lesserIndex = 0;
        for (int index = 0; index < politicians.size(); index++) {
            PoliticianClassification polClassification = politicians.get(index);
            String polUf = polClassification.getPolitician().getUf();
            if (polUf.equalsIgnoreCase(section)) {
                return index;
            }

            lesserIndex = index;
        }

        return lesserIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position >= politicians.size()) {
            position = politicians.size() - 1;
        }
        PoliticianClassification polClassification = politicians.get(position);
        String polUf = polClassification.getPolitician().getUf();

        int lesserIndex = 0;
        for (int index = 0; index < sectionsToShow.size(); index++) {
            String section = sectionsToShow.get(index);
            if (section.equalsIgnoreCase(polUf)) {
                return index;
            }

            lesserIndex = index;
        }

        return lesserIndex;
    }
}
