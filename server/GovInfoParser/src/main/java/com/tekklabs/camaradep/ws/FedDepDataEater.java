package com.tekklabs.camaradep.ws;

import com.tekklabs.camaradep.ws.model.Deputado;
import com.tekklabs.camaradep.ws.model.ListaDeputados;
import com.tekklabs.model.Model2Json;
import com.tekklabs.model.entities.FedDep;
import com.tekklabs.util.JSONWriter;

import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by taciosd on 5/3/16.
 */
public class FedDepDataEater {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Número de parâmetros inválido.");
        }

        File outputFile = new File(args[0]);

        List<Deputado> deputados = getDeputiesData();
        List<FedDep> fedDeputies = convertToModel(deputados);
        Model2Json converter = new Model2Json();
        JSONArray jsonArray = converter.fedDepList2Json(fedDeputies);
        writeToFile(jsonArray, outputFile);
    }

    private static void writeToFile(JSONArray jsonArray, File outputFile) {
        JSONWriter writer = new JSONWriter();
        try {
            jsonArray.writeJSONString(writer);
            String strJson = writer.toString();

            PrintWriter printWriter = new PrintWriter(outputFile.getAbsolutePath(), "UTF-8");
            printWriter.print(strJson);
            printWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Deputado> getDeputiesData() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.camara.gov.br/SitCamaraWS/Deputados.asmx/")
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        DeputadoService service = retrofit.create(DeputadoService.class);
        Call<ListaDeputados> call = service.obterDeputados();
        return call.execute().body().deputados;
    }

    private static List<FedDep> convertToModel(List<Deputado> deputados) {
        List<FedDep> fedDeputies = new ArrayList<>();

        for (Deputado deputado : deputados) {
            FedDep fedDep = new FedDep();
            fedDep.politicianName = deputado.nomeParlamentar;
            fedDep.civilName = deputado.nome;
            fedDep.sex = deputado.sexo;
            fedDep.email = deputado.email;
            fedDep.office = deputado.gabinete;
            fedDep.annex = deputado.anexo;
            fedDep.phone = deputado.fone;
            fedDep.party = deputado.partido;
            fedDep.status = deputado.condicao;
            fedDeputies.add(fedDep);
        }

        return fedDeputies;
    }
}
