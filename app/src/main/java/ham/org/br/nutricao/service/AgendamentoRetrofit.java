package ham.org.br.nutricao.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.adapter.AgendaAdapter;
import ham.org.br.nutricao.database.Database;
import ham.org.br.nutricao.dominio.RepositorioAgendamento;
import ham.org.br.nutricao.model.Agendamento;
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

public class AgendamentoRetrofit extends AsyncTask<String, String, String> {
    private Context context;
    private ListView listView;
    private ProgressDialog dialog;
    private ArrayList<Agendamento> listaAgendamento;
    private ArrayAdapter<Agendamento> arrayAdapter;
    private Database database;
    private SQLiteDatabase conn;
    private RepositorioAgendamento repositorioAgendamento;

    public AgendamentoRetrofit(Context c, ListView lv ){
        this.context = c;
        this.listView = lv;

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog( context );
        dialog.setMessage( "Buscando agendamentos" );
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected String  doInBackground(String... strings) {
        dialog.setMessage( "Buscando agendamentos" );



        ServiceAPI serviceAPI = ServiceAPI.retrofit.create(ServiceAPI.class);
        listaAgendamento = new ArrayList<Agendamento>();

        //idTipoRef, data
        final Call<List<Agendamento>> agdCall = serviceAPI.getAgendamento( "G", strings[0] );
        agdCall.enqueue( new Callback<List<Agendamento>>() {
            @Override
            public void onResponse(Call<List<Agendamento>> call, Response<List<Agendamento>> response) {
                  //   Log.i("onResponse Agendamento",response.toString());

                if( response.isSuccessful() ){
                    List<Agendamento> lstAgd = response.body();

                    for( Agendamento agendamento : lstAgd  ){
                        database = new Database( context );
                        conn = database.getWritableDatabase();
                        repositorioAgendamento = new RepositorioAgendamento( conn );
                        Log.d("LodAgdRetrotipoRefRec",""+agendamento.getCdTipo());
                        Log.d("LodAgdRetroCallInsert","Insert");
                       // Log.d("tipoRefDs",""+agendamento.getTipo());
                        long teste = repositorioAgendamento.addAgendamento( agendamento );

                        Log.d("LodAgdRetroAfterInsert",""+teste);

                    }

                    listaAgendamento.addAll( lstAgd );

                }
                arrayAdapter = new AgendaAdapter( context, listaAgendamento );




                //setando list adapter
                listView.setAdapter( arrayAdapter );


                dialog.dismiss();



            }

            @Override
            public void onFailure(Call<List<Agendamento>> call, Throwable t) {
                dialog.dismiss();
                //Log.i("onFailure age", t.getMessage());
                if( !isOnline() ){

                    dialogAlert( "Ops","Ocorreu um problema ao conectar\nPor favor verique sua conexão" );
                }else{
                    dialogAlert( "Ops","Ocorreu um problema ao buscar os dados\n"+t.getMessage() );
                }

              //  Toast.makeText( context, "Ocorreu um problema ao buscar os dados\n"+t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });


        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
        dialog.setMessage( "Buscando agendamentos" );


    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Agendamento agendamento = listaAgendamento.get( i );

                Intent intent = new Intent( context, CardapioActivity.class );
                Log.d("LodAgdRetrCDTIPO", ""+agendamento.getCdTipo() );
                intent.putExtra( "tipo", agendamento.getCdTipo() );
                intent.putExtra( "data", agendamento.getData() );
                intent.putExtra( "strTipo", agendamento.getTipo() );

                context.startActivity( intent );

            }
        });
    }

    private void dialogAlert( String titulo, String mensagem  ){
        //   AlertDialog.Builder alert = new AlertDialog.Builder( CrachaActivity.this  );
        AlertDialog.Builder alert = new AlertDialog.Builder( new ContextThemeWrapper( context , R.style.AlertDialogCustom )  );
        alert.setTitle( titulo );
        alert.setMessage( mensagem );
        alert.setNeutralButton( context.getString(R.string.lbl_ok), null);

        AlertDialog aviso = alert.create();
        aviso.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
