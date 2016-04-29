package com.tekklabs.senadores;

import com.tekklabs.model.entities.JsonAttributes;
import com.tekklabs.util.ConsultaCandColumns;

/**
 * Created by taciosd on 8/2/15.
 */
public class SenatorsCsvColumns implements ConsultaCandColumns {

    @Override
    public int getColumn(String name) {
        if (JsonAttributes.NOME_POLITICO.equals(name)) return 0;
        else if (JsonAttributes.PARTIDO.equals(name)) return 1;
        else if (JsonAttributes.SIGLA_UF.equals(name)) return 2;
        else if (JsonAttributes.PHONE.equals(name)) return 4;
        else if (JsonAttributes.FAX.equals(name)) return 5;
        else throw new IllegalArgumentException("Nome de atributo inválido.");
    }
}
