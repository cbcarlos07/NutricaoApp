package ham.org.br.nutricao.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.TipoRefeicao;
import ham.org.br.nutricao.model.TiposRefeicao;
import ham.org.br.nutricao.service.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFragment extends Fragment {
    private final String BASE_URL = "http://10.50.140.54/webservice/";
    private List<TipoRefeicao> listTipoRefeicao;
    private List <String> listData;
    private int idTipoRefeicao;
    SimpleDateFormat data_br = new SimpleDateFormat("dd/MM/yyyy");
    Date dataAtual = new Date();
    private ArrayAdapter<TipoRefeicao> adapterTipoRefeicao;



    ListView listViewTipoRef;
    ListView listViewData;
    private String[] datas = {
            data_br.format( dataAtual )
    };

    public PesquisarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);

        listViewTipoRef = (ListView) view.findViewById( R.id.lv_tp_refeicao );
        listViewData = (ListView) view.findViewById( R.id.lv_Data );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        Service service = retrofit.create(Service.class);

        chamarTipoRefeicao( service );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_data,
                R.id.tv_item_data,
                datas
        );

        listViewData.setAdapter( dataAdapter );
        return view;
    }

    private void chamarTipoRefeicao( Service service ){
        listTipoRefeicao =  new ArrayList<TipoRefeicao>();
        final List<TipoRefeicao> tipoRefeicaoList = new ArrayList<TipoRefeicao>();
        TiposRefeicao tiposRefeicao = new TiposRefeicao();
        Call<List<TipoRefeicao>> callList = service.getlistTipoRefeicao("A");
        callList.enqueue(new Callback<List<TipoRefeicao>>() {
            @Override
            public void onResponse(Call<List<TipoRefeicao>> call, Response<List<TipoRefeicao>> response) {
                Log.i("onResponse TipoRefeicao",response.toString());
                if( response.isSuccessful() ){
                    List<TipoRefeicao> lstTipoRefeicao = response.body();
                    Log.i("TiposRefeicao",lstTipoRefeicao.toString());

                    for ( TipoRefeicao tipoRefeicao : lstTipoRefeicao ){
                        listTipoRefeicao.add( tipoRefeicao );
                        Log.i("TipoFor",tipoRefeicao.getDescricao());
                    }

                    Iterator iterator = listTipoRefeicao.iterator();
                    if( iterator.hasNext() ){
                        TipoRefeicao tipoRefeicao = (TipoRefeicao) iterator.next();
                        Log.i("Tipo",tipoRefeicao.getDescricao());
                        tipoRefeicaoList.add( tipoRefeicao );
                        idTipoRefeicao = tipoRefeicao.getCodigo();
                    }
                    adapterTipoRefeicao = new ArrayAdapter<TipoRefeicao>(
                            getActivity(),
                            R.layout.item_tipo_refeicao,
                            R.id.tv_item_tipo_prato,
                            tipoRefeicaoList);
                    listViewTipoRef.setAdapter( adapterTipoRefeicao );
                }
            }

            @Override
            public void onFailure(Call<List<TipoRefeicao>> call, Throwable t) {
                Log.i("onFailure TipoRefeicao", t.getMessage());
            }
        });

    }

}
