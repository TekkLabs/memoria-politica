package com.tsd.memoriapolitica.gui.notebook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.tsd.memoriapolitica.R;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClass;
import com.tsd.memoriapolitica.gui.MainActivity;
import com.tsd.memoriapolitica.gui.PoliticianThumbnailAdapter;
import com.tsd.memoriapolitica.gui.Searchable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPoliticianFragmentInteractionListener}
 * interface.
 */
public abstract class PoliticianFragment extends Fragment implements AbsListView.OnItemClickListener, Searchable {

    /**
     * The fragment's ListView/GridView.
     */
    AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    PoliticianThumbnailAdapter mAdapter;

    /**
     * Class of politician to be shown.
     */
    private PoliticianClass politicianClass = PoliticianClass.FED_DEP;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PoliticianFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_politician, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.politician_grid);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((MainActivity)activity).setSearchableFragment(this);
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity)getActivity()).setSearchableFragment(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onSearchRequested(String query) {
        doSearch(query);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
        return false;
    }

    public void doSearch(String query) {
        String[] terms = query.split(" ");

        Iterator<Politician> it = mAdapter.getPoliticians().iterator();
        while (it.hasNext()) {
            Politician pol = it.next();
            for (String term : terms) {
                if (!pol.matchesQueryTerm(term)) {
                    it.remove();
                    mAdapter.removeItem(pol);
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void setPoliticianClass(PoliticianClass politicianClass) {
        this.politicianClass = politicianClass;
    }

    public PoliticianClass getPoliticianClass() {
        return politicianClass;
    }

    public abstract void refresh();


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPoliticianFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onPoliticianFragmentInteraction(Politician politician);
    }
}
