package ham.org.br.nutricao.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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
public class PesquisarFragment extends Fragment {
    static SimpleDateFormat data_br = new SimpleDateFormat("dd/MM/yyyy");
    static Date dataAtual = new Date();

    private Spinner spinnerTipoRefeicao;
    private TextView textViewData;
    private ArrayList<TipoRefeicao> listTipoRefeicao;

    private Button pesquisar;
    private String strData = data_br.format( dataAtual );
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



        chamarTipoRefeicao();



        textViewData.setText( strData );

        textViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        listTipoRefeicao = getListTipoRefeicao();







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






    public ArrayList<TipoRefeicao> getListTipoRefeicao(){
        return this.listTipoRefeicao;
    }

    public void setListTipoRefeicao( ArrayList<TipoRefeicao> lista ){
        this.listTipoRefeicao = lista;
    }

}
