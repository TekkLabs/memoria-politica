package com.tekklabs.tse.parser;

import com.tekklabs.model.entities.JsonAttributes;
import com.tekklabs.util.ConsultaCandColumns;

/**
 * Created by PC on 22/07/2015.
 */
public class ConsultaCandColumns2014 implements ConsultaCandColumns {

    @Override
    public int getColumn(String name) {
        if (JsonAttributes.CPF_CANDIDATO.equals(name)) return 13;
        else if (JsonAttributes.NOME_CIVIL.equals(name)) return 10;
        else if (JsonAttributes.NOME_POLITICO.equals(name)) return 14;
        else if (JsonAttributes.NUM_TITULO_ELEITORAL_CANDIDATO.equals(name)) return 27;
        else if (JsonAttributes.SIGLA_UF.equals(name)) return 5;
        else if (JsonAttributes.PARTIDO.equals(name)) return 18;
        else throw new IllegalArgumentException("Nome de atributo inválido.");
    }
}
