package ham.org.br.nutricao.activity;

import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.model.RetornoMensagem;

import ham.org.br.nutricao.service.ServiceAPI;
import ham.org.br.nutricao.util.GeradorDeSenha;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SenhaActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_pwd;
    private TextInputLayout input_layout_pwd;
    private Button btn_pwd;
    private TextView tv_pwd;
    private TextView tv_question_esqueceu;
    private TextView tv_infsenha;
    boolean teste;
    public static String crachaBundle;
    public static String nomeBundle;
    public static String emailBundle;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha);

        input_layout_pwd = ( TextInputLayout ) findViewById( R.id.input_layout_pwd );
        btn_pwd          = ( Button ) findViewById( R.id.btn_pwd );
        et_pwd           = ( EditText ) findViewById( R.id.et_pwd );
        tv_pwd           = ( TextView ) findViewById( R.id.tv_pwd_cracha );
        tv_infsenha      = ( TextView ) findViewById( R.id.tv_infsenha );
        tv_question_esqueceu  = ( TextView ) findViewById( R.id.tv_question_esqueceu );
        progressBar      = ( ProgressBar ) findViewById( R.id.progressBarSenha );
        progressBar.setVisibility( View.INVISIBLE );

        Bundle bundle = getIntent().getExtras();
        crachaBundle = bundle.getString( "cracha" );
        emailBundle  = bundle.getString( "email" );
        nomeBundle = bundle.getString( "nome" );

        tv_pwd.setText( nomeBundle );

        btn_pwd.setOnClickListener( this );
        tv_question_esqueceu.setOnClickListener( this );
        tv_infsenha.setOnClickListener( this );

        
    }

    private boolean validatePwd(  ){

        if( et_pwd.getText().toString().trim().isEmpty() ){
            input_layout_pwd.setError( getString( R.string.senhaEmpty ) );
            requestFocus( et_pwd );
            teste = false;
        } else {
            input_layout_pwd.setErrorEnabled( false );

            ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );


            String dados = crachaBundle+":"+et_pwd.getText().toString();



            String authHeader = "Basic "+ Base64.encodeToString( dados.getBytes(), Base64.NO_WRAP );
           // Log.i("Cryp", authHeader);

            Call<RetornoMensagem> pwdValidaCall = serviceAPI.logarSistema( authHeader );
            pwdValidaCall.enqueue(new Callback<RetornoMensagem>() {
                @Override
                public void onResponse(Call<RetornoMensagem> call, Response<RetornoMensagem> response) {
                    if( response.isSuccessful() ){

                        RetornoMensagem pwdValidaResponse = response.body();
                        //Log.i("Senha",""+pwdValidaResponse.getEmail());
                        if( pwdValidaResponse.getSuccess() == 1 ){



                             //  mensagemToast( nomeBundle+" possui cadastro" );

                                Preferences preferences = new Preferences( SenhaActivity.this );
                                preferences.salvarDados( nomeBundle, crachaBundle, emailBundle );



                                Intent intent = new Intent( getApplicationContext(), MainActivity.class );

                                startActivity( intent );
                                finish();


                        }
                        else{

                            teste = false;
                            input_layout_pwd.setError( getString( R.string.senhaNot ) );
                            requestFocus( et_pwd );
                            dialogAlert( "Atenção","Senha não confere" );

                        }

                    }
                }

                @Override
                public void onFailure(Call<RetornoMensagem> call, Throwable t) {
                    dialogAlert("Ops", "Ocorreu um problema" );
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

    private void esqueceuSenha() {
        String novaSenha = GeradorDeSenha.getRandomPass(8);
       // Log.i("Esqueceu Senha", novaSenha );
        ServiceAPI serviceAPI = ServiceAPI.retrofit.create(ServiceAPI.class);
        String dados = SenhaActivity.crachaBundle + " | " + novaSenha + " | " + SenhaActivity.emailBundle + " | " + SenhaActivity.nomeBundle;

        String base64 = Base64.encodeToString(dados.getBytes(), Base64.NO_WRAP);
       // Log.i("Esqueceu dados", base64 );
        progressBar.setVisibility(View.VISIBLE);
        Call<RetornoMensagem> retornoMensagemCall = serviceAPI.esqueceuSenha("B", base64);
        retornoMensagemCall.enqueue(new Callback<RetornoMensagem>() {
            @Override
            public void onResponse(Call<RetornoMensagem> call, Response<RetornoMensagem> response) {
                   // Log.i("Esqueceu Resp", response.toString());
                if (response.isSuccessful()) {
                    progressBar.setVisibility( View.GONE );
                    Intent intent = new Intent(SenhaActivity.this, EsqueceuActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {

                if( !isOnline() ){
                    dialogAlert( "Ops", "Verifique sua conexão" );
                }else{
                    dialogAlert( "Ops", "Não foi possível processar a requisição" );
                }




            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_pwd:
                validatePwd();
                break;
            case R.id.tv_question_esqueceu:
                esqueceuSenha();
                //Log.i("Esqueceu", "Esqueceu clicado");
                break;
            case R.id.tv_infsenha:
                voltarAoCracha();
                break;

        }


    }

    private void voltarAoCracha(){
        Intent intent = new Intent( SenhaActivity.this, CrachaActivity.class );
        startActivity( intent );
        finish();
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void dialogAlert( String titulo, String mensagem  ){
        //   AlertDialog.Builder alert = new AlertDialog.Builder( CrachaActivity.this  );
        AlertDialog.Builder alert = new AlertDialog.Builder( new ContextThemeWrapper( SenhaActivity.this , R.style.AlertDialogCustom )  );
        alert.setTitle( titulo );
        alert.setMessage( mensagem );
        alert.setNeutralButton( getString(R.string.lbl_ok), null);

        AlertDialog aviso = alert.create();
        aviso.show();
    }
}
