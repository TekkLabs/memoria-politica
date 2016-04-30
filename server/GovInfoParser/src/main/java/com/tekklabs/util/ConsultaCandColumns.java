package com.tekklabs.util;

/**
 * Created by PC on 22/07/2015.
 */
public interface ConsultaCandColumns {

    /**
     * Retrieve a column index from a Json attribute name.
     *
     * @param name Json attribute name defined in JsonAttributes class.
     * @return column index in csv file.
     */
    public int getColumn(String name);
}
