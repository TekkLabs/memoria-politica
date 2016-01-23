package com.tsd.memoriapolitica.db;

import android.content.Context;
import android.content.res.Resources;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by taciosd on 7/31/15.
 */
public class ResourceUtil {

    public static JsonReader getJsonReaderFromFile(Context context, String fileName) throws IOException, JSONException {
        Resources res = context.getResources();

        //InputStream in = new FileInputStream(fileName);
        InputStream in = res.openRawResource(
                res.getIdentifier(fileName, "raw", context.getPackageName()));
        return new JsonReader(new InputStreamReader(in, "UTF-8"));
    }

    public static JSONArray getJsonArrayFromFile(Context context, String fileName) throws IOException, JSONException {
        Resources res = context.getResources();
        byte[] buffer;
        InputStream ins = res.openRawResource(
                res.getIdentifier(fileName, "raw", context.getPackageName()));

        buffer = new byte[200000];
        int size = ins.read(buffer);
        byte[] bufferReduced = Arrays.copyOf(buffer, size);
        String jsonFileContent = new String(bufferReduced);

        return new JSONArray(jsonFileContent);
    }
}
