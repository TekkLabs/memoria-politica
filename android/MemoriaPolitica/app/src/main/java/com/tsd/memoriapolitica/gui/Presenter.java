package com.tsd.memoriapolitica.gui;

import android.content.Context;

import com.tsd.memoriapolitica.db.CitizenNotebookDao;
import com.tsd.memoriapolitica.db.DaoFactory;
import com.tsd.memoriapolitica.domain.CitizenNotebook;

import java.io.Serializable;

/**
 * Created by taciosd on 1/31/16.
 */
public class Presenter implements Serializable {

    CitizenNotebookDao notebookDao;
    CitizenNotebook notebook;

    public Presenter(Context theContext) {
        this.notebookDao = DaoFactory.getNotebookDao(theContext);
        this.notebook = notebookDao.getNotebook();
    }

    public CitizenNotebook getCurrentNotebook() {
        return notebook;
    }

    public void saveNotebook(CitizenNotebook notebook) {
        this.notebook = notebook;
        notebookDao.saveNotebook(notebook);
    }
}
