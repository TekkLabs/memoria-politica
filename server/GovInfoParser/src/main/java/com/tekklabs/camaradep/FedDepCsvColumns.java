package com.tekklabs.camaradep;

import com.tekklabs.model.entities.JsonAttributes;
import com.tekklabs.util.ConsultaCandColumns;

/**
 * Created by PC on 24/07/2015.
 */
public class FedDepCsvColumns implements ConsultaCandColumns {

    @Override
    public int getColumn(String name) {
        if (JsonAttributes.NOME_CIVIL.equals(name)) return 17;
        else if (JsonAttributes.NOME_POLITICO.equals(name)) return 0;
        else if (JsonAttributes.SIGLA_UF.equals(name)) return 2;
        else if (JsonAttributes.PARTIDO.equals(name)) return 1;
        else if (JsonAttributes.STATUS.equals(name)) return 3;
        else if (JsonAttributes.EMAIL.equals(name)) return 13;
        else throw new IllegalArgumentException("Nome de atributo inválido.");
    }
}
