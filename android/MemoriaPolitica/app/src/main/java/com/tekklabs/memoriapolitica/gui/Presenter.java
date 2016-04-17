package com.tekklabs.memoriapolitica.gui;

import android.content.Context;

import com.tekklabs.memoriapolitica.db.CitizenNotebookDao;
import com.tekklabs.memoriapolitica.db.DaoFactory;
import com.tekklabs.memoriapolitica.domain.CitizenNotebook;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by taciosd on 1/31/16.
 */
public class Presenter implements Serializable {

    private final Context context;
    private CitizenNotebook notebook;

    public Presenter(Context theContext) {
        this.context = theContext;
    }

    public void setCurrentNotebook(CitizenNotebook currentNotebook) {
        this.notebook = currentNotebook;
    }

    public CitizenNotebook getCurrentNotebook() {
        if (notebook == null) {
            this.notebook = DaoFactory.getNotebookDao(context).getNotebook();
        }
        return notebook;
    }

    public void saveNotebook(CitizenNotebook notebook) {
        this.notebook = notebook;
        DaoFactory.getNotebookDao(context).saveNotebook(notebook);
    }
}
