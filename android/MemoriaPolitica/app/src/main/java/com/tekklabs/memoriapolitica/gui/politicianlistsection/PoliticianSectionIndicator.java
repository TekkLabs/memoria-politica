package com.tekklabs.memoriapolitica.gui.politicianlistsection;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.tekklabs.memoriapolitica.domain.Approval;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.SortMode;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;

/**
 * Created by taciosd on 5/1/16.
 */
public class PoliticianSectionIndicator extends SectionTitleIndicator {

    public PoliticianSectionIndicator(Context context) {
        super(context);
    }

    public PoliticianSectionIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PoliticianSectionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSection(Object section) {
        setTitleText(section.toString());
        //setIndicatorTextColor(Color.WHITE);
    }
}
