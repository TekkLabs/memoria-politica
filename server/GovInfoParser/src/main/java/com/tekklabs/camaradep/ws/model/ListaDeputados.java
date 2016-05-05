package com.tekklabs.camaradep.ws.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by taciosd on 5/4/16.
 */
@Root(name="deputados")
public class ListaDeputados {

    @ElementList(inline = true)
    public ArrayList<Deputado> deputados;
}
