package com.tekklabs.camaradep.ws.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by taciosd on 5/3/16.
 */
@Root(name="deputado")
public class Deputado {

    // Identificador único do parlamentar
    @Element
    public int ideCadastro;

    @Element(required = false)
    public int codOrcamento;

    // Retorna se o deputado e Titular ou suplente
    @Element
    public String condicao;

    @Element
    public int matricula;

    @Element
    public int idParlamentar;

    // Nome Civil
    @Element
    public String nome;

    // Nome parlamentar
    @Element
    public String nomeParlamentar;

    // URL da foto do parlamentar.
    @Element
    public String urlFoto;

    // Sexo
    @Element
    public String sexo;

    // UF da representação parlamentar
    @Element
    public String uf;

    // Partido atual do parlamentar.
    @Element
    public String partido;

    // Número do gabinete do parlamentar.
    @Element
    public String gabinete;

    // Anexo (prédio) onde o gabinete está localizado
    @Element
    public String anexo;

    // Número do telefone do gabinete.
    @Element
    public String fone;

    // Email institucional do parlamentar.
    @Element
    public String email;

    // Comissões da Câmara dos Deputados que o parlamentar é membro
    @Element
    public Comissoes comissoes;
}
