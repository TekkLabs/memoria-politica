package com.tsd.memoriapolitica.db;

import android.content.Context;

/**
 * Created by PC on 17/07/2015.
 */
public class DaoFactory {

    public static FedDepDao getFederalDeputiesDao(Context aContext) {
        return new FedDepDao(aContext, new CandidateDao(aContext));
    }

    public static SenatorDao getSenatorsDao(Context aContext) {
        return new SenatorDao(aContext, new CandidateDao(aContext));
    }

    public static CitizenNotebookDao getNotebookDao(Context aContext) {
        return new CitizenNotebookDao(aContext, getFederalDeputiesDao(aContext));
    }
}
