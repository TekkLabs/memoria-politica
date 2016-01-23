package com.tsd.memoriapolitica.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsd.memoriapolitica.R;
import com.tsd.memoriapolitica.domain.Constants;
import com.tsd.memoriapolitica.domain.Politician;

import java.io.IOException;

/**
 * Created by PC on 05/07/2015.
 */
public class PoliticianInfoActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public PoliticianInfoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politician_info);

        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        configureToolbarHomeButton();

        buildGui();
    }

    private void configureToolbarHomeButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // /NavUtils.navigateUpFromSameTask(this);
            }
        });
    }

    private void buildGui() {
        Intent intent = getIntent();
        Politician politician = (Politician) intent.getSerializableExtra(Constants.POLITICIAN_KEY);
        getSupportActionBar().setTitle(politician.getPoliticianName());

        ImageView photo = (ImageView) findViewById(R.id.politician_info_photo);
        try {
            photo.setImageBitmap(politician.getPhoto(this));
        } catch (IOException e) {
            photo.setImageResource(R.drawable.shadow_man);
            e.printStackTrace();
        }

        TextView polName = (TextView) findViewById(R.id.politician_info_name);
        polName.setText(politician.getPoliticianName());

        TextView polParty = (TextView) findViewById(R.id.politician_info_party);
        polParty.setText(politician.getPartyName());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onShowPoliticianPhoto(View view) {
        // 2do
    }
}
