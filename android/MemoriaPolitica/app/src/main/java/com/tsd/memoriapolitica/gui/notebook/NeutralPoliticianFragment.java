package com.tsd.memoriapolitica.gui.notebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Constants;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClass;
import com.tsd.memoriapolitica.domain.PoliticianClassification;
import com.tsd.memoriapolitica.gui.MainActivity;
import com.tsd.memoriapolitica.gui.PoliticianThumbnailAdapter;
import com.tsd.memoriapolitica.gui.Presenter;

import java.util.Collection;
import java.util.Set;

/**
 * Created by PC on 18/07/2015.
 */
public class NeutralPoliticianFragment extends PoliticianFragment {

    public static PoliticianFragment newInstance(Bundle args) {
        PoliticianFragment fragment = new NeutralPoliticianFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NeutralPoliticianFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        PoliticianClass polClass = (PoliticianClass) args.getSerializable(Constants.POLITICIAN_CLASS);
        mAdapter = new PoliticianThumbnailAdapter((MainActivity) getActivity(), Approval.NEUTRAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }
}
