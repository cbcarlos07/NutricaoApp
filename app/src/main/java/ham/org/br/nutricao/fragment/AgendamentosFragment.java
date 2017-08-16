package ham.org.br.nutricao.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.activity.SenhaActivity;
import ham.org.br.nutricao.adapter.SwipeListAdapter;
import ham.org.br.nutricao.model.Agendamento;
import ham.org.br.nutricao.service.AgendamentoRetrofit;
import ham.org.br.nutricao.service.AgendamentoRetrofitSwiper;


public class AgendamentosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListAdapter adapter;
    private List<Agendamento> agendamentoList;

    public AgendamentosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agendamentos, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.swipe_refresh_layout );

        listView = (ListView) view.findViewById( R.id.lv_agendamento_swipe );

        swipeRefreshLayout.setOnRefreshListener( this );

        buscarDados();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing( true );
                buscarDados();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void buscarDados(){
        swipeRefreshLayout.setRefreshing( true );
        AgendamentoRetrofit agendamentoRetrofit = new AgendamentoRetrofit( getActivity(), listView );
        agendamentoRetrofit.execute( SenhaActivity.crachaBundle );
        swipeRefreshLayout.setRefreshing( false );

    }

    @Override
    public void onRefresh() {
        buscarDados();
    }

    @Override
    public void onStart() {
        super.onStart();
        buscarDados();
    }

    @Override
    public void onResume() {
        super.onResume();
        buscarDados();

    }
}
