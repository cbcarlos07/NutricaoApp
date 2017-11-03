package ham.org.br.nutricao.service;

import java.util.List;

import ham.org.br.nutricao.model.Agendamento;
import ham.org.br.nutricao.model.CrachaValida;
import ham.org.br.nutricao.model.Mensagem;
import ham.org.br.nutricao.model.Message;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.TipoRefeicao;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by carlos.bruno on 31/07/2017.
 */

public interface ServiceAPI {
     String BASE_URL = "http://boleto.ham.org.br:8080/webservice/";
   //static String BASE_URL = "http://10.50.140.54/webservice/";
   String webservice = "nutricao/"; 

    @POST( webservice )
    @FormUrlEncoded //Se for usar o @POST tem que se user o @FormUrlEncoded
    Call<List<TipoPrato>> getlistTipoPratos(
         @Query("acao") String acao,
         @Query("tipo_refeicao") int tipo,
         @Query("data") String data);

    @POST( webservice )
    @FormUrlEncoded
    Call<List<TipoRefeicao>> getlistTipoRefeicao(@Field("acao") String acao);


    @POST( webservice )
    @FormUrlEncoded
    Call<Mensagem> getMensagem(@Field("acao") String acao,
                               @Field("tipo_refeicao") int tipo,
                               @Field("data") String data,
                               @Field("cracha") String cracha);

    @POST( webservice )
    @FormUrlEncoded
    Call<Message> getMessage( @Field("acao") String acao,
                              @Field("tipo_refeicao") int tipo,
                              @Field("data") String data,
                              @Field("cracha") String cracha);

    @POST( webservice )
    @FormUrlEncoded
    Call<List<Agendamento>> getAgendamento(@Field("acao") String acao,
                                           @Field("cracha") String cracha);


    @POST( webservice )
    @FormUrlEncoded
    Call<RetornoMensagem> insertOperacao(@Field("acao") String acao,
                                         @Field("cracha") String cracha,
                                         @Field("cardapio") String cardapio,
                                         @Field("operacao") String operacao);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl( ServiceAPI.BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build();


    @POST( webservice )
    @FormUrlEncoded
    Call<CrachaValida> getRetornoCracha(@Field("acao") String acao, @Field("cracha") String cracha);

    @POST( webservice )
    @FormUrlEncoded
    Call<RetornoMensagem> getInserirUser(@Field("acao") String acao, @Field("cracha") String cracha);

    @POST( webservice )

    Call<RetornoMensagem> logarSistema(@Header("Authorization") String autHeader);

    @POST( webservice )
    @FormUrlEncoded
    Call<RetornoMensagem> reenviarEmail(@Field("acao") String acao,
                                         @Field("email") String cracha);
    @POST( webservice )
    @FormUrlEncoded
    Call<RetornoMensagem> esqueceuSenha(@Field("acao") String acao,
                                        @Field("email") String cracha);


}
