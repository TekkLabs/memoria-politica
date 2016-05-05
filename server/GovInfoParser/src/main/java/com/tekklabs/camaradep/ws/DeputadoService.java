package com.tekklabs.camaradep.ws;

import com.tekklabs.camaradep.ws.model.Bancada;
import com.tekklabs.camaradep.ws.model.Bloco;
import com.tekklabs.camaradep.ws.model.Deputado;
import com.tekklabs.camaradep.ws.model.ListaDeputados;
import com.tekklabs.camaradep.ws.model.Partido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface do servico da camara dos deputados.
 *
 * Created by taciosd on 5/3/16.
 */
public interface DeputadoService {

    /**
     * Retorna os deputados em exercício na Câmara dos Deputados
     * @return
     */
    @GET("ObterDeputados")
    Call<ListaDeputados> obterDeputados();

    /**
     * Retorna detalhes dos deputados com histórico de participação em comissões, períodos de exercício, filiações partidárias e lideranças.
     * @return
     */
    @GET("ObterDetalhesDeputado")
    Call<List<Deputado>> obterDetalhesDeputado(@Query("ideCadastro") String ideCadastro, @Query("numLegislatura") int numLegislatura);

    /**
     * Retorna os deputados líderes e vice-líderes em exercício das bancadas dos partidos
     * @return
     */
    @GET("ObterLideresBancadas")
    Call<List<Bancada>> obterLideresBancadas();

    /**
     * Retorna os partidos com representação na Câmara dos Deputados
     * @return
     */
    @GET("ObterPartidosCD")
    Call<List<Partido>> obterPartidosCd();

    /**
     * Retorna os blocos parlamentares na Câmara dos Deputados.
     * @param idBloco ID do Bloco Parlamentar
     * @param numLegislatura Número da Legislatura. Campo Vazio, legislatura atual. Apenas legislatura 53 em diante.
     * @return
     */
    @GET("ObterPartidosBlocoCD")
    Call<List<Bloco>> obterPartidosBlocoCd(@Query("idBloco") String idBloco, @Query("numLegislatura") int numLegislatura);
}
