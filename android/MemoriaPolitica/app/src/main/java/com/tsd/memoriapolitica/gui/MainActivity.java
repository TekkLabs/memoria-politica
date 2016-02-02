package com.tsd.memoriapolitica.gui;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tsd.memoriapolitica.R;
import com.tsd.memoriapolitica.db.DaoFactory;
import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClass;
import com.tsd.memoriapolitica.domain.PoliticianClassification;
import com.tsd.memoriapolitica.gui.notebook.PoliticianFragment;
import com.tsd.memoriapolitica.gui.notebook.PoliticianListPagerAdapter;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
                                                        ApprovalDescDialogFragment.NotifyDialogFragmentClicked {

    /**
     * Toolbar replacing the default ActionBar.
     */
    private Toolbar mToolbar;

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

    private Searchable mSearchableFragment;
    private ViewPager pager;
    private PoliticianListPagerAdapter mPagerAdapter;

    private PoliticianClass currentPolClass;
    private Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        presenter = new Presenter(this);
        //notebook = presenter.getCurrentNotebook();

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);

        pager = (ViewPager) findViewById(R.id.mainViewPager);
        mPagerAdapter = new PoliticianListPagerAdapter(getSupportFragmentManager());
        currentPolClass = PoliticianClass.FED_DEP;
        mPagerAdapter.setPoliticianClass(currentPolClass);
        pager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        tabLayout.setupWithViewPager(pager);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        // Select all politicians menu item.
        MenuItem menuItem = nvDrawer.getMenu().findItem(R.id.action_federal_deputies);
        selectDrawerItem(menuItem);
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
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, 0,  0);
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
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch(menuItem.getItemId()) {
            case R.id.action_federal_deputies:
                mPagerAdapter.setPoliticianClass(PoliticianClass.FED_DEP);
                break;
            case R.id.action_senators:
                mPagerAdapter.setPoliticianClass(PoliticianClass.SENATORS);
                break;
            case R.id.action_settings:
                fragment = SettingsFragment.newInstance("", "");
                break;
            default:
                throw new IllegalArgumentException("Item de menu do drawer inválido.");
        }

        pager.setCurrentItem(PoliticianListPagerAdapter.NEUTRAL_ITEM);

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_drawer_activity_actions, menu);
/*
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        searchView.setSubmitButtonEnabled(true);
*/
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

    public Presenter getPresenter() {
        return presenter;
    }

    public void fragmentChanged() {
        mPagerAdapter.notifyFragments();
    }

    @Override
    public void onDialogPositiveClick(PoliticianClassification polClassification) {
        mPagerAdapter.onDialogPositiveClick(polClassification);
    }
}
