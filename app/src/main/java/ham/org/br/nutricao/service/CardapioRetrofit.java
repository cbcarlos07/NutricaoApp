package ham.org.br.nutricao.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.model.Prato;
import ham.org.br.nutricao.model.TipoPrato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by carlos.bruno on 10/08/2017.
 */

public class CardapioRetrofit extends AsyncTask<String, String, ExpandableListView> {
    private Context context;
    private ArrayList<String> listGrupo;
    private HashMap<String, String> listIngredientes;
    private HashMap<String, List<String>> listaItem;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ProgressDialog dialog;

    public CardapioRetrofit ( Context c, ExpandableListView expandableListView ){
        this.context = c;
        this.expListView = expandableListView;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog( context );
        dialog.setMessage( "Recebendo Cardápio" );
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected ExpandableListView doInBackground(String... strings) {
        dialog.setMessage( "Recebendo Cardápio" );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServiceAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        ServiceAPI serviceAPI = retrofit.create(ServiceAPI.class);

        Log.i("ChamarPratos", "Pratos");

        listGrupo = new ArrayList<String>();
        listaItem = new HashMap<String, List<String>>();
        listIngredientes = new HashMap<String, String>();

        //idTipoRef, data
        final Call<List<TipoPrato>> listTipoPratos = serviceAPI.getlistTipoPratos("R", Integer.parseInt(strings[0]), strings[1]);
        listTipoPratos.enqueue( new Callback<List<TipoPrato>>() {
            @Override
            public void onResponse(Call<List<TipoPrato>> call, Response<List<TipoPrato>> response) {
                //     Log.i("onResponse TipoPratos",response.toString());
                int cardapio = 0;
                if( response.isSuccessful() ){
                    List<TipoPrato> tipoPratos = response.body();
                    int i = 0;

                    for( TipoPrato tipoPrato : tipoPratos ){

                        Log.i("Tipo", ""+tipoPrato.getCardapio());
                        cardapio  = tipoPrato.getCardapio();
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
                listAdapter = new ExpandableListAdapter( context, listGrupo, listaItem );



                //setando list adapter
                expListView.setAdapter( listAdapter );


                /** deixando grupos expandidos por padrão**/
                for ( int i = 0; i < listAdapter.getGroupCount(); i++ ){

                    expListView.expandGroup( i );
                }
                dialog.dismiss();



            }

            @Override
            public void onFailure(Call<List<TipoPrato>> call, Throwable t) {
                //Log.i("onFailure TipoPrato", t.getMessage());
                dialog.dismiss();
                //Log.i("onFailure age", t.getMessage());
                Toast.makeText( context, "Ocorreu um problema ao buscar os dados\nTente novamente mais tarde\n"+t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });


        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
        dialog.setMessage( "Recebendo Cardápio" );
        //ListView on child click listener

    }

    @Override
    protected void onPostExecute(ExpandableListView expandableListView) {
        //super.onPostExecute(expandableListView);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int item, long l) {
                String valor = listIngredientes.get(  listaItem.get( listGrupo.get( group ) ).get( item )  );
                // Log.i("Objeto", valor);
                abrirMensagem( "Ingredientes",valor );
                return false;
            }
        });
    }

    private void abrirMensagem(String titulo, String mensagem ){

        AlertDialog.Builder alert = new AlertDialog.Builder( context );
        alert.setTitle(titulo);
        alert.setMessage( mensagem );
        alert.setNeutralButton("OK", null);
        AlertDialog dialog = alert.create();
        dialog.show();

    }

}
