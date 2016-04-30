package com.tekklabs.memoriapolitica.gui;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tekklabs.memoriapolitica.R;
import com.tekklabs.memoriapolitica.domain.CitizenNotebook;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /**
     * Toolbar replacing the default ActionBar.
     */
    private Toolbar mToolbarMain;

    /**
     * Toolbar with a search view component.
     */
    private Toolbar mToolbarSearch;

    /**
     * Layout of the drawer.
     */
    private DrawerLayout mDrawer;

    /**
     * Navigation View of the drawer.
     */
    private NavigationView nvDrawer;

    /**
     * Handler for drawer events and action bars's home button clicks.
     */
    ActionBarDrawerToggle drawerToggle;

    /**
     * Adapter da view de políticos.
     */
    private PoliticianCardAdapter mPoliticiansCardAdapter;

    private Searchable mSearchableFragment;
    private Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        CitizenNotebook notebook = (CitizenNotebook) getIntent().getSerializableExtra("NOTEBOOK");
        presenter.setCurrentNotebook(notebook);

        setContentView(R.layout.activity_main);
        configureToolbars();
        configureDrawer();
        configurePoliticiansView();

        // Select federal deputies menu item.
        MenuItem menuItem = nvDrawer.getMenu().findItem(R.id.action_federal_deputies);
        selectDrawerItem(menuItem);
    }

    private void configurePoliticiansView() {
        RecyclerView mPoliticiansView = (RecyclerView) findViewById(R.id.politician_grid);
        mPoliticiansCardAdapter = new PoliticianCardAdapter(presenter.getCurrentNotebook());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mPoliticiansView.setLayoutManager(mLayoutManager);
        mPoliticiansView.setItemAnimator(new DefaultItemAnimator());
        mPoliticiansView.setAdapter(mPoliticiansCardAdapter);
    }

    private void configureToolbars() {
        // Set a Toolbar to replace the ActionBar.
        mToolbarMain = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbarMain);

        //mToolbarSearch = (Toolbar) findViewById(R.id.search_toolbar);
    }

    private void configureDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbarMain, 0,  0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            if (mSearchableFragment != null) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                mSearchableFragment.onSearchRequested(query);
            }
        }

        super.onNewIntent(intent);
    }

    public void setSearchableFragment(Searchable searchableFragment) {
        this.mSearchableFragment = searchableFragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return mSearchableFragment.onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return mSearchableFragment.onQueryTextChange(newText);
    }

    public CitizenNotebook getNotebook() {
        return presenter.getCurrentNotebook();
    }

    /**
     * Chamado quando o usuário clica em um item do menu Ordenar.
     * @param menuItem
     */
    public void onSortMenuItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.action_sort_by_state:
                mPoliticiansCardAdapter.sortItems(SortMode.BY_STATE);
                break;
            case R.id.action_sort_by_party:
                mPoliticiansCardAdapter.sortItems(SortMode.BY_PARTY);
                break;
            case R.id.action_sort_by_name:
                mPoliticiansCardAdapter.sortItems(SortMode.BY_NAME);
                break;
            case R.id.action_sort_by_approved_first:
                mPoliticiansCardAdapter.sortItems(SortMode.BY_APPROVED_FIRST);
                break;
            case R.id.action_sort_by_neutral_first:
                mPoliticiansCardAdapter.sortItems(SortMode.BY_NEUTRAL_FIRST);
                break;
            case R.id.action_sort_by_reproved_first:
                mPoliticiansCardAdapter.sortItems(SortMode.BY_REPROVED_FIRST);
                break;
            default:
                throw new IllegalArgumentException("Item de menu de ordenacão inválido.");
        }
    }
}
