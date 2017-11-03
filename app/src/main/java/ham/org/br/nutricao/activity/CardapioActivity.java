package ham.org.br.nutricao.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ham.org.br.nutricao.R;

import ham.org.br.nutricao.database.Database;
import ham.org.br.nutricao.dominio.RepositorioCardapio;
import ham.org.br.nutricao.dominio.RepositorioPrato;
import ham.org.br.nutricao.dominio.RepositorioTipoPrato;
import ham.org.br.nutricao.dominio.RepositorioTipoRefeicao;
import ham.org.br.nutricao.fragment.AgendamentosFragment;
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.model.Agendamento;
import ham.org.br.nutricao.model.Cardapio;
import ham.org.br.nutricao.model.Mensagem;
import ham.org.br.nutricao.model.Message;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.Prato;
import ham.org.br.nutricao.model.TipoRefeicao;
import ham.org.br.nutricao.service.CardapioRetrofit;
import ham.org.br.nutricao.service.ExpandableListAdapter;
import ham.org.br.nutricao.service.Permissao;
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



    private  ServiceAPI serviceAPI;
    private int idTipoRef;
    private String data;
    public static Activity cardapioActivity;
    public static boolean cardapioAtivo =  false;
    private int cdCardapio;

    private ArrayList<String> listGrupo;
    private HashMap<String, String> listIngredientes;
    private HashMap<String, List<String>> listaItem;
    private ExpandableListAdapter listAdapter;
    private ProgressDialog dialog;

    private Database database;
    private SQLiteDatabase conn;
    private RepositorioTipoRefeicao repositorioTipoRefeicao;
    private RepositorioTipoPrato repositorioTipoPrato;
    private RepositorioPrato repositorioPrato;
    private RepositorioCardapio repositorioCardapio;

    private Bundle bundle;


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
        toolbar.setNavigationIcon( R.drawable.ic_action_arrow_left );

        setSupportActionBar( toolbar );






        bundle = getIntent().getExtras();
      //  Log.d("LodCABundle", ""+bundle);

        idTipoRef = bundle.getInt( "tipo" );
     //   Log.d("LodCodigoRef", ""+idTipoRef );
        data   = bundle.getString( "data" );
        String strTipo   = bundle.getString( "strTipo" );
        //toolbar.setTitle( "Cardápio - "+data+" - "+strTipo );
        setTitle( strTipo+" - "+data );
        getHoras();


        serviceAPI = ServiceAPI.retrofit.create(ServiceAPI.class);
         getDatas();
         codCardapio();


      //  chamarTipoPratos(idTipoRef, data );





    //    chamarPratos( serviceAPI );




        btn_acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getAcao() == 'N'){
                    finish();
                }else{
                    inserirOperacao( SenhaActivity.crachaBundle );
                }

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int item, long l) {
                String valor = listIngredientes.get(  listaItem.get( listGrupo.get( group ) ).get( item )  );
                // Log.i("Objeto", valor);
                dialogAlert( "Ingredientes",valor );
                return false;
            }
        });


    }

    private void getDatas(){
        bundle = getIntent().getExtras();
        //  Log.d("LodCABundle", ""+bundle);

        idTipoRef = bundle.getInt( "tipo" );
        //   Log.d("LodCodigoRef", ""+idTipoRef );
        data   = bundle.getString( "data" );
        String strTipo   = bundle.getString( "strTipo" );
        //toolbar.setTitle( "Cardápio - "+data+" - "+strTipo );
        setTitle( strTipo+" - "+data );
        getHoras();
    }

    private void codCardapio(){
        database = new Database( this );
        conn = database.getReadableDatabase();
        repositorioCardapio = new RepositorioCardapio( conn );
        repositorioCardapio.listarCardapios();
        conn = database.getReadableDatabase();
        repositorioCardapio = new RepositorioCardapio( conn );
        int varCdCardapio = repositorioCardapio.getCdCardapio( data, idTipoRef );
      //  Log.d("LogCACdCardapio", "Codigo do Cardapio: "+varCdCardapio);
        if( varCdCardapio > 0 ){
            setCdCardapio( varCdCardapio );
            preencherCardapioSQlite(  );
        }else{
            getMensagem( );
        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        cardapioAtivo = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        cardapioAtivo = false;
    }

    private void preencherCardapioSQlite(  ){
      //  Log.d("LodCAPreenCS","Preencher cardapio SQlite");
        database = new Database( this );
        conn = database.getReadableDatabase();
        repositorioPrato = new RepositorioPrato( conn );
       // repositorioPrato.getListPratos( getCdCardapio() );
        conn = database.getReadableDatabase();
        repositorioCardapio = new RepositorioCardapio( conn );
        String data = repositorioCardapio.getDataCardapio( getCdCardapio() );
       // Log.d("LodCADataCard", data);

        if( data.equals("") ){

                getMensagem();

        }
        else{
            listGrupo = new ArrayList<String>();
            listaItem = new HashMap<String, List<String>>();
            listIngredientes = new HashMap<String, String>();



            conn = database.getReadableDatabase();
            repositorioTipoPrato = new RepositorioTipoPrato( conn );
            repositorioPrato = new RepositorioPrato( conn );
          //  Log.d("231.LogCACdCar", ""+this.getCdCardapio());
            List<Prato> listTipoPratos = repositorioPrato.getTiposPratos( getCdCardapio()  );
            int loop =  0;


            int cdTipoPrato = 0;
            for( Prato prato : listTipoPratos ){


                conn = database.getReadableDatabase();
                repositorioTipoPrato = new RepositorioTipoPrato( conn );

                cdTipoPrato = prato.getTipoprato();

                String strTipoPrato = repositorioTipoPrato.getTipoPrato( cdTipoPrato );
                Log.d("LogCATipoPrato", strTipoPrato);
                listGrupo.add( strTipoPrato );

                conn = database.getReadableDatabase();
                repositorioPrato = new RepositorioPrato( conn );
                List<Prato> dishList = repositorioPrato.getPratos( cdCardapio, cdTipoPrato );

                ArrayList<String> pratos = new ArrayList<String>();
                ArrayList<String> ingredientes = new ArrayList<String>();
                for( Prato dish :  dishList ){

                    pratos.add( dish.getPrato() );
                    ingredientes.add( dish.getIngrediente() );
                    listIngredientes.put( dish.getPrato(), dish.getIngrediente() );

                }

                listaItem.put( listGrupo.get( loop ), pratos );

                loop++;

            }

            listAdapter = new ExpandableListAdapter( CardapioActivity.this, listGrupo, listaItem );

            expListView.setAdapter( listAdapter );


            /** deixando grupos expandidos por padrão**/
            for ( int i = 0; i < listAdapter.getGroupCount(); i++ ){

                expListView.expandGroup( i );
            }

            if( getHoras() > 0 ){
                btn_acao.setText( getString( R.string.btn_cancelar ) );
                btn_acao.setVisibility( View.VISIBLE );
                btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorNormalCancelar )  );
                acao =  'C' ;
                setAcao( 'C' );
            }else{
                btn_acao.setText( getString( R.string.label_voltar ) );
                btn_acao.setVisibility( View.VISIBLE );
                btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorPrimaryDark )  );
                acao =  'N' ;
                setAcao( 'N' );
            }
        }




    }

    private void getMensagem (  ){
        dialog = new ProgressDialog( CardapioActivity.this );
        dialog.setMessage( "Buscando Cardapios" );
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        listGrupo = new ArrayList<String>();
        listaItem = new HashMap<String, List<String>>();
        listIngredientes = new HashMap<String, String>();
        Call<Message> mensagemCall = serviceAPI.getMessage( "M", idTipoRef, data, SenhaActivity.crachaBundle );
        mensagemCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
              //  Log.i("Mensagens body",response.toString());
                if( response.isSuccessful() ){
                    int i = 0;
                    Message mensagem = response.body();
                   // mensagem = new Mensagem();
                    setCdCardapio( mensagem.getCardapio() );


                    if( mensagem.getCardapio() > 0 ){

                        List<TipoPrato> tipoPratoList = mensagem.getTipoPrato();

                        database =  new Database( getApplicationContext() );
                        conn = database.getWritableDatabase();
                        repositorioCardapio = new RepositorioCardapio( conn );
                        Cardapio cardapio = new Cardapio();
                      //  Log.d("LodCAdCdCardResp", ""+mensagem.getCardapio());
                        cardapio.setCodigo( mensagem.getCardapio() );
                   //     Log.d("LodCADataResp", data);
                        cardapio.setData( data );
                        cardapio.setTipo( idTipoRef );
                        long teste = repositorioCardapio.addCardapio( cardapio );
                        if( teste > 0 ){
                        //    Log.d("LogInsertCard: ", "Inserido com sucesso: "+teste);
                        }

                        for (TipoPrato tipoPrato : tipoPratoList){
                            ArrayList<Prato> pratoArrayList = tipoPrato.getPratoList();
                       //     Log.i("344. LogRPTipoprato: ", tipoPrato.getTipoprato());
                            listGrupo.add( tipoPrato.getTipoprato() );

                            database = new Database( getApplicationContext() );
                            conn = database.getReadableDatabase();
                            repositorioTipoPrato = new RepositorioTipoPrato( conn );
                            String strTipoPrato = repositorioTipoPrato.getTipoPrato( tipoPrato.getCdtipoprato() );
                            if( strTipoPrato.equals("") ){
                          //      Log.d("LogInsertTPPRato", "Nao possui tipoprato: "+tipoPrato.getTipoprato()+" ID - "+tipoPrato.getCdtipoprato());
                                conn = database.getWritableDatabase();
                                repositorioTipoPrato = new RepositorioTipoPrato( conn );

                                long retornoCard = repositorioTipoPrato.addTipoPrato( tipoPrato );
                                if( retornoCard > 0 ){
                           //         Log.d("LogInsertPrato", "Inserido com sucesso: "+retornoCard);

                                }

                            }



                            ArrayList<String> pratos = new ArrayList<String>();
                            ArrayList<String> ingredientes = new ArrayList<String>();

                            for( Prato prato : pratoArrayList ){

                                pratos.add( prato.getPrato() );
                                ingredientes.add( prato.getIngrediente() );
                                listIngredientes.put( prato.getPrato(), prato.getIngrediente() );

                                database = new Database( getApplicationContext() );
                                conn = database.getReadableDatabase();
                                repositorioPrato = new RepositorioPrato( conn );
                                if ( repositorioPrato.getPratoCount( getCdCardapio(), prato.getPrato() ) == 0 ){
                                    conn = database.getWritableDatabase();
                                    repositorioPrato = new RepositorioPrato( conn );
                                    Prato dish = new Prato();
                                    dish.setCardapio( tipoPrato.getCardapio() );
                                    dish.setTipoprato( tipoPrato.getCdtipoprato() );
                                    dish.setPrato( prato.getPrato() );
                                    dish.setIngrediente( prato.getIngrediente() );



                                    long retornoPrato = repositorioPrato.addPrato( dish );
                                    if( retornoPrato > 0 ){
                                     //   Log.d("LodInsertPrato", "Inserido com sucesso");
                                    }
                                }




                            }

                            listaItem.put( listGrupo.get( i ), pratos );

                            i++;

                        }


                    }

                  //  Log.d("431.ListaAposCad", "Listar tipos apos cadastro");
                    conn = database.getReadableDatabase();
                    repositorioTipoPrato = new RepositorioTipoPrato( conn );
                    repositorioTipoPrato.listaTipoPrato();




                    if( !mensagem.getMotivo().equals("") ){

                        dialogAlert( "Atenção!", mensagem.getMotivo() );

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

                            btn_acao.setText( getString( R.string.label_voltar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorIcone )  );
                            //btn_acao = new Button( new ContextThemeWrapper( CardapioActivity.this, R.style.botaoPadrao ) );
                            acao =  'N' ;
                            setAcao( 'N' );
                            break;
                        default:
                            btn_acao.setText( getString( R.string.btn_agendar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            break;
                    }

                }
                listAdapter = new ExpandableListAdapter( CardapioActivity.this, listGrupo, listaItem );

                expListView.setAdapter( listAdapter );


                /** deixando grupos expandidos por padrão**/
                for ( int i = 0; i < listAdapter.getGroupCount(); i++ ){

                    expListView.expandGroup( i );
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                //Log.i("onFailure Mensagem", t.getMessage());
                dialog.dismiss();
                if( !isOnline() ){
                    dialogAlert( "Ops","Ocorreu um problema ao conectar\nPor favor verique sua conexão" );
                }else{
                    dialogAlert( "Ops","Ocorreu um problema ao salvar operação\nTente novamente mais tarde " );
                    t.printStackTrace();
                }

                btn_acao.setVisibility( View.INVISIBLE );

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

                        dialogAlert( "Atenção!", mensagem.getMotivo() );

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
                            btn_acao.setText( getString( R.string.label_voltar ) );
                            btn_acao.setVisibility( View.VISIBLE );
                            btn_acao.setBackgroundColor(   getResources().getColor( R.color.colorPrimaryDark )  );
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
                if( !isOnline() ){
                    dialogAlert( "Ops","Ocorreu um problema ao conectar\nPor favor verique sua conexão" );
                }else{
                    dialogAlert( "Ops","Ocorreu um problema ao salvar operação\nTente novamente mais tarde" );
                }

                btn_acao.setVisibility( View.INVISIBLE );

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
                        conn = database.getWritableDatabase();
                        repositorioCardapio = new RepositorioCardapio( conn );
                        repositorioCardapio.excluir( getCdCardapio() );

                        conn = database.getWritableDatabase();
                        repositorioPrato = new RepositorioPrato( conn );
                        repositorioPrato.excluir( getCdCardapio() );
                        Agendamento agendamento = new Agendamento();
                        agendamento.setCardapio( getCdCardapio() );
                        agendamento.setCdTipo( idTipoRef );
                        database = new Database( getApplicationContext() );
                        conn = database.getReadableDatabase();
                        repositorioTipoRefeicao = new RepositorioTipoRefeicao( conn );
                        String strTipo = repositorioTipoPrato.getTipoPrato( idTipoRef );
                        agendamento.setData( data );
                        agendamento.setTipo( strTipo );
                        int position = AgendamentosFragment.arrayAdapter.getPosition( agendamento );
                        AgendamentosFragment.arrayAdapter.remove( agendamento );
                        AgendamentosFragment.arrayAdapter.notifyDataSetChanged(  );


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
                if( !isOnline() ){
                    dialogAlert( "Ops","Ocorreu um problema ao conectar\nPor favor verique sua conexão" );
                }else{
                    dialogAlert( "Ops","Ocorreu um problema ao salvar operação\nTente novamente mais tarde" );
                    t.printStackTrace();
                }


                //Log.i("onFailure age", t.getMessage());
                //Toast.makeText( context, , Toast.LENGTH_LONG ).show();
                //dialogMensagem( "Ops", "Ocorreu um problema ao salvar operação\nTente novamente mais tarde\n"+t.getMessage() );

            }
        });

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
            case R.id.action_alterar_senha_card:
                alterarSenha();
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
        if( MainActivity.mainAtivo ){
            MainActivity.mainActivity.finish();
        }

        finish();


    }

    private void alterarSenha(){

        Intent intent = new Intent( this, CriarSenhaActivity.class );
        intent.putExtra( "acao", 'A' );
        intent.putExtra( "nome", SenhaActivity.nomeBundle );
        intent.putExtra( "email", SenhaActivity.emailBundle );
        intent.putExtra( "cracha", SenhaActivity.crachaBundle );

        startActivity( intent );

    }


    private void dialogAlert( String titulo, String mensagem  ){
        //   AlertDialog.Builder alert = new AlertDialog.Builder( CrachaActivity.this  );
        AlertDialog.Builder alert = new AlertDialog.Builder( new ContextThemeWrapper( CardapioActivity.this , R.style.AlertDialogCustom )  );
        alert.setTitle( titulo );
        alert.setMessage( mensagem );
        alert.setNeutralButton( getString(R.string.lbl_ok), null);

        AlertDialog aviso = alert.create();
        aviso.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private double getHoras(){

        database = new Database( this );
        conn = database.getReadableDatabase();
        repositorioTipoRefeicao = new RepositorioTipoRefeicao( conn );
      //  Log.d("LodCACdTipoRef", ""+idTipoRef );
        TipoRefeicao tipoRefeicao = repositorioTipoRefeicao.getPrazos( idTipoRef );


        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
        df.setLenient(false);
        Date d1 = null;
        Date d2 = new Date();
        try {
            d1 = df.parse (data+" "+tipoRefeicao.getHrInicio()+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println (d2);
        double dia1 = ( d1.getTime() / 86400000 ); //86400000 representa 1 dia
        double dia2 = ( d2.getTime() / 86400000 ); //86400000 representa 1 dia

       // Log.d ("LodDataInicial",""+dia1);
      //  Log.d ("LodDataFinal",""+dia2);
        double dt =  dia1 - ( tipoRefeicao.getCancelamento() / 24 ) -  dia2  ; //
        //float dt = ( d1.getTime() - (( 12 / 24 ) * 3600000 ) ) -  d2.getTime()  ; // 1 hora para compensar horário de verão
       // float dt = (d2.getTime() - d1.getTime()) + 3600000; // 1 hora para compensar horário de verão
       // System.out.println (dt / 86400000L); // passaram-se 67111 dias
       // float passaram = dt / 86400000L;
     //   Log.d("LodHoraFinal", ""+dt );
        return dt;

    }


    @Override
    protected void onResume() {
        super.onResume();

        if( bundle !=  null ){
            getDatas();
        }

        database = new Database( this );
        Log.d("873.CAOnresume", "Buscardadospratos");
        conn = database.getReadableDatabase();
        repositorioPrato = new RepositorioPrato( conn );
        repositorioPrato.getListPratos( getCdCardapio() );

        conn = database.getReadableDatabase();
        repositorioTipoPrato = new RepositorioTipoPrato( conn );
        repositorioTipoPrato.listaTipoPrato();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



}


