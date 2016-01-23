package com.tsd.memoriapolitica.gui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tsd.memoriapolitica.R;
import com.tsd.memoriapolitica.domain.Constants;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.gui.notebook.Approval;
import com.tsd.memoriapolitica.util.StringUtil;
import com.tsd.memoriapolitica.widget.radio.DeselectableRadioButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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
    private Set<Politician> politicians;
    private Approval approval;

    public PoliticianThumbnailAdapter(MainActivity theContext,
                                      Set<Politician> somePoliticians,
                                      Approval theApproval) {
        this.context = theContext;
        this.politicians = somePoliticians;
        this.approval = theApproval;
    }

    public Collection<Politician> getPoliticians() {
        return Collections.unmodifiableCollection(politicians);
    }

    @Override
    public int getCount() {
        return politicians.size();
    }

    @Override
    public Object getItem(int position) {
        Iterator<Politician> it = politicians.iterator();
        for (int i = 0; i < position; i++) {
            it.next();
        }
        return it.next();
    }

    @Override
    public long getItemId(int position) {
        Politician pol = (Politician) getItem(position);
        return pol.getCpf().hashCode();
    }

    /**
     * Removes an item from the adapter.
     */
    public void removeItem(Politician politician) { politicians.remove(politician); }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.politician_thumbnail_layout, null);
        }

        final Politician pol = (Politician) getItem(position);

        loadPhoto(pol, view);

        TextView polName = (TextView) view.findViewById(R.id.politician_name);
        polName.setText(pol.getPoliticianName());

        TextView partyView = (TextView) view.findViewById(R.id.party_name);
        partyView.setText(pol.getPartyName());

        configureButtons(view, pol);

        return view;
    }

    private void configureButtons(View view, final Politician pol) {
        final DeselectableRadioButton btnLike = (DeselectableRadioButton) view.findViewById(R.id.btn_like);
        btnLike.setChecked(approval.equals(Approval.APPROVED));

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLike.isChecked()) {
                    context.setPoliticianApproved(pol);
                }
                else {
                    context.setPoliticianNeutral(pol);
                }
            }
        });

        final DeselectableRadioButton btnDislike = (DeselectableRadioButton) view.findViewById(R.id.btn_dislike);
        btnDislike.setChecked(approval.equals(Approval.REPROVED));

        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnDislike.isChecked()) {
                    context.setPoliticianReproved(pol);
                }
                else {
                    context.setPoliticianNeutral(pol);
                }
            }
        });
    }

    private void loadPhoto(final Politician pol, View view) {
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
            photo.setImageBitmap(pol.getPhoto(context));
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
        for (Politician pol : politicians) {
            String sectionLetter = sections.get(sectionIndex);
            String polLetter = pol.getPoliticianNameFirstLetter();
            if (polLetter.compareTo(sectionLetter) >= 0) {
                return index;
            }

            index++;
        }

        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        List<Politician> politicianList = new ArrayList<>();
        politicianList.addAll(politicians);
        Politician pol = politicianList.get(position);
        String letter = pol.getPoliticianNameFirstLetter();
        return sections.indexOf(letter);
    }
}
