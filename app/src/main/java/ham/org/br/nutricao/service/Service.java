package ham.org.br.nutricao.service;

import java.util.ArrayList;
import java.util.List;

import ham.org.br.nutricao.model.Prato;
import ham.org.br.nutricao.model.Pratos;
import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.TipoRefeicao;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by carlos.bruno on 31/07/2017.
 */

public interface Service {
    @GET("nutricao/")
    Call<List<TipoPrato>> getlistTipoPratos(
         @Query("acao") String acao,
         @Query("tipo_refeicao") int tipo,
         @Query("data") String data);

    @GET("nutricao/")
    Call<List<TipoRefeicao>> getlistTipoRefeicao(@Query("acao") String acao);


}
