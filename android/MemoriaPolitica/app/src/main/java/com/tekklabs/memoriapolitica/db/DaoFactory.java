package com.tekklabs.memoriapolitica.db;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by PC on 17/07/2015.
 */
public class DaoFactory {

    public static PoliticianClassificationDao getPoliticianClassificationDao(Context aContext) {
        return new PoliticianClassificationDao(aContext, getFederalDeputiesDao(aContext));
    }

    public static FedDepDao getFederalDeputiesDao(Context aContext) {
        return new FedDepDao(aContext, getCandidateDao(aContext),
                                       new PartyDao(aContext));
    }

    public static SenatorDao getSenatorsDao(Context aContext) {
        return new SenatorDao(aContext, getCandidateDao(aContext), getPartyDao(aContext));
    }

    @NonNull
    private static CandidateDao getCandidateDao(Context aContext) {
        return new CandidateDao(aContext, new PartyDao(aContext));
    }

    public static CitizenNotebookDao getNotebookDao(Context aContext) {
        return new CitizenNotebookDao(aContext, getPoliticianClassificationDao(aContext),
                                                getPartyDao(aContext));
    }

    public static PartyDao getPartyDao(Context context) {
        return new PartyDao(context);
    }
}
