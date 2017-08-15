package ham.org.br.nutricao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.Agendamento;

/**
 * Created by carlos.bruno on 15/08/2017.
 */

public class SwipeListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Agendamento> agendamentoArrayList;

    public SwipeListAdapter( Context c, ArrayList<Agendamento> lista ){

        this.context = c;
        this.agendamentoArrayList = lista;

    }

    @Override
    public int getCount() {
        return agendamentoArrayList.size();
    }

    @Override
    public Object getItem(int location ) {
        return agendamentoArrayList.get( location );
    }

    @Override
    public long getItemId(int position ) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
         view = null;

        //verifica se Lista está vazia
        if( agendamentoArrayList != null ){

            //Inicializa objetos para a montagem da view
            LayoutInflater inflater = ( LayoutInflater ) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );

            //Monta a nossa view a partir do xml
            view = inflater.inflate( R.layout.lista_agendamento, viewGroup, false );

            //recupera elemento para exibicao
            TextView data = (TextView) view.findViewById( R.id.tv_agd_data );
            TextView tipo = (TextView) view.findViewById( R.id.tv_agd_tipo );

            Agendamento agendamento = agendamentoArrayList.get( position );
            data.setText( agendamento.getData() );
            tipo.setText( agendamento.getTipo() );

        }

        return view;
    }
}
