package ham.org.br.nutricao.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.fragment.PesquisarFragment;
import ham.org.br.nutricao.model.Prato;
import ham.org.br.nutricao.model.TipoPrato;
import ham.org.br.nutricao.model.TipoRefeicao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by carlos.bruno on 10/08/2017.
 */

public class TipoRefeicaoRetrofit extends AsyncTask<String, String, ArrayList<TipoRefeicao>> {
    private Context context;
    private ArrayList<TipoRefeicao> listTipoRefeicao;
    private Spinner spinner;
    private ProgressDialog dialog;
    ArrayAdapter<TipoRefeicao> adapterTipoRefeicao;
    private Button btn_pesquisar;
    private TextView textView;
    public TipoRefeicaoRetrofit(Context c, Spinner spnr, Button button, TextView tV){
        this.context = c;
        this.spinner = spnr;
        this.btn_pesquisar = button;
        this.textView = tV;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog( context );
        dialog.setMessage( "Aguarde um momento" );
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected ArrayList<TipoRefeicao> doInBackground(String... strings) {
        dialog.setMessage( "Aguarde um momento" );


        ServiceAPI serviceAPI = ServiceAPI.retrofit.create(ServiceAPI.class);

        listTipoRefeicao =  new ArrayList<TipoRefeicao>();

        Call<List<TipoRefeicao>> callList = serviceAPI.getlistTipoRefeicao("A");
        callList.enqueue(new Callback<List<TipoRefeicao>>() {
            @Override
            public void onResponse(Call<List<TipoRefeicao>> call, Response<List<TipoRefeicao>> response) {
                // Log.i("onResponse TipoRefeicao",response.toString());
                if( response.isSuccessful() ){
                    List<TipoRefeicao> lstTipoRefeicao = response.body();
                    // Log.i("Mensagem",lstTipoRefeicao.toString());

                    for ( TipoRefeicao tipoRefeicao : lstTipoRefeicao ){
                        listTipoRefeicao.add( tipoRefeicao );
                        Log.i("TipoFor",tipoRefeicao.getDescricao());
                    }

                    PesquisarFragment pesquisarFragment = new PesquisarFragment();
                    pesquisarFragment.setListTipoRefeicao( listTipoRefeicao );

                    adapterTipoRefeicao = new ArrayAdapter<TipoRefeicao>(
                            context,
                            R.layout.item_tipo_refeicao,
                            R.id.tv_item_tipo_prato,
                            listTipoRefeicao);
                    spinner.setAdapter( adapterTipoRefeicao );

                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<TipoRefeicao>> call, Throwable t) {
               // Log.i("onFailure TipoRefeicao", t.getMessage());
                dialog.dismiss();
                //Log.i("onFailure age", t.getMessage());
                Toast.makeText( context, "Ocorreu um problema ao buscar os dados\n"+t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        });


        return listTipoRefeicao;
    }

    @Override
    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
        dialog.setMessage( "Aguarde um momento" );

    }

    @Override
    protected void onPostExecute(ArrayList<TipoRefeicao> tipoRefeicaos) {
        //super.onPostExecute(tipoRefeicaos);
        btn_pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, CardapioActivity.class );

                TipoRefeicao tipoRefeicao = listTipoRefeicao.get( spinner.getSelectedItemPosition() )  ;
                intent.putExtra( "tipo", tipoRefeicao.getCodigo() );
                intent.putExtra( "data", textView.getText() );
                intent.putExtra( "strTipo", tipoRefeicao.getDescricao() );
                context.startActivity( intent );

            }
        });
    }
}
