package ham.org.br.nutricao.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ham.org.br.nutricao.R;

import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.Prato;
import ham.org.br.nutricao.model.TipoRefeicao;
import ham.org.br.nutricao.service.ExpandableListAdapter;
import ham.org.br.nutricao.service.Permissao;
import ham.org.br.nutricao.service.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardapioActivity extends AppCompatActivity {

    private final String BASE_URL = "http://10.50.140.54/webservice/";
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET

    };
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<TipoRefeicao> listTipoRefeicao;
    List<String> listGrupo;
    List<String> listIngrediente;
    HashMap<String, String> listIngredientes;
    HashMap<String, List<String>> listaItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardapio_activity);

        Permissao.validaPermissoes(1, this, permissoesNecessarias );

        expListView = ( ExpandableListView ) findViewById( R.id.lvExp );



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        Service service = retrofit.create(Service.class);

        chamarTipoPratos( service );

        listAdapter = new ExpandableListAdapter( this, listGrupo, listaItem );

        //setando list adapter
        expListView.setAdapter( listAdapter );
    //    chamarPratos( service );

        //ListView on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int item, long l) {
                String valor = listIngredientes.get(  listaItem.get( listGrupo.get( group ) ).get( item )  );
               // Log.i("Objeto", valor);
                abrirMensagem( valor );
                return false;
            }
        });


    }


    private void chamarTipoPratos( Service service ){
        listGrupo = new ArrayList<String>();
        listaItem = new HashMap<String, List<String>>();
        listIngredientes = new HashMap<String, String>();
        listIngrediente = new ArrayList<String>();
        final Call<List<TipoPrato>> listTipoPratos = service.getlistTipoPratos("R", 5, "03/08/2017");
        listTipoPratos.enqueue( new Callback<List<TipoPrato>>() {
            @Override
            public void onResponse(Call<List<TipoPrato>> call, Response<List<TipoPrato>> response) {
           //     Log.i("onResponse TipoPratos",response.toString());

                if( response.isSuccessful() ){
                    List<TipoPrato> tipoPratos = response.body();
                    int i = 0;
                    for( TipoPrato tipoPrato : tipoPratos ){

                        //Log.i("Tipo", tipoPrato.getTipoprato());
                        ArrayList<Prato> listaPratos = tipoPrato.getPratoList();

                        listGrupo.add(tipoPrato.getTipoprato());
                        ArrayList<String> pratos = new ArrayList<String>();
                        ArrayList<String> ingredientes = new ArrayList<String>();

                        for ( Prato prato : listaPratos ){
                       //     Log.i("Pratos", prato.getPrato());
                            pratos.add( prato.getPrato() );
                            ingredientes.add( prato.getIngrediente() );
                            listIngredientes.put( prato.getPrato(), prato.getIngrediente() );
                        }

                        listaItem.put( listGrupo.get( i ), pratos );

                        i++;

                }

            }
            }

            @Override
            public void onFailure(Call<List<TipoPrato>> call, Throwable t) {
                Log.i("onFailure TipoPrato", t.getMessage());
            }
        });
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

    private void abrirMensagem( String mensagem ){

        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle("Ingredientes");
        alert.setMessage( mensagem );
        alert.setNeutralButton("OK", null);
        AlertDialog dialog = alert.create();
        dialog.show();

    }

}