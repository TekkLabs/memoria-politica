package com.tsd.memoriapolitica.gui.notebook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tsd.memoriapolitica.db.DaoFactory;
import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Constants;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClass;
import com.tsd.memoriapolitica.domain.PoliticianClassification;
import com.tsd.memoriapolitica.gui.Presenter;

/**
 * Created by PC on 16/07/2015.
 */
public class PoliticianListPagerAdapter extends FragmentPagerAdapter {

    public static final int APPROVED_ITEM = 0;
    public static final int NEUTRAL_ITEM = 1;
    public static final int REPROVED_ITEM = 2;

    private PoliticianFragment approvedPolFrag;
    private PoliticianFragment reprovedPolFrag;
    private PoliticianFragment neutralPolFrag;


    public PoliticianListPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void setPoliticianClass(PoliticianClass polClass) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.POLITICIAN_CLASS, polClass);

        approvedPolFrag = ApprovedPoliticiansFragment.newInstance(bundle);
        reprovedPolFrag = ReprovedPoliticiansFragment.newInstance(bundle);
        neutralPolFrag = NeutralPoliticianFragment.newInstance(bundle);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return approvedPolFrag;
            case 1: return neutralPolFrag;
            case 2: return reprovedPolFrag;
            default: throw new IllegalArgumentException("Tab inválida");
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case APPROVED_ITEM: return "Aprovados";
            case REPROVED_ITEM: return "Reprovados";
            case NEUTRAL_ITEM: return "Neutros";
            default: throw new IllegalArgumentException("Tab inválida");
        }
    }

    public void notifyFragments() {
        approvedPolFrag.refresh();
        reprovedPolFrag.refresh();
        neutralPolFrag.refresh();
    }

    public void onDialogPositiveClick(PoliticianClassification polClassification) {
        if (approvedPolFrag.isResumed()) {
            approvedPolFrag.onDialogPositiveClick(polClassification);
        }
        else if (neutralPolFrag.isResumed()) {
            neutralPolFrag.onDialogPositiveClick(polClassification);
        }
        else if (reprovedPolFrag.isResumed()) {
            reprovedPolFrag.onDialogPositiveClick(polClassification);
        }
        notifyFragments();
    }
}
