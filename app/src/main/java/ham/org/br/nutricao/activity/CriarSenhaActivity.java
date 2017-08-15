package ham.org.br.nutricao.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.CrachaValida;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.model.Usuario;
import ham.org.br.nutricao.service.ServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CriarSenhaActivity extends AppCompatActivity {
    private TextInputLayout inputLayoutSenha;
    private TextInputLayout inputLayoutRepetirSenha;
    private Button btn_criar_senha;
    private EditText senha;
    private EditText reSenha;
    private TextView tv_cracha;
    private String nomeBundle;
    private String emailBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_senha);

        inputLayoutSenha = (TextInputLayout) findViewById( R.id.input_layout_senha );
        inputLayoutRepetirSenha = (TextInputLayout) findViewById( R.id.input_layout_repita_senha );
        btn_criar_senha  = (Button) findViewById( R.id.btn_criar_senha );
        senha = ( EditText ) findViewById( R.id.et_senha );
        reSenha = ( EditText ) findViewById( R.id.et_repita_senha );
        tv_cracha = (TextView) findViewById( R.id.tv_cracha );
    //    reSenha.addTextChangedListener( new MyTextWatcher( reSenha )  );
      //  senha.addTextChangedListener( new MyTextWatcher( senha )  );

        Bundle bundle = getIntent().getExtras();

        String cracha = bundle.getString( "cracha" );
        nomeBundle = bundle.getString( "nome" );
        emailBundle = bundle.getString( "email" );

        tv_cracha.setText( cracha );

        btn_criar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaidarSenha();
            }
        });

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void senhasIguais(){
        if( senha.getText().toString().equals( reSenha.getText().toString() ) ){
            btn_criar_senha.setEnabled(true);
        }else{
            inputLayoutRepetirSenha.setError( getString( R.string.senhaNaoIguais ) );
            inputLayoutSenha.setError( getString( R.string.senhaNaoIguais ) );
            btn_criar_senha.setEnabled( false );
        }
    }

    private void vaidarSenha(  ){

        if( senha.getText().toString().trim().isEmpty()){
            inputLayoutSenha.setError( getString( R.string.senhaEmpty ) );
            requestFocus( senha );
        }else if( reSenha.getText().toString().trim().isEmpty() ){
            inputLayoutRepetirSenha.setError( getString( R.string.senhaEmpty ) );
            requestFocus( reSenha );
        }
        else if( senha.getText().length() < 8 ){
            inputLayoutSenha.setError( getString( R.string.senhaMenorQue8 ) );
            requestFocus( senha );

        }
        else{

            if( senha.getText().toString().equals( reSenha.getText().toString() ) ){

                Usuario usuario = new Usuario();

                usuario.setCodigo( Integer.parseInt( tv_cracha.getText().toString() ) );
                usuario.setEmail( emailBundle );
                usuario.setNome( nomeBundle );
                usuario.setSenha( senha.getText().toString() );

                ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );

                String dados = usuario.getCodigo() + " | "+usuario.getSenha()+" | "+usuario.getEmail()+" | "+usuario.getNome();

                String base64Crypt = Base64.encodeToString( dados.getBytes(), Base64.NO_WRAP );

               // Log.i("Campo: ", base64Crypt);
               // Log.i("Campo Email", usuario.getEmail());
                Call<RetornoMensagem> retornoMensagemCall = serviceAPI.getInserirUser( "D", base64Crypt );
                final ProgressDialog progressDialog = new ProgressDialog( CriarSenhaActivity.this );
                progressDialog.setMessage( "Salvando dados..." );
                progressDialog.setCancelable( false );
                progressDialog.setCanceledOnTouchOutside( false );
                progressDialog.show();
                retornoMensagemCall.enqueue(new Callback<RetornoMensagem>() {
                    @Override
                    public void onResponse(Call<RetornoMensagem> call, Response<RetornoMensagem> response) {
                        RetornoMensagem retornoMensagem = response.body();
                        if( retornoMensagem.getSuccess() == 1 ){
                            progressDialog.dismiss();
                            dialogRetorno( "Parabéns", "Sua senha foi criada com sucesso!\nEnviamos um e-mail de confirmação." );

                        }
                    }

                    @Override
                    public void onFailure(Call<RetornoMensagem> call, Throwable t) {

                        dialogRetorno( "Erro", "Erro ao tentar salvar\n"+t.getMessage() );

                    }
                });





            }else{

                inputLayoutRepetirSenha.setError( getString( R.string.senhaNaoIguais ) );
                inputLayoutSenha.setError( getString( R.string.senhaNaoIguais ) );
            }


        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_senha:
                    senhasIguais();
                    break;
                case R.id.et_repita_senha:
                    senhasIguais();
                    break;
            }
        }
    }

    private void dialogRetorno( String titulo, String msg ){

        AlertDialog.Builder dialog = new AlertDialog.Builder( CriarSenhaActivity.this );
        dialog.setTitle( titulo );
        dialog.setMessage( msg );
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //abrir activity
                inputLayoutRepetirSenha.setErrorEnabled( false );
                inputLayoutSenha.setErrorEnabled( false );
                Intent intent = new Intent( CriarSenhaActivity.this, CrachaActivity.class );
                startActivity( intent );
                finish();
            }
        });
        AlertDialog aviso = dialog.create();
        aviso.show();

    }

}
