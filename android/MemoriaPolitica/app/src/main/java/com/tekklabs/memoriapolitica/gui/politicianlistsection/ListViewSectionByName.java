package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import com.tekklabs.memoriapolitica.domain.PoliticianClassification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by taciosd on 4/22/16.
 */
public class ListViewSectionByName implements ListViewSectionIndexer {

    private static final List<String> sections = new ArrayList<>();
    private List<PoliticianClassification> politicians;

    public ListViewSectionByName() {
    }

    @Override
    public void updatePoliticianList(List<PoliticianClassification> politicians) {
        this.politicians = politicians;

        sections.clear();
        for (PoliticianClassification polClassification : politicians) {
            String firstLetter = polClassification.getPolitician().getPoliticianNameFirstLetter();
            if (!sections.contains(firstLetter)) {
                sections.add(firstLetter);
            }
        }
        Collections.sort(sections);
    }

    @Override
    public Object[] getSections() {
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {

        int lesserIndex = 0;
        int index = 0;
        for (PoliticianClassification pol : politicians) {
            String sectionLetter = sections.get(sectionIndex);
            String polLetter = pol.getPolitician().getPoliticianNameFirstLetter();
            if (polLetter.compareTo(sectionLetter) >= 0) {
                return index;
            }

            lesserIndex = index;
            index++;
        }

        return lesserIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position >= politicians.size()) {
            position = politicians.size() - 1;
        }
        PoliticianClassification pol = politicians.get(position);
        String letter = pol.getPolitician().getPoliticianNameFirstLetter();
        return sections.indexOf(letter);
    }
}
