package ham.org.br.nutricao.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.model.RetornoMensagem;

import ham.org.br.nutricao.service.ServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SenhaActivity extends AppCompatActivity {
    private EditText et_pwd;
    private TextInputLayout input_layout_pwd;
    private Button btn_pwd;
    private TextView tv_pwd;
    boolean teste;
    public static String crachaBundle;
    public static String nomeBundle;
    public static String emailBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha);

        input_layout_pwd = (TextInputLayout) findViewById( R.id.input_layout_pwd );
        btn_pwd          = (Button) findViewById( R.id.btn_pwd );
        et_pwd           = (EditText) findViewById( R.id.et_pwd );
        tv_pwd           = (TextView) findViewById( R.id.tv_pwd_cracha );

        Bundle bundle = getIntent().getExtras();
        crachaBundle = bundle.getString( "cracha" );
        emailBundle  = bundle.getString( "email" );
        nomeBundle = bundle.getString( "nome" );

        tv_pwd.setText( crachaBundle );

        btn_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePwd();
            }
        });



        
        
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
                            mensagemToast( "Senha não confere" );

                        }

                    }
                }

                @Override
                public void onFailure(Call<RetornoMensagem> call, Throwable t) {
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


}
