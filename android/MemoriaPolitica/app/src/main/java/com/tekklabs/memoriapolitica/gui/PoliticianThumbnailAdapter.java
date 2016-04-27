package com.tekklabs.memoriapolitica.gui;

import android.content.Intent;
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
import com.tekklabs.memoriapolitica.domain.Approval;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByApproval;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByName;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByParty;
import com.tekklabs.memoriapolitica.domain.politiciancomparator.ComparePoliticianByState;
import com.tekklabs.memoriapolitica.gui.politicianlistsection.ListViewSectionManager;
import com.tekklabs.memoriapolitica.widget.radio.DeselectableRadioButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by PC on 01/07/2015.
 */
public class PoliticianThumbnailAdapter extends BaseAdapter implements SectionIndexer {


    private final ListViewSectionManager listSectionManager;
    private MainActivity context;
    private CitizenNotebook notebook;
    private List<PoliticianClassification> politiciansToShow = new ArrayList<>();


    public PoliticianThumbnailAdapter(MainActivity theContext) {
        this.context = theContext;
        this.notebook = context.getNotebook();
        politiciansToShow.addAll(notebook.getAllPoliticians(PoliticianClass.FED_DEP));
        this.listSectionManager = new ListViewSectionManager(this.notebook);
        this.listSectionManager.updatePoliticianList(politiciansToShow);
    }

    public Collection<PoliticianClassification> getPoliticians() {
        return Collections.unmodifiableCollection(politiciansToShow);
    }

    @Override
    public int getCount() {
        return politiciansToShow.size();
    }

    @Override
    public Object getItem(int position) {
        return politiciansToShow.toArray(new PoliticianClassification[0])[position];
    }

    @Override
    public long getItemId(int position) {
        PoliticianClassification pol = (PoliticianClassification) getItem(position);
        return pol.getPolitician().getCpf().hashCode();
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

        TextView partyView = (TextView) view.findViewById(R.id.party_and_state_name);
        partyView.setText(pol.getParty().getAcronym() + " - " + pol.getUf());

        configureButtons(view, polClassification);

        return view;
    }

    private void configureButtons(View view, final PoliticianClassification polClassification) {
        final DeselectableRadioButton btnLike = (DeselectableRadioButton) view.findViewById(R.id.btn_like);
        btnLike.setChecked(polClassification.getApproval().equals(Approval.APPROVED));

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLike.isChecked()) {
                    notebook.addApprovedPolitician(PoliticianClass.FED_DEP, polClassification);
                }
                else {
                    notebook.addNeutralPolitician(PoliticianClass.FED_DEP, polClassification);
                }
                new Presenter(context).saveNotebook(notebook);
                sortItems(listSectionManager.getCurrentSortMode());
            }
        });

        final DeselectableRadioButton btnDislike = (DeselectableRadioButton) view.findViewById(R.id.btn_dislike);
        btnDislike.setChecked(polClassification.getApproval().equals(Approval.REPROVED));

        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnDislike.isChecked()) {
                    notebook.addReprovedPolitician(PoliticianClass.FED_DEP, polClassification);
                }
                else {
                    notebook.addNeutralPolitician(PoliticianClass.FED_DEP, polClassification);
                }
                new Presenter(context).saveNotebook(notebook);
                sortItems(listSectionManager.getCurrentSortMode());
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
        return listSectionManager.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return listSectionManager.getPositionForSection(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return listSectionManager.getSectionForPosition(position);
    }

    public void sortItems(SortMode sortMode) {
        switch (sortMode) {
            case BY_STATE:
                Collections.sort(politiciansToShow, new ComparePoliticianByState());
                break;
            case BY_PARTY:
                Collections.sort(politiciansToShow, new ComparePoliticianByParty());
                break;
            case BY_NAME:
                Collections.sort(politiciansToShow, new ComparePoliticianByName());
                break;
            case BY_APPROVED_FIRST:
            case BY_NEUTRAL_FIRST:
            case BY_REPROVED_FIRST:
                Collections.sort(politiciansToShow, new ComparePoliticianByApproval(sortMode));
                break;
            default:
                throw new IllegalArgumentException("Modo de ordenacao inválido.");
        }

        listSectionManager.setSortMode(sortMode);
        listSectionManager.updatePoliticianList(politiciansToShow);
        notifyDataSetChanged();
    }
}
