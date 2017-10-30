package ham.org.br.nutricao.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.model.CrachaValida;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.service.ServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrachaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_cracha;
    private TextInputLayout input_layout_cracha;
    private Button btn_cracha;
    private String nome;
    private String email;
    private boolean teste;
    private ProgressDialog progressDialog;
    private TextView tv_cracha_termo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cracha);

        verificarUsuarioLogado();

        input_layout_cracha = (TextInputLayout) findViewById( R.id.input_layout_cracha );
        btn_cracha          = (Button) findViewById( R.id.btn_cracha );
        et_cracha           = (EditText) findViewById( R.id.et_cracha );
       // progressBar         = ( ProgressBar ) findViewById( R.id.progressBar );
        tv_cracha_termo     = ( TextView ) findViewById( R.id.tv_cracha_termo );
     //   progressBar.setVisibility( View.GONE );


        btn_cracha.setOnClickListener( this );
        tv_cracha_termo.setOnClickListener( this );
    }

    private boolean validateCracha(  ){

        if( et_cracha.getText().toString().trim().isEmpty() ){
            input_layout_cracha.setError( getString( R.string.crachaEmpty ) );
            requestFocus( et_cracha );
            teste = false;
        } else {
            input_layout_cracha.setErrorEnabled( false );
            loadingMessage( "Buscando dados. Aguarde" );
            ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );
            Call<CrachaValida> crachaValidaCall = serviceAPI.getRetornoCracha( "C", et_cracha.getText().toString() );
            crachaValidaCall.enqueue(new Callback<CrachaValida>() {
                @Override
                public void onResponse(Call<CrachaValida> call, Response<CrachaValida> response) {
                    if( response.isSuccessful() ){
                        closeLoadingMessa();
                        CrachaValida crachaValidaResponse = response.body();
                        if( crachaValidaResponse.getSuccess() == 1 ){

                         //   Log.i("Cracha Email", crachaValidaResponse.getEmail());
                            teste = true;

                            //mensagemToast( crachaValidaResponse.getNome()+"Possui cadastro" );
                            Intent intent;
                            nome = crachaValidaResponse.getNome();
                            email = crachaValidaResponse.getEmail();
                            switch ( crachaValidaResponse.getAtivo() ) {
                                case -1:

                                    intent = new Intent( getApplicationContext(), CriarSenhaActivity.class );
                                    intent.putExtra( "cracha", et_cracha.getText().toString() );

                                    intent.putExtra( "nome", nome );
                                    intent.putExtra( "email", email );
                                    intent.putExtra( "acao", "C" );
                                    startActivity( intent );
                                    finish();
                                    break;
                                case 1:
                                    intent = new Intent( getApplicationContext(), SenhaActivity.class );
                                    intent.putExtra( "cracha", et_cracha.getText().toString() );

                                    intent.putExtra( "nome", crachaValidaResponse.getNome() );
                                    intent.putExtra( "email", crachaValidaResponse.getEmail() );
                                    startActivity( intent );
                                    finish();
                                    break;
                                case 0:
                                    dialogAlert( getApplicationContext().getString( R.string.reenviarEmail ) );
                                    break;

                            }


                        }
                        else{

                            teste = false;
                            input_layout_cracha.setError( getString( R.string.crachaNot ) );
                            requestFocus( et_cracha );
                            mensagemToast( "Crachá não encontrado" );

                        }

                    }
                }

                @Override
                public void onFailure(Call<CrachaValida> call, Throwable t) {
                    closeLoadingMessa();
                    if( !isOnline() ){
                        dialogAlertErro( "Ops","Ocorreu um problema ao conectar" );
                    }else{
                        dialogAlertErro( "Ops","Não foi possível recuperar informação");
                        Log.d("Erro", t.getMessage());
                    }
                   /* if( t.hashCode() == 156572836 ){
                        dialogAlertErro( "Ops","Ocorreu um problema ao conectar" );
                    }else{

                        dialogAlertErro( "Ops","Não foi possível recuperar informação");
                        Log.i( "Erro retrofit", ""+t );
                    }*/


                }
            });


        }

        return teste;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void mensagemToast( String mensagem ){

        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();

    }

    private void dialogAlert( String mensagem ){

        AlertDialog.Builder dialog = new AlertDialog.Builder( new ContextThemeWrapper(CrachaActivity.this , R.style.AlertDialogCustom ) );
        dialog.setTitle( "E-mail" );
        dialog.setMessage( mensagem );
        dialog.setPositiveButton(getString(R.string.lbl_sim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reenviarEmail();
            }
        });
        dialog.setNegativeButton( getString( R.string.lbl_nao ), null );
        AlertDialog aviso = dialog.create();
        aviso.show();


    }


    private void reenviarEmail(){
        ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );

        String envio = email + " | "+nome+" | "+et_cracha.getText().toString();

        String base64 = Base64.encodeToString( envio.getBytes(), Base64.NO_WRAP );
      //  Log.i("Envio", base64);

        loadingMessage( "Processando solicitação. Aguarde" );
        final Call<RetornoMensagem> retornoMensagemCall = serviceAPI.reenviarEmail( "E", base64 );
        retornoMensagemCall.enqueue(new Callback<RetornoMensagem>() {
            @Override
            public void onResponse(Call<RetornoMensagem> call, Response<RetornoMensagem> response) {


                    if( response.isSuccessful() ) {
                        closeLoadingMessa();
                        int retornoMensagemResponse = response.body().getSuccess();
                        if (retornoMensagemResponse == 1) {
                            dialogAlert("Parabéns!", "O e-mail de confirmação foi enviado para o seu e-mail");

                        }

                    }


            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {
                closeLoadingMessa();
                if( !isOnline() ){
                    dialogAlertErro( "Ops","Ocorreu um problema ao conectar\nPor favor verifique sua conexão" );
                }else{
                    dialogAlertErro( "Ops","Não foi possível recuperar informação");
                    Log.d("Erro:", t.getMessage());
                }

            }
        });

    }

    private void verificarUsuarioLogado(){

        Preferences preferences = new Preferences( CrachaActivity.this );
        if( preferences.getNome() != null ){
            SenhaActivity.nomeBundle = preferences.getNome();
            SenhaActivity.emailBundle = preferences.getEmail();
            SenhaActivity.crachaBundle = preferences.getCracha();
            Intent intent = new Intent( CrachaActivity.this, MainActivity.class );
            startActivity( intent );
            finish();
        }


    }


    private void dialogAlertErro( String titulo, String mensagem  ){
        //AlertDialog.Builder alert = new AlertDialog.Builder( CrachaActivity.this  );
        AlertDialog.Builder alert = new AlertDialog.Builder( new ContextThemeWrapper(CrachaActivity.this , R.style.AlertDialogCustom )  );
        alert.setTitle( titulo );
        alert.setMessage( mensagem );
        alert.setNeutralButton(getString(R.string.lbl_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit( 0 );
            }
        });

        AlertDialog aviso = alert.create();
        aviso.show();
    }

    private void dialogAlert( String titulo, String mensagem  ){
     //   AlertDialog.Builder alert = new AlertDialog.Builder( CrachaActivity.this  );
        AlertDialog.Builder alert = new AlertDialog.Builder( new ContextThemeWrapper(CrachaActivity.this , R.style.AlertDialogCustom )  );
        alert.setTitle( titulo );
        alert.setMessage( mensagem );
        alert.setNeutralButton(getString(R.string.lbl_ok), null);

        AlertDialog aviso = alert.create();
        aviso.show();
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ){
            case R.id.btn_cracha:
                validateCracha();
                break;
            case R.id.tv_cracha_termo:
                abrirTermo();
                break;

        }
    }

    private void abrirTermo( ){
        Intent intent = new Intent( CrachaActivity.this, TermoActivity.class );
        startActivity( intent );
    }

    private void loadingMessage(  String msg ){
        progressDialog = new ProgressDialog( CrachaActivity.this );
        progressDialog.setCancelable( false );
        progressDialog.setCanceledOnTouchOutside( false );
        progressDialog.setMessage( msg );
        progressDialog.show();
    }

    private void closeLoadingMessa(){
        progressDialog.dismiss();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
