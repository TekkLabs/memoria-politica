package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import com.tekklabs.memoriapolitica.domain.CitizenNotebook;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.SortMode;

import java.util.List;

/**
 * Created by taciosd on 4/22/16.
 */
public class ListViewSectionManager implements ListViewSectionIndexer {

    private ListViewSectionIndexer sectionsToShow = new ListViewSectionByName();
    private SortMode currentSortMode = SortMode.BY_NAME;

    private final CitizenNotebook notebook;

    public ListViewSectionManager(CitizenNotebook theNotebook) {
        this.notebook = theNotebook;
    }

    public SortMode getCurrentSortMode() {
        return currentSortMode;
    }

    public void setSortMode(SortMode sortMode) {

        this.currentSortMode = sortMode;
        switch (sortMode) {
            case BY_NAME:
                this.sectionsToShow = new ListViewSectionByName();
                break;
            case BY_PARTY:
                this.sectionsToShow = new ListViewSectionByParty();
                break;
            case BY_STATE:
                this.sectionsToShow = new ListViewSectionByState();
                break;
            case BY_APPROVED_FIRST:
            case BY_NEUTRAL_FIRST:
            case BY_REPROVED_FIRST:
                this.sectionsToShow = new ListViewSectionByApproval(sortMode);
                break;
        }
    }

    @Override
    public void updatePoliticianList(List<PoliticianClassification> politicians) {
        this.sectionsToShow.updatePoliticianList(politicians);
    }

    @Override
    public Object[] getSections() {
        return sectionsToShow.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionsToShow.getPositionForSection(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionsToShow.getSectionForPosition(position);
    }
}
