package com.tekklabs.memoriapolitica.gui;

/**
 * Created by PC on 09/07/2015.
 */
public interface Searchable {

    void onSearchRequested(String query);

    boolean onQueryTextSubmit(String query);

    boolean onQueryTextChange(String newText);
}
