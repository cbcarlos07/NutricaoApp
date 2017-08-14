package ham.org.br.nutricao.service;

import java.util.List;

import ham.org.br.nutricao.model.Agendamento;
import ham.org.br.nutricao.model.CrachaValida;
import ham.org.br.nutricao.model.Mensagem;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.TipoRefeicao;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by carlos.bruno on 31/07/2017.
 */

public interface ServiceAPI {
    public  final static String BASE_URL = "http://10.50.140.54/webservice/";

    @GET("nutricao/")
    Call<List<TipoPrato>> getlistTipoPratos(
         @Query("acao") String acao,
         @Query("tipo_refeicao") int tipo,
         @Query("data") String data);

    @GET("nutricao/")
    Call<List<TipoRefeicao>> getlistTipoRefeicao(@Query("acao") String acao);


    @GET("nutricao/")
    Call<Mensagem> getMensagem(@Query("acao") String acao,
                               @Query("tipo_refeicao") int tipo,
                               @Query("data") String data,
                               @Query("cracha") String cracha);

    @GET("nutricao/")
    Call<List<Agendamento>> getAgendamento(@Query("acao") String acao,
                                           @Query("cracha") String cracha);


    @POST("nutricao/")
    Call<RetornoMensagem> insertOperacao(@Query("acao") String acao,
                                         @Query("cracha") String cracha,
                                         @Query("cardapio") String cardapio,
                                         @Query("operacao") String operacao);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl( ServiceAPI.BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build();


    @GET("nutricao/")
    Call<CrachaValida> getRetornoCracha(@Query("acao") String acao, @Query("cracha") String cracha);

    @GET("nutricao/")
    Call<RetornoMensagem> getInserirUser(@Query("acao") String acao, @Query("cracha") String cracha);

    @GET("nutricao/")
    Call<RetornoMensagem> logarSistema(@Header("Authorization") String autHeader);


}
