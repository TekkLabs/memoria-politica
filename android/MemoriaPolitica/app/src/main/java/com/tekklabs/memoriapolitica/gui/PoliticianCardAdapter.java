package com.tekklabs.memoriapolitica.gui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tekklabs.memoriapolitica.R;
import com.tekklabs.memoriapolitica.domain.Approval;
import com.tekklabs.memoriapolitica.domain.CitizenNotebook;
import com.tekklabs.memoriapolitica.domain.Constants;
import com.tekklabs.memoriapolitica.domain.Politician;
import com.tekklabs.memoriapolitica.domain.PoliticianClass;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;
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
public class PoliticianCardAdapter extends RecyclerView.Adapter<PoliticianCardAdapter.ViewHolder> implements SectionIndexer {

    private final ListViewSectionManager listSectionManager;
    private CitizenNotebook notebook;
    private List<PoliticianClassification> politiciansToShow = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView polName, partyView;
        public ImageView photo;
        public DeselectableRadioButton btnLike, btnDislike;

        public ViewHolder(View itemView) {
            super(itemView);

            polName = (TextView) itemView.findViewById(R.id.politician_name);
            partyView = (TextView) itemView.findViewById(R.id.party_and_state_name);
            photo = (ImageView) itemView.findViewById(R.id.politician_photo);
            btnLike = (DeselectableRadioButton) itemView.findViewById(R.id.btn_like);
            btnDislike = (DeselectableRadioButton) itemView.findViewById(R.id.btn_dislike);
        }
    }

    public PoliticianCardAdapter(CitizenNotebook theNotebook) {
        this.notebook = theNotebook;
        politiciansToShow.addAll(notebook.getAllPoliticians(PoliticianClass.FED_DEP));
        this.listSectionManager = new ListViewSectionManager(this.notebook);
        this.listSectionManager.updatePoliticianList(politiciansToShow);
    }
/*
    public Collection<PoliticianClassification> getPoliticians() {
        return Collections.unmodifiableCollection(politiciansToShow);
    }
*/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        View view = vi.inflate(R.layout.politician_thumbnail_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoliticianClassification polClassification = politiciansToShow.get(position);
        Politician pol = polClassification.getPolitician();

        holder.polName.setText(pol.getPoliticianName());
        holder.partyView.setText(pol.getParty().getAcronym() + " - " + pol.getUf());
        loadPhoto(polClassification, holder.photo);
        configureButtonLike(polClassification, holder.btnLike);
        configureButtonDislike(polClassification, holder.btnDislike);
    }

    private void loadPhoto(final PoliticianClassification pol, final ImageView photoView) {
        // Loads image on UI thread. Can cause latency!
        try {
            photoView.setImageBitmap(pol.getPolitician().getPhoto(photoView.getContext()));
        }
        catch (IOException e) {
            photoView.setImageResource(R.drawable.shadow_man);
            e.printStackTrace();
        }

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(photoView.getContext(), PoliticianInfoActivity.class);
                intent.putExtra(Constants.POLITICIAN_KEY, pol);
                photoView.getContext().startActivity(intent);
            }
        });
    }

    private void configureButtonLike(final PoliticianClassification polClassification,
                                     final DeselectableRadioButton btnLike) {
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
                new Presenter(btnLike.getContext()).saveNotebook(notebook);
                sortItems(listSectionManager.getCurrentSortMode());
            }
        });
    }

    private void configureButtonDislike(final PoliticianClassification polClassification,
                                        final DeselectableRadioButton btnDislike) {
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
                new Presenter(btnDislike.getContext()).saveNotebook(notebook);
                sortItems(listSectionManager.getCurrentSortMode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return politiciansToShow.size();
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
