package ham.org.br.nutricao.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.model.RetornoMensagem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by carlos.bruno on 10/08/2017.
 */

public class SalvarOperacao extends AsyncTask<String, String, String>  {

    private ProgressDialog dialog;
    private Context context;
    private Button button;
    private String mensagem;
    private char retorno;

    public SalvarOperacao( Context c, Button btn ){

        this.context = c;
        this.button = btn;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog =  new ProgressDialog(context);
        dialog.setMessage("Enviando dados... aguarde");

        dialog.show();
    }

    @Override
    protected String doInBackground(final String ... params) {
        publishProgress("Enviando dados... aguarde");
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServiceAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        ServiceAPI serviceAPI = retrofit.create(ServiceAPI.class);

        final Call<RetornoMensagem> mensagemCall = serviceAPI.insertOperacao( "I", params[0],
                                        params[1],
                                        params[2] );
        mensagemCall.enqueue(new Callback<RetornoMensagem>() {
            @Override
            public void onResponse(Call<RetornoMensagem> call, Response<RetornoMensagem> response) {

                if( response.isSuccessful() ){

                    RetornoMensagem retornoMensagemService = response.body();
                    RetornoMensagem retornoMensagem = new RetornoMensagem();
                    retornoMensagem.setEmail( retornoMensagemService.getEmail() );
                    retornoMensagem.setSuccess( retornoMensagemService.getSuccess() );

                    switch (retornoMensagem.getSuccess()){
                        case 0:
                            //mensagemOperacao(  );
                            mensagem = "Ocorreu um erro na operação!\n Tente novamente mais tarde";


                            break;
                        case 1:
                            mensagem = "Operação efetuada com sucesso \nEnviamos um e-mail para "+retornoMensagem.getEmail();


                            break;
                        case 2:
                            mensagem = "Você já efetuação previamente" ;

                           // mensagemOperacao( "Você já efetuação previamente" );

                            break;
                    }
                    Log.i("Async", mensagem);
                    publishProgress("Requisição consluída");


                }
                dialog.dismiss();
                CardapioActivity cardapioActivity = new CardapioActivity();
                switch ( params[0].charAt(0) ){
                    case 'A':
                        cardapioActivity.setAcao('C');
                        button.setText( "Cancelar Refeição" );
                        break;
                    case 'C':
                        cardapioActivity.setAcao( 'A' );
                        button.setText( "Agendar " );
                }
                Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {

            }
        });
        return mensagem;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);
        dialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {

    }
}
