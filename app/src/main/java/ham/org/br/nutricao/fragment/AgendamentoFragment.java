package ham.org.br.nutricao.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.service.AgendamentoRetrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendamentoFragment extends Fragment {

    ListView listViewAgendamento;

    public AgendamentoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agendamento, container, false);

        listViewAgendamento = (ListView) view.findViewById( R.id.lv_agendamento );

        AgendamentoRetrofit agendamentoRetrofit = new AgendamentoRetrofit( getActivity(), listViewAgendamento );
        agendamentoRetrofit.execute("4142");

        return view;
    }

}
