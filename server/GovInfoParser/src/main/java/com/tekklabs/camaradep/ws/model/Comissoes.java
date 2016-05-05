package com.tekklabs.camaradep.ws.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by taciosd on 5/4/16.
 */
public class Comissoes {

    @Element(required = false)
    public String titular;

    @Element(required = false)
    public String suplente;
}