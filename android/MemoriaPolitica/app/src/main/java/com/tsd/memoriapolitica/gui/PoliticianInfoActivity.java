package com.tsd.memoriapolitica.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsd.memoriapolitica.R;
import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Constants;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClassification;
import com.tsd.memoriapolitica.gui.notebook.Approval;

import java.io.IOException;

/**
 * Created by PC on 05/07/2015.
 */
public class PoliticianInfoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText approvalReasonEdit;
    private PoliticianClassification polClassification;
    private Presenter presenter;


    public PoliticianInfoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politician_info);

        Intent intent = getIntent();
        polClassification = (PoliticianClassification) intent.getSerializableExtra(Constants.POLITICIAN_KEY);
        presenter = new Presenter(this);

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
            }
        });
    }

    private void buildGui() {
        Politician politician = polClassification.getPolitician();

        getSupportActionBar().setTitle(politician.getPoliticianName());

        ImageView photo = (ImageView) findViewById(R.id.politician_info_photo);
        try {
            photo.setImageBitmap(politician.getPhoto(this));
        }
        catch (IOException e) {
            photo.setImageResource(R.drawable.shadow_man);
            e.printStackTrace();
        }

        TextView polName = (TextView) findViewById(R.id.politician_info_name);
        polName.setText(politician.getPoliticianName());

        TextView polParty = (TextView) findViewById(R.id.politician_info_party);
        polParty.setText(politician.getPartyName());

        approvalReasonEdit = (EditText) findViewById(R.id.approval_reason);
        approvalReasonEdit.setText(polClassification.getReason());
        if (polClassification.getApproval().equals(Approval.NEUTRAL)) {
            approvalReasonEdit.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        polClassification.setApprovalReason(approvalReasonEdit.getText().toString());
        CitizenNotebook notebook = presenter.getCurrentNotebook();
        notebook.setPoliticianClassification(polClassification);
        presenter.saveNotebook(notebook);
        super.onStop();
    }

    public void onShowPoliticianPhoto(View view) {
        // 2do
    }
}
