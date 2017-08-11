package ham.org.br.nutricao.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.activity.MainActivity;
import ham.org.br.nutricao.model.RetornoMensagem;
import okhttp3.OkHttpClient;
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
    private String cracha;
    private int cardapio;
    private String acao;
    private Activity activity;

    public SalvarOperacao(Context c, Button btn, Activity act){

        this.context = c;
        this.button = btn;
        this.activity = act;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServiceAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        ServiceAPI serviceAPI = retrofit.create(ServiceAPI.class);

        cracha = params[0];
        cardapio = Integer.parseInt( params[1] ) ;
        acao = params[2];

        Log.i("Salvar cracha", params[0]);
        Log.i("Salvar Cardapio", params[1]);
        Log.i("Salvar acao", params[2]);
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

                switch ( params[2].charAt(0) ){
                    case 'A':
                        retorno = 'C';
                        button.setText( context.getString(R.string.btn_cancelar ) );
                        button.setBackgroundColor(   context.getResources().getColor( R.color.colorNormalCancelar )  );
                        break;
                    case 'C':
                        retorno = 'A' ;
                        button.setText( context.getString(R.string.btn_agendar ) );
                        button.setBackgroundColor(   context.getResources().getColor( R.color.colorNormalAgendar )  );
                }
                Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {
                //Log.i("onFailure TipoPrato", t.getMessage());
                dialog.dismiss();
                //Log.i("onFailure age", t.getMessage());
                //Toast.makeText( context, , Toast.LENGTH_LONG ).show();
                AlertDialog.Builder alerta = new AlertDialog.Builder( context );
                alerta.setTitle( "Ops" );
                alerta.setMessage( "Ocorreu um problema ao salvar operação\nTente novamente mais tarde\n"+t.getMessage() );
                //t.printStackTrace();
                alerta.setNeutralButton( "OK", null );
                AlertDialog dialog = alerta.create();
                Log.i("Retorno", t.toString());

                dialog.show();
            }
        });
        return mensagem;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);
        dialog.setMessage("Enviando dados... aguarde");
    }

    @Override
    protected void onPostExecute(String s) {

       activity.finish();
       context.startActivity( activity.getIntent() );
        CardapioActivity.acao = acao.charAt(0);



    }
}
