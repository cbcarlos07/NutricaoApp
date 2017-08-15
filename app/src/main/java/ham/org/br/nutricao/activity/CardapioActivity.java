package ham.org.br.nutricao.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ham.org.br.nutricao.R;

import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.model.Mensagem;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.Prato;
import ham.org.br.nutricao.model.TipoRefeicao;
import ham.org.br.nutricao.service.CardapioRetrofit;
import ham.org.br.nutricao.service.ExpandableListAdapter;
import ham.org.br.nutricao.service.Permissao;
import ham.org.br.nutricao.service.SalvarOperacao;
import ham.org.br.nutricao.service.ServiceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardapioActivity extends AppCompatActivity {


    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET

    };

    private ExpandableListView expListView;
    private Toolbar toolbar;

    private Button btn_acao;
    public static char acao;
    private int cdCardapio;


    private  ServiceAPI serviceAPI;
    private int idTipoRef;
    private String data;
    public static Activity cardapioActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardapio_activity);
        cardapioActivity = this;
        Permissao.validaPermissoes(1, this, permissoesNecessarias );

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        expListView = ( ExpandableListView ) findViewById( R.id.lvExp );

        btn_acao    = ( Button ) findViewById( R.id.btn_acao );
        toolbar = (Toolbar) findViewById( R.id.toolbarCardapio );

        setSupportActionBar( toolbar );






        Bundle bundle = getIntent().getExtras();

        idTipoRef = bundle.getInt( "tipo" );
        data   = bundle.getString( "data" );
        String strTipo   = bundle.getString( "strTipo" );
        //toolbar.setTitle( "Cardápio - "+data+" - "+strTipo );
        setTitle( strTipo+" - "+data );



         serviceAPI = ServiceAPI.retrofit.create(ServiceAPI.class);


        getMensagem( );
        chamarTipoPratos(idTipoRef, data );





    //    chamarPratos( serviceAPI );




        btn_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserirOperacao( SenhaActivity.crachaBundle );
            }
        });


    }

    private void getMensagem (  ){
        Call<Mensagem> mensagemCall = serviceAPI.getMensagem( "M", idTipoRef, data, SenhaActivity.crachaBundle );
        mensagemCall.enqueue(new Callback<Mensagem>() {
            @Override
            public void onResponse(Call<Mensagem> call, Response<Mensagem> response) {
              //  Log.i("Mensagens body",response.toString());
                if( response.isSuccessful() ){

                    Mensagem mensagem = response.body();
                   // mensagem = new Mensagem();
                    setCdCardapio( mensagem.getCardapio() );



                    if( !mensagem.getMotivo().equals("") ){

                        abrirMensagem( "Atenção!", mensagem.getMotivo() );

                    }else{
                        btn_acao.setText( "Agendar" );
                    }

               //     Log.i("Mensagem Acao", mensagem.getAcao());
                    switch ( mensagem.getAcao() ) {
                        case "A":
                            btn_acao.setText( getString( R.string.btn_agendar ) );
                            btn_acao.setVisibility( View.VISIBLE );

                            btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalAgendar )  );
                            acao = 'A' ;
                            setAcao( 'A' );
                            break;
                        case "C":
                            btn_acao.setText( getString( R.string.btn_cancelar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalCancelar )  );
                            acao =  'C' ;
                            setAcao( 'C' );
                            break;
                        case "N":
                            btn_acao.setVisibility( View.INVISIBLE );
                            acao =  'N' ;
                            setAcao( 'N' );
                            break;
                        default:
                            btn_acao.setText( getString( R.string.btn_agendar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            break;
                    }

                }

            }

            @Override
            public void onFailure(Call<Mensagem> call, Throwable t) {
                //Log.i("onFailure Mensagem", t.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder( CardapioActivity.this );
                builder.setTitle("Ops");
                builder.setMessage("Ocorreu um erro:\n"+t.getMessage());

                builder.setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    private void getMensagemBotao (  ){
        Call<Mensagem> mensagemCall = serviceAPI.getMensagem( "M", idTipoRef, data, SenhaActivity.crachaBundle );
        mensagemCall.enqueue(new Callback<Mensagem>() {
            @Override
            public void onResponse(Call<Mensagem> call, Response<Mensagem> response) {
          //      Log.i("Mensagens body",response.toString());
                if( response.isSuccessful() ){

                    Mensagem mensagem = response.body();
                    // mensagem = new Mensagem();
                    setCdCardapio( mensagem.getCardapio() );



                    if( !mensagem.getMotivo().equals("") ){

                       // abrirMensagem( "Atenção!", mensagem.getMotivo() );

                    }else{
                        btn_acao.setText( "Agendar" );
                    }

                   // Log.i("Mensagem Acao", mensagem.getAcao());
                    switch ( mensagem.getAcao() ) {
                        case "A":
                            btn_acao.setText( getString( R.string.btn_agendar ) );
                            btn_acao.setVisibility( View.VISIBLE );

                            btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalAgendar )  );
                            acao = 'A' ;
                            setAcao( 'A' );
                            break;
                        case "C":
                            btn_acao.setText( getString( R.string.btn_cancelar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalCancelar )  );
                            acao =  'C' ;
                            setAcao( 'C' );
                            break;
                        case "N":
                            btn_acao.setVisibility( View.INVISIBLE );
                            acao =  'N' ;
                            setAcao( 'N' );
                            break;
                        default:
                            btn_acao.setText( getString( R.string.btn_agendar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            break;
                    }

                }

            }

            @Override
            public void onFailure(Call<Mensagem> call, Throwable t) {
                //Log.i("onFailure Mensagem", t.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder( CardapioActivity.this );
                builder.setTitle("Ops");
                builder.setMessage("Ocorreu um erro:\n"+t.getMessage());

                builder.setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    private void chamarTipoPratos( int idTipoRef, String data ){

        CardapioRetrofit cardapioRetrofit = new CardapioRetrofit( this, expListView );
        cardapioRetrofit.execute( String.valueOf( idTipoRef ), data );


    }




    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){


        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        for( int resultado : grantResults ){

            if( resultado == PackageManager.PERMISSION_DENIED ){

                alertaValidacaoPermissao();
                //ActivityCompat.requestPermissions( this, permissions, resultado );

            }

        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirMensagem( String titulo, String mensagem ){

        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle(titulo);
        alert.setMessage( mensagem );
        alert.setNeutralButton("OK", null);
        AlertDialog dialog = alert.create();
        dialog.show();

    }




    public char getAcao(){
        return this.acao;
    }

    public void setAcao( char action ){
        this.acao = action;
    }

    public int getCdCardapio(){
        return this.cdCardapio;
    }

    public void setCdCardapio( int cd_card ){
        this.cdCardapio = cd_card;
    }


    public void inserirOperacao(String cracha ){
        //SalvarOperacao salvarOperacao = new SalvarOperacao(CardapioActivity.this, btn_acao, CardapioActivity.this);

       // salvarOperacao.execute( cracha, String.valueOf( getCdCardapio() ), String.valueOf(acao) );


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServiceAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        ServiceAPI serviceAPI = retrofit.create(ServiceAPI.class);

        final ProgressDialog dialog = new ProgressDialog( CardapioActivity.this );
        dialog.setMessage( "Enviando dados... aguarde" );
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
     //   Log.i("Salvar Cracha", cracha);
     //   Log.i("Salvar Cardapio", String.valueOf(getCdCardapio()));
     //   Log.i("Salvar Acai", String.valueOf(getAcao() ));
        final Call<RetornoMensagem> mensagemCall = serviceAPI.insertOperacao( "I", cracha,
                                                                               String.valueOf( getCdCardapio() ) ,
                                                                                String.valueOf(getAcao()) );
        mensagemCall.enqueue(new Callback<RetornoMensagem>() {
            @Override
            public void onResponse(Call<RetornoMensagem> call, Response<RetornoMensagem> response) {
                String mensagem = "";
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
                   // Log.i("Async", mensagem);
                  //  publishProgress("Requisição consluída");


                }
                dialog.dismiss();

                switch ( getAcao() ){
                    case 'A':

                        setAcao( 'C' );
                        btn_acao.setText( getString(R.string.btn_cancelar ) );
                        btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalCancelar )  );
                        break;
                    case 'C':

                        setAcao( 'A' );
                        btn_acao.setText( getString(R.string.btn_agendar ) );
                        btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalAgendar )  );
                }
                Toast.makeText(CardapioActivity.this, mensagem, Toast.LENGTH_SHORT).show();

                getMensagemBotao();


            }

            @Override
            public void onFailure(Call<RetornoMensagem> call, Throwable t) {
                //Log.i("onFailure TipoPrato", t.getMessage());
                dialog.dismiss();
                //Log.i("onFailure age", t.getMessage());
                //Toast.makeText( context, , Toast.LENGTH_LONG ).show();
                dialogMensagem( "Ops", "Ocorreu um problema ao salvar operação\nTente novamente mais tarde\n"+t.getMessage() );

            }
        });

    }





    private void refresh(  ){
        Intent intent = getIntent();
        finish();
        startActivity( intent );
    }

    private void dialogMensagem(String titulo, String mensagem){
        AlertDialog.Builder alerta = new AlertDialog.Builder( CardapioActivity.this );
        alerta.setTitle( titulo );
        alerta.setMessage( mensagem );
        //t.printStackTrace();
        alerta.setNeutralButton( "OK", null );
        AlertDialog dialog = alerta.create();
        //     Log.i("Retorno", t.toString());

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_cardapio, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ){

            case R.id.action_perfil_card:
                abrirPerfil();
                break;

            case R.id.action_sair_card:
                sair();
                break;

        }
        return super.onOptionsItemSelected( item );
    }

    private void abrirPerfil(){

        Intent intent = new Intent( CardapioActivity.this, PerfilActivity.class );
        startActivity( intent );

    }

    private void sair(){
        Preferences preferences = new Preferences( CardapioActivity.this );
        preferences.salvarDados( null, null, null );

        Intent intent = new Intent( CardapioActivity.this, CrachaActivity.class );
        startActivity( intent );
        MainActivity.mainActivity.finish();
        finish();


    }


}