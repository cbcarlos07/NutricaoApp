package ham.org.br.nutricao.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.activity.MainActivity;
import ham.org.br.nutricao.database.Database;
import ham.org.br.nutricao.dominio.RepositorioTipoRefeicao;
import ham.org.br.nutricao.model.TipoRefeicao;
import ham.org.br.nutricao.service.ServiceAPI;
import ham.org.br.nutricao.service.TipoRefeicaoRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFragment extends Fragment implements View.OnClickListener{
    static SimpleDateFormat data_br = new SimpleDateFormat("dd/MM/yyyy");
    static Date dataAtual = new Date();

    private Spinner spinnerTipoRefeicao;
    private TextView textViewData;
    private ArrayList<TipoRefeicao> listTipoRefeicao;

    private Button pesquisar;
    private String strData = data_br.format( dataAtual );
    private RepositorioTipoRefeicao repositorioTipoRefeicao;
    private Database database;
    private SQLiteDatabase conn;
    ArrayAdapter<TipoRefeicao> adapterTipoRefeicao;

    private ProgressDialog dialog;

    public PesquisarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);

        spinnerTipoRefeicao = (Spinner) view.findViewById( R.id.spinnerTipoRefeicao );
        textViewData = (TextView) view.findViewById( R.id.textViewData );
        pesquisar    = (Button) view.findViewById( R.id.btn_consultar );


        if(MainActivity.TIPO_REFEICAO == 0 ){
            chamarTipoRefeicao();
        }else{
            comboBox();
        }




        textViewData.setText( strData );

        textViewData.setOnClickListener(this);

        pesquisar.setOnClickListener(this);








        return view;


    }

    private void chamarTipoRefeicao( ){


        TipoRefeicaoRetrofit tipoRefeicaoRetrofit = new TipoRefeicaoRetrofit( getActivity(), spinnerTipoRefeicao, pesquisar, textViewData );
        tipoRefeicaoRetrofit.execute(  );


    }



    public void showDatePickerDialog() {

        DialogFragment newFragment = new DatePickerFragment( textViewData );
        newFragment.show(getActivity().getFragmentManager(), "datePicker");

    }






    private void comboBox(){
        abrirEspera();
        database = new Database( getActivity() );
        conn = database.getReadableDatabase();
        repositorioTipoRefeicao = new RepositorioTipoRefeicao( conn );
        listTipoRefeicao = repositorioTipoRefeicao.getAllTipoRefeicao(  );
        adapterTipoRefeicao = new ArrayAdapter<TipoRefeicao>(
                getActivity(),
                R.layout.item_tipo_refeicao,
                R.id.tv_item_tipo_prato,
                listTipoRefeicao);
        spinnerTipoRefeicao.setAdapter( adapterTipoRefeicao );
        fecharEspera();
    }



    @Override
    public void onClick(View view) {
        switch ( view.getId() ){
            case R.id.btn_consultar:
                    abrirTela();
                break;
            case R.id.textViewData:
                  showDatePickerDialog();
                break;
        }
    }

    private void abrirTela(){
        Intent intent = new Intent( getActivity(), CardapioActivity.class );

        TipoRefeicao tipoRefeicao = listTipoRefeicao.get( spinnerTipoRefeicao.getSelectedItemPosition() )  ;
        intent.putExtra( "tipo", tipoRefeicao.getCodigo() );
        intent.putExtra( "data", textViewData.getText() );
        intent.putExtra( "strTipo", tipoRefeicao.getDescricao() );
        getActivity().startActivity( intent );
    }

    private void abrirEspera(){
        dialog = new ProgressDialog( getActivity() );
        dialog.setMessage( "Aguarde um momento" );
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void fecharEspera(){
        dialog.dismiss();
    }
}
