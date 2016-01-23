package com.tsd.memoriapolitica.db;

import android.content.Context;

import com.tsd.memoriapolitica.domain.CitizenNotebook;
import com.tsd.memoriapolitica.domain.Politician;
import com.tsd.memoriapolitica.domain.PoliticianClass;

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

/**
 * Created by PC on 17/07/2015.
 */
public class CitizenNotebookDao extends Dao {

    private static final String CPF_KEY = "cpf";

    private FedDepDao politicianDao;

    public CitizenNotebookDao(Context aContext, FedDepDao thePoliticianDao) {
        super(aContext);
        this.politicianDao = thePoliticianDao;
    }

    public CitizenNotebook getNotebook() {
        return readNotebook();
    }

    private CitizenNotebook readNotebook() {
        CitizenNotebook notebook = new CitizenNotebook();
        notebook.addNeutralPoliticians(PoliticianClass.FED_DEP, politicianDao.getAll());

        try {
            FileInputStream inputStream = getContext().openFileInput("notebook.json");
            InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
            char[] buffer = new char[500000];
            int size = streamReader.read(buffer);
            char[] reducedBuffer = Arrays.copyOf(buffer, size);
            String jsonFileContent = new String(reducedBuffer);

            JSONObject jNotebook = new JSONObject(jsonFileContent);
            JSONArray jApproved = jNotebook.getJSONArray("approved");
            JSONArray jReproved = jNotebook.getJSONArray("reproved");
            List<Politician> approvedList = createPoliticianFrom(jApproved);
            List<Politician> reprovedList = createPoliticianFrom(jReproved);

            notebook.addApprovedPoliticians(PoliticianClass.FED_DEP, approvedList);
            notebook.addReprovedPoliticians(PoliticianClass.FED_DEP, reprovedList);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return notebook;
    }

    private List<Politician> createPoliticianFrom(JSONArray jArray) throws JSONException, IOException {
        List<Politician> politicians = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++)
        {
            JSONObject jsonPol = jArray.getJSONObject(i);
            String cpf = jsonPol.getString(CPF_KEY);

            Politician pol = politicianDao.getByCpf(cpf);
            politicians.add(pol);
        }

        return politicians;
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

        Collection<Politician> approvedPols = notebook.getApprovedPoliticians(PoliticianClass.FED_DEP);
        JSONArray jApprovedArray = createJsonFrom(approvedPols);
        jNotebook.put("approved", jApprovedArray);

        Collection<Politician> reprovedPols = notebook.getReprovedPoliticians(PoliticianClass.FED_DEP);
        JSONArray jReprovedArray = createJsonFrom(reprovedPols);
        jNotebook.put("reproved", jReprovedArray);

        return jNotebook;
    }

    private JSONArray createJsonFrom(Collection<Politician> approvedPols) throws JSONException {
        JSONArray jArray = new JSONArray();
        for (Politician pol : approvedPols) {
            JSONObject jPol = createJsonFrom(pol);
            jArray.put(jPol);
        }

        return jArray;
    }

    private JSONObject createJsonFrom(Politician pol) throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put(CPF_KEY, pol.getCpf());
        return jObj;
    }
}
