package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import com.tekklabs.memoriapolitica.domain.CitizenNotebook;
import com.tekklabs.memoriapolitica.domain.Party;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by taciosd on 4/22/16.
 */
public class ListViewSectionByParty implements ListViewSectionIndexer {

    private final List<String> sectionsToShow = new ArrayList<>();
    private List<PoliticianClassification> politicians;

    public ListViewSectionByParty() {
    }

    @Override
    public void updatePoliticianList(List<PoliticianClassification> politicians) {
        this.politicians = politicians;

        sectionsToShow.clear();
        for (PoliticianClassification polClassification : politicians) {
            String partyAcronym = polClassification.getPolitician().getParty().getAcronym();
            if (!sectionsToShow.contains(partyAcronym)) {
                sectionsToShow.add(partyAcronym);
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
            String party = polClassification.getPolitician().getParty().getAcronym();
            if (party.equalsIgnoreCase(section)) {
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
        String party = polClassification.getPolitician().getParty().getAcronym();

        int lesserIndex = 0;
        for (int index = 0; index < sectionsToShow.size(); index++) {
            String section = sectionsToShow.get(index);
            if (section.equalsIgnoreCase(party)) {
                return index;
            }

            lesserIndex = index;
        }

        return lesserIndex;
    }
}
