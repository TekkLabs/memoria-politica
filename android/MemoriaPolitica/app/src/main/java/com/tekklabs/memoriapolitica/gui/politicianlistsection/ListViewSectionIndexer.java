package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import android.widget.SectionIndexer;

import com.tekklabs.memoriapolitica.domain.PoliticianClassification;

import java.util.List;

/**
 * Created by taciosd on 4/22/16.
 */
public interface ListViewSectionIndexer extends SectionIndexer {

    void updatePoliticianList(List<PoliticianClassification> politicians);
}
