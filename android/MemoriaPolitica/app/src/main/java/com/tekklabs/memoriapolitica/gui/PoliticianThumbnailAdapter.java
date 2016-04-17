package com.tekklabs.memoriapolitica.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tekklabs.memoriapolitica.R;
import com.tekklabs.memoriapolitica.domain.CitizenNotebook;
import com.tekklabs.memoriapolitica.domain.Constants;
import com.tekklabs.memoriapolitica.domain.Politician;
import com.tekklabs.memoriapolitica.domain.PoliticianClass;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
import com.tekklabs.memoriapolitica.gui.notebook.Approval;
import com.tekklabs.memoriapolitica.widget.radio.DeselectableRadioButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by PC on 01/07/2015.
 */
public class PoliticianThumbnailAdapter extends BaseAdapter implements SectionIndexer {

    private static final List<String> sections = new ArrayList<>();
    static {
        Collections.addAll(sections, new String[]{"A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"});
    }

    private MainActivity context;
    private Approval approval;
    private Presenter presenter;
    private CitizenNotebook notebook;

    public PoliticianThumbnailAdapter(MainActivity theContext, Approval theApproval) {
        this.context = theContext;
        this.presenter = context.getPresenter();
        this.notebook = this.presenter.getCurrentNotebook();
        this.approval = theApproval;
    }

    public Collection<PoliticianClassification> getPoliticians() {
        Collection<PoliticianClassification> politicians = notebook.getPoliticians(approval, PoliticianClass.FED_DEP);
        return Collections.unmodifiableCollection(politicians);
    }

    @Override
    public int getCount() {
        return notebook.getPoliticians(approval, PoliticianClass.FED_DEP).size();
    }

    @Override
    public Object getItem(int position) {
        Set<PoliticianClassification> politicians = notebook.getPoliticians(approval, PoliticianClass.FED_DEP);
        return politicians.toArray(new PoliticianClassification[0])[position];
    }

    @Override
    public long getItemId(int position) {
        PoliticianClassification pol = (PoliticianClassification) getItem(position);
        return pol.getPolitician().getCpf().hashCode();
    }

    /**
     * Removes an item from the adapter.
     */
    public void removeItem(PoliticianClassification politician) {
        //politicians.remove(politician);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.politician_thumbnail_layout, null);
        }

        final PoliticianClassification polClassification = (PoliticianClassification) getItem(position);
        Politician pol = polClassification.getPolitician();

        loadPhoto(polClassification, view);

        TextView polName = (TextView) view.findViewById(R.id.politician_name);
        polName.setText(pol.getPoliticianName());

        TextView partyView = (TextView) view.findViewById(R.id.party_name);
        partyView.setText(pol.getPartyName());

        configureButtons(view, polClassification);

        return view;
    }

    private void configureButtons(View view, final PoliticianClassification polClassification) {
        final DeselectableRadioButton btnLike = (DeselectableRadioButton) view.findViewById(R.id.btn_like);
        btnLike.setChecked(approval.equals(Approval.APPROVED));

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApprovalDescDialogFragment dlgFrag = new ApprovalDescDialogFragment();
                Bundle bundle = new Bundle();
                dlgFrag.setArguments(bundle);
                bundle.putSerializable(Constants.POLITICIAN_KEY, polClassification);

                if (btnLike.isChecked()) {
                    bundle.putSerializable(Constants.APPROVAL_KEY, Approval.APPROVED);
                    dlgFrag.show(context.getSupportFragmentManager(), "");
                }
                else {
                    notebook.addNeutralPolitician(PoliticianClass.FED_DEP, polClassification);
                    presenter.saveNotebook(notebook);
                }
            }
        });

        final DeselectableRadioButton btnDislike = (DeselectableRadioButton) view.findViewById(R.id.btn_dislike);
        btnDislike.setChecked(approval.equals(Approval.REPROVED));

        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApprovalDescDialogFragment dlgFrag = new ApprovalDescDialogFragment();
                Bundle bundle = new Bundle();
                dlgFrag.setArguments(bundle);
                bundle.putSerializable(Constants.POLITICIAN_KEY, polClassification);

                if (btnDislike.isChecked()) {
                    bundle.putSerializable(Constants.APPROVAL_KEY, Approval.REPROVED);
                    dlgFrag.show(context.getSupportFragmentManager(), "");
                }
                else {
                    notebook.addNeutralPolitician(PoliticianClass.FED_DEP, polClassification);
                    presenter.saveNotebook(notebook);
                }
            }
        });
    }

    private void loadPhoto(final PoliticianClassification pol, View view) {
        ImageView photo = (ImageView) view.findViewById(R.id.politician_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PoliticianInfoActivity.class);
                intent.putExtra(Constants.POLITICIAN_KEY, pol);
                context.startActivity(intent);
            }
        });

        // Loads image on UI thread. Can cause latency!
        try {
            photo.setImageBitmap(pol.getPolitician().getPhoto(context));
        }
        catch (IOException e) {
            photo.setImageResource(R.drawable.shadow_man);
            e.printStackTrace();
        }
    }

    @Override
    public Object[] getSections() {
        return sections.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex < 0) {
            return 0;
        }

        int index = 0;
        Collection<PoliticianClassification> politicians = notebook.getPoliticians(approval, PoliticianClass.FED_DEP);
        for (PoliticianClassification pol : politicians) {
            String sectionLetter = sections.get(sectionIndex);
            String polLetter = pol.getPolitician().getPoliticianNameFirstLetter();
            if (polLetter.compareTo(sectionLetter) >= 0) {
                return index;
            }

            index++;
        }

        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        Set<PoliticianClassification> politicians = notebook.getPoliticians(approval, PoliticianClass.FED_DEP);
        PoliticianClassification pol = politicians.toArray(new PoliticianClassification[0])[position];
        String letter = pol.getPolitician().getPoliticianNameFirstLetter();
        return sections.indexOf(letter);
    }
}
