package com.tekklabs.memoriapolitica.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tekklabs.memoriapolitica.R;
import com.tekklabs.memoriapolitica.domain.CitizenNotebook;

/**
 * Created by taciosd on 4/17/16.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new AsyncTask<Void, Void, CitizenNotebook>() {

            @Override
            protected CitizenNotebook doInBackground(Void... params) {
                Presenter presenter = new Presenter(SplashActivity.this);
                return presenter.getCurrentNotebook();
            }

            @Override
            protected void onPostExecute(CitizenNotebook notebook) {
                super.onPostExecute(notebook);

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("NOTEBOOK", notebook);
                startActivity(intent);

                finish();
            }
        }.execute();
    }
}
