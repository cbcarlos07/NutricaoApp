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
import android.widget.ProgressBar;
import android.widget.TextView;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.CrachaValida;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.model.Usuario;
import ham.org.br.nutricao.service.ServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CriarSenhaActivity extends AppCompatActivity implements View.OnClickListener{
    private TextInputLayout inputLayoutSenha;
    private TextInputLayout inputLayoutRepetirSenha;
    private Button btn_criar_senha;
    private EditText senha;
    private EditText reSenha;
    private TextView tv_cracha;
    private TextView tv_msg_senha;
    private TextView tv_cria_senha_load;
    private TextView tv_cria_senha;
    private String nomeBundle;
    private String emailBundle;
    private char acao;
    private ProgressBar progressBar;
    private String cracha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_senha);

        inputLayoutSenha        = ( TextInputLayout ) findViewById( R.id.input_layout_senha );
        inputLayoutRepetirSenha = ( TextInputLayout ) findViewById( R.id.input_layout_repita_senha );
        btn_criar_senha         = ( Button ) findViewById( R.id.btn_criar_senha );
        senha                   = ( EditText ) findViewById( R.id.et_senha );
        reSenha                 = ( EditText ) findViewById( R.id.et_repita_senha );
        tv_cracha               = ( TextView ) findViewById( R.id.tv_cracha );
        tv_cria_senha_load      = ( TextView ) findViewById( R.id.tv_cria_senha_load );
        tv_cria_senha           = ( TextView ) findViewById( R.id.tv_criasenha );
        tv_msg_senha            = ( TextView ) findViewById( R.id.tv_msg_senha );
        progressBar             = ( ProgressBar ) findViewById( R.id.progressBarCriaSenha );
        progressBar.setVisibility( View.INVISIBLE );

        tv_cria_senha_load.setVisibility( View.INVISIBLE );
    //    reSenha.addTextChangedListener( new MyTextWatcher( reSenha )  );
      //  senha.addTextChangedListener( new MyTextWatcher( senha )  );

        Bundle bundle = getIntent().getExtras();

        cracha = bundle.getString( "cracha" );
        nomeBundle    = bundle.getString( "nome" );
        emailBundle   = bundle.getString( "email" );
        acao          = bundle.getString( "acao" ).charAt(0);
        tv_cracha.setText( nomeBundle );
        if( acao == 'C' ){
            tv_msg_senha.setText( getString( R.string.criarNovaSenha ) );
        }else{

            tv_msg_senha.setText( getString( R.string.alterarSenha ) );
        }

        tv_cracha.setText( nomeBundle );

        btn_criar_senha.setOnClickListener( this );

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

                    progressBar.setVisibility( View.VISIBLE );
                    tv_cria_senha_load.setVisibility( View.VISIBLE );
                    tv_cracha.setVisibility( View.INVISIBLE );
                    senha.setVisibility( View.INVISIBLE );
                    reSenha.setVisibility( View.INVISIBLE );
                    btn_criar_senha.setVisibility( View.INVISIBLE );

                    if( acao == 'C' ){
                        criarSenha();
                    }else {

                        alterarSenha();

                    }

                progressBar.setVisibility( View.INVISIBLE );
                tv_cria_senha_load.setVisibility( View.INVISIBLE );
                tv_cracha.setVisibility( View.VISIBLE );
                senha.setVisibility( View.VISIBLE );
                reSenha.setVisibility( View.VISIBLE );
                btn_criar_senha.setVisibility( View.VISIBLE );




            }else{

                inputLayoutRepetirSenha.setError( getString( R.string.senhaNaoIguais ) );
                inputLayoutSenha.setError( getString( R.string.senhaNaoIguais ) );
            }


        }

    }

    private void criarSenha(){
        Usuario usuario = new Usuario();
       // inputLayoutSenha.setVisibility( View.INVISIBLE );
        //inputLayoutRepetirSenha.setVisibility( View.INVISIBLE );

        usuario.setCodigo( Integer.parseInt( cracha ) );
        usuario.setEmail( emailBundle );
        usuario.setNome( nomeBundle );
        usuario.setSenha( senha.getText().toString() );

        ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );

        String dados = usuario.getCodigo() + " | "+usuario.getSenha()+" | "+usuario.getEmail()+" | "+usuario.getNome();

        String base64Crypt = Base64.encodeToString( dados.getBytes(), Base64.NO_WRAP );

        // Log.i("Campo: ", base64Crypt);
        // Log.i("Campo Email", usuario.getEmail());
      //  progressBar.setVisibility( View.VISIBLE );
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
                    //progressBar.setVisibility( View.GONE );
                  //  inputLayoutSenha.setVisibility( View.VISIBLE );
                  //  inputLayoutRepetirSenha.setVisibility( View.VISIBLE );
                    dialogRetorno( "Parabéns", "Sua senha foi criada com sucesso!\nEnviamos um e-mail de confirmação." );

                }
            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {
                progressDialog.dismiss();
                dialogRetorno( "Erro", "Erro ao tentar salvar\n"+t.getMessage() );

            }
        });
    }

    private void alterarSenha(){
      //  progressBar.setVisibility( View.VISIBLE );
        Usuario usuario = new Usuario();
      //  inputLayoutSenha.setVisibility( View.INVISIBLE );
      //  inputLayoutRepetirSenha.setVisibility( View.INVISIBLE );

        usuario.setCodigo( Integer.parseInt( cracha ) );
        usuario.setEmail( emailBundle );
        usuario.setNome( nomeBundle );
        usuario.setSenha( senha.getText().toString() );

        ServiceAPI serviceAPI = ServiceAPI.retrofit.create( ServiceAPI.class );

        String dados = usuario.getCodigo() + " | "+usuario.getSenha()+" | "+usuario.getEmail()+" | "+usuario.getNome();

        String base64Crypt = Base64.encodeToString( dados.getBytes(), Base64.NO_WRAP );

//         Log.i("Campo: ", base64Crypt);
        // Log.i("Campo Email", usuario.getEmail());
        Call<RetornoMensagem> retornoMensagemCall = serviceAPI.getInserirUser( "S", base64Crypt );
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
                  //  progressBar.setVisibility( View.GONE );
                  //  inputLayoutSenha.setVisibility( View.VISIBLE );
                   // inputLayoutRepetirSenha.setVisibility( View.VISIBLE );
                    dialogRetorno( "Parabéns", "Sua senha foi criada com sucesso!\nEnviamos um e-mail de confirmação." );

                }
            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {
                progressDialog.dismiss();
                dialogRetorno( "Erro", "Erro ao tentar salvar\n"+t.getMessage() );

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ){

            case R.id.btn_criar_senha:

                vaidarSenha();
                break;
            case R.id.tv_criasenha:
                voltarAoCracha();
                break;

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

    private void voltarAoCracha(){
        Intent intent = new Intent( CriarSenhaActivity.this, CrachaActivity.class );
        startActivity( intent );
        finish();
    }

}
