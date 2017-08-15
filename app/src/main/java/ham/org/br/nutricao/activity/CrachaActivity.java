package ham.org.br.nutricao.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.model.CrachaValida;
import ham.org.br.nutricao.service.ServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrachaActivity extends AppCompatActivity {
    private EditText et_cracha;
    private TextInputLayout input_layout_cracha;
    private Button btn_cracha;
    boolean teste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cracha);

        verificarUsuarioLogado();

        input_layout_cracha = (TextInputLayout) findViewById( R.id.input_layout_cracha );
        btn_cracha          = (Button) findViewById( R.id.btn_cracha );
        et_cracha           = (EditText) findViewById( R.id.et_cracha );


        btn_cracha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCracha();
            }
        });
    }

    private boolean validateCracha(  ){

        if( et_cracha.getText().toString().trim().isEmpty() ){
            input_layout_cracha.setError( getString( R.string.crachaEmpty ) );
            requestFocus( et_cracha );
            teste = false;
        } else {
            input_layout_cracha.setErrorEnabled( false );

            ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );
            Call<CrachaValida> crachaValidaCall = serviceAPI.getRetornoCracha( "C", et_cracha.getText().toString() );
            crachaValidaCall.enqueue(new Callback<CrachaValida>() {
                @Override
                public void onResponse(Call<CrachaValida> call, Response<CrachaValida> response) {
                    if( response.isSuccessful() ){

                        CrachaValida crachaValidaResponse = response.body();
                        if( crachaValidaResponse.getSuccess() == 1 ){

                            Log.i("Cracha Email", crachaValidaResponse.getEmail());
                            teste = true;

                            //mensagemToast( crachaValidaResponse.getNome()+"Possui cadastro" );
                            Intent intent;
                            switch ( crachaValidaResponse.getAtivo() ) {
                                case -1:

                                    intent = new Intent( getApplicationContext(), CriarSenhaActivity.class );
                                    intent.putExtra( "cracha", et_cracha.getText().toString() );
                                    intent.putExtra( "nome", crachaValidaResponse.getNome() );
                                    intent.putExtra( "email", crachaValidaResponse.getEmail() );
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
                                    dialogAlert( "você ainda não ativou sua senha\nVerifique seu e-mail" );
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
                    mensagemToast( "Ocorreu um problema ao conectar: \n"+t.getMessage() );
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

        AlertDialog.Builder dialog = new AlertDialog.Builder( CrachaActivity.this );
        dialog.setTitle( "E-mail" );
        dialog.setMessage( mensagem );
        dialog.setNeutralButton( "OK", null );
        AlertDialog aviso = dialog.create();
        aviso.show();


    }

    private void verificarUsuarioLogado(){

        Preferences preferences = new Preferences( CrachaActivity.this );
        if( preferences.getNome() != null ){
            SenhaActivity.nomeBundle = preferences.getNome();
            SenhaActivity.emailBundle = preferences.getEmail();
            SenhaActivity.crachaBundle = preferences.getCracha();
            Intent intent = new Intent( CrachaActivity.this, MainActivity.class );
            startActivity( intent );
        }


    }
}
