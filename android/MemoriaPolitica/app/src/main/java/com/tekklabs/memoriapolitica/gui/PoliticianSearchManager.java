package com.tekklabs.memoriapolitica.gui;

import android.widget.SearchView;

import com.tekklabs.memoriapolitica.domain.CitizenNotebook;
import com.tekklabs.memoriapolitica.domain.PoliticianClass;
import com.tekklabs.memoriapolitica.domain.PoliticianClassification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by taciosd on 5/3/16.
 */
public class PoliticianSearchManager implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private final PoliticianCardAdapter adapter;
    private final CitizenNotebook notebook;


    public PoliticianSearchManager(CitizenNotebook notebook, PoliticianCardAdapter adapter) {
        this.notebook = notebook;
        this.adapter = adapter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<PoliticianClassification> filteredPoliticians = new ArrayList<>();
        String searchedText = newText.toUpperCase();
        String[] searchedTokens = searchedText.split(" ");

        Collection<PoliticianClassification> politicians = notebook.getAllPoliticians(PoliticianClass.FED_DEP);
        for (PoliticianClassification polClassification : politicians) {
            String name = polClassification.getPolitician().getPoliticianName().toUpperCase();

            boolean matched = true;
            for (String searchedToken : searchedTokens) {
                if (!name.contains(searchedToken)) {
                    matched = false;
                }
            }

            if (matched) {
                filteredPoliticians.add(polClassification);
            }
        }

        adapter.setPoliticiansToShow(filteredPoliticians);
        return false;
    }
}
