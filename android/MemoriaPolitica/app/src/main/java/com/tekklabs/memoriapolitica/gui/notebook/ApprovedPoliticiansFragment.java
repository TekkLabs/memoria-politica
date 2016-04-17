package com.tekklabs.memoriapolitica.gui.notebook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tekklabs.memoriapolitica.domain.CitizenNotebook;
import com.tekklabs.memoriapolitica.domain.Constants;
import com.tekklabs.memoriapolitica.domain.PoliticianClass;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.MainActivity;
import com.tekklabs.memoriapolitica.gui.PoliticianThumbnailAdapter;
import com.tekklabs.memoriapolitica.gui.Presenter;

/**
 * Created by PC on 05/07/2015.
 */
public class ApprovedPoliticiansFragment extends PoliticianFragment {

    public static PoliticianFragment newInstance(Bundle args) {
        PoliticianFragment fragment = new ApprovedPoliticiansFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ApprovedPoliticiansFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        PoliticianClass polClass = (PoliticianClass) args.getSerializable(Constants.POLITICIAN_CLASS);
        MainActivity activity = (MainActivity) getActivity();
        mAdapter = new PoliticianThumbnailAdapter(activity, Approval.APPROVED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogClick(PoliticianClassification polClassification) {
        Presenter presenter = ((MainActivity)getActivity()).getPresenter();
        CitizenNotebook notebook = presenter.getCurrentNotebook();
        notebook.addApprovedPolitician(PoliticianClass.FED_DEP, polClassification);
        presenter.saveNotebook(notebook);
    }
}