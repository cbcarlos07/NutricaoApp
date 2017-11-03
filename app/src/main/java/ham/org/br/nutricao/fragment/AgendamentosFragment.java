package ham.org.br.nutricao.fragment;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.activity.CardapioActivity;
import ham.org.br.nutricao.activity.MainActivity;
import ham.org.br.nutricao.activity.SenhaActivity;
import ham.org.br.nutricao.adapter.AgendaAdapter;
import ham.org.br.nutricao.adapter.SwipeListAdapter;
import ham.org.br.nutricao.database.Database;
import ham.org.br.nutricao.dominio.RepositorioAgendamento;
import ham.org.br.nutricao.dominio.RepositorioCardapio;
import ham.org.br.nutricao.dominio.RepositorioPrato;
import ham.org.br.nutricao.dominio.RepositorioTipoPrato;
import ham.org.br.nutricao.model.Agendamento;
import ham.org.br.nutricao.service.AgendamentoRetrofit;


public class AgendamentosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListAdapter adapter;
    private ArrayList<Agendamento> agendamentoList;
    private Database database;
    private SQLiteDatabase conn;
    private RepositorioAgendamento repositorioAgendamento;
    private RepositorioCardapio repositorioCardapio;
    public static ArrayAdapter<Agendamento> arrayAdapter;

    public AgendamentosFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agendamentos, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.swipe_refresh_layout );

        listView = (ListView) view.findViewById( R.id.lv_agendamento_swipe );

        swipeRefreshLayout.setOnRefreshListener( this );


        if( MainActivity.AGENDAMENTOS == 0 ){
           // Log.d("LodAgendamentos", "Não possui Agendamentos");
            buscarDados();
            comboBox();
        }else{
           // Log.d("LodAgendamentos", "possui Agendamentos");
            comboBox();
        }


    /*    swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing( true );
                refreshScreen();
            }
        });*/
        // Inflate the layout for this fragment






        return view;
    }

    private void refreshScreen(){
        swipeRefreshLayout.setRefreshing( true );
        if( MainActivity.AGENDAMENTOS > 0 ){
            database = new Database( getActivity() );
            conn = database.getWritableDatabase();
            repositorioAgendamento = new RepositorioAgendamento( conn );
            repositorioAgendamento.excluirTudo();

            conn = database.getWritableDatabase();
            repositorioCardapio = new RepositorioCardapio( conn );
            repositorioCardapio.excluirTudo();

            conn = database.getWritableDatabase();
            RepositorioPrato repositorioPrato = new RepositorioPrato( conn );
            repositorioPrato.excluirTudo();

            conn = database.getWritableDatabase();
            RepositorioTipoPrato repositorioTipoPrato = new RepositorioTipoPrato( conn );
            repositorioTipoPrato.excluirTudo();

            buscarDados();

            comboBox();
        }else{

            buscarDados();

            comboBox();
        }
        swipeRefreshLayout.setRefreshing( false );
    }

    private void buscarDados(){
        //swipeRefreshLayout.setRefreshing( true );

            AgendamentoRetrofit agendamentoRetrofit = new AgendamentoRetrofit( getActivity(), listView );
            agendamentoRetrofit.execute( SenhaActivity.crachaBundle );

        //swipeRefreshLayout.setRefreshing( false );



    }

    @Override
    public void onRefresh() {
        refreshScreen();
    }


    public void comboBox(){
       // Log.d("LodMetodo", "Combobox");
        database = new Database( getActivity() );
        conn = database.getReadableDatabase();
        repositorioAgendamento = new RepositorioAgendamento( conn );
        agendamentoList = repositorioAgendamento.getAllAgendamento();

        arrayAdapter = new AgendaAdapter( getActivity(), agendamentoList );
        listView.setAdapter( arrayAdapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Log.d("LodCliqueNovaTela", "Nova tela");
                abrirNovaTela( i );
            }
        });
    }

    private void  abrirNovaTela( int i ){

      //  Log.d( "LodAFAbrirNovaTela", "Abrir nova tela" );

        Agendamento agendamento = agendamentoList.get( i );

        Intent intent = new Intent( getActivity(), CardapioActivity.class );

        if( CardapioActivity.cardapioAtivo ){
            CardapioActivity.cardapioActivity.finish();
         //   Log.d("174.LogAFCardaAct","CardapioAcitivty ativo finalizar");
        }

      //  Log.d("LodAF Codigo Tipo", ""+agendamento.getCdTipo());

        intent.putExtra( "tipo", agendamento.getCdTipo() );
        intent.putExtra( "data", agendamento.getData() );
        intent.putExtra( "strTipo", agendamento.getTipo() );

        getActivity().startActivity( intent );
    }





}
