package com.tsd.memoriapolitica.db;

import android.content.Context;

import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClass;
import com.tsd.memoriapolitica.domain.PoliticianClassification;
import com.tsd.memoriapolitica.gui.notebook.Approval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by PC on 17/07/2015.
 */
public class CitizenNotebookDao extends Dao {

    private static final String APPROVED_KEY = "approved";
    private static final String REPROVED_KEY = "reproved";

    private PoliticianClassificationDao polClassificationDao;

    public CitizenNotebookDao(Context aContext,
                              PoliticianClassificationDao theClassificationDao) {
        super(aContext);
        this.polClassificationDao = theClassificationDao;
    }

    public CitizenNotebook getNotebook() {
        return readNotebook();
    }

    private CitizenNotebook readNotebook() {
        CitizenNotebook notebook = new CitizenNotebook();
        notebook.addNeutralPoliticians(PoliticianClass.FED_DEP, polClassificationDao.getAllNeutral());

        try {
            FileInputStream inputStream = getContext().openFileInput("notebook.json");
            InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
            char[] buffer = new char[500000];
            int size = streamReader.read(buffer);
            char[] reducedBuffer = Arrays.copyOf(buffer, size);
            String jsonFileContent = new String(reducedBuffer);

            JSONObject jNotebook = new JSONObject(jsonFileContent);
            fillNotebook(notebook, jNotebook);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return notebook;
    }

    private void fillNotebook(CitizenNotebook notebook, JSONObject jNotebook) throws JSONException {
        JSONArray approvedJArray = jNotebook.getJSONArray(APPROVED_KEY);
        List<PoliticianClassification> approvedList = polClassificationDao.getClassificationsFrom(approvedJArray, Approval.APPROVED);

        JSONArray reprovedJArray = jNotebook.getJSONArray(REPROVED_KEY);
        List<PoliticianClassification> reprovedList = polClassificationDao.getClassificationsFrom(reprovedJArray, Approval.REPROVED);

        notebook.addApprovedPoliticians(PoliticianClass.FED_DEP, approvedList);
        notebook.addReprovedPoliticians(PoliticianClass.FED_DEP, reprovedList);
    }

    public void saveNotebook(CitizenNotebook notebook) {
        OutputStreamWriter outputStreamWriter = null;

        try {
            JSONObject jNotebook = createJsonFrom(notebook);
            String jsonStr = jNotebook.toString(4);

            FileOutputStream outStream = getContext().openFileOutput("notebook.json", Context.MODE_PRIVATE);
            outputStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
            outputStreamWriter.write(jsonStr);
        }
        catch (JSONException | IOException  e) {
            e.printStackTrace();
        }
        finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject createJsonFrom(CitizenNotebook notebook) throws JSONException {
        JSONObject jNotebook = new JSONObject();

        Set<PoliticianClassification> approvedList = notebook.getApprovedPoliticians(PoliticianClass.FED_DEP);
        JSONArray jApprovedArray = polClassificationDao.createJsonArrayFrom(approvedList);
        jNotebook.put(APPROVED_KEY, jApprovedArray);

        Set<PoliticianClassification> reprovedList = notebook.getReprovedPoliticians(PoliticianClass.FED_DEP);
        JSONArray jReprovedArray = polClassificationDao.createJsonArrayFrom(reprovedList);
        jNotebook.put(REPROVED_KEY, jReprovedArray);

        return jNotebook;
    }
}
