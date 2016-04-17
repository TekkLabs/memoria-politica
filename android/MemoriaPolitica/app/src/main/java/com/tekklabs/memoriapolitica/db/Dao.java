package com.tekklabs.memoriapolitica.db;

import android.content.Context;

/**
 * Created by PC on 17/07/2015.
 */
public class Dao {

    private Context context;

    public Dao(Context aContext) {
        this.context = aContext;
    }

    public Context getContext() {
        return context;
    }
}
