package ham.org.br.nutricao.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.ArrayList;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.Agendamento;

/**
 * Created by carlos.bruno on 11/08/2017.
 */

public class AgendaAdapter extends ArrayAdapter<Agendamento> {

    private ArrayList<Agendamento> agendamentos;
    private Context context;


    public AgendaAdapter(@NonNull Context c,  @NonNull ArrayList<Agendamento> objects) {
        super(c, 0, objects);
        this.context = c;
        this.agendamentos = objects;
    }

    public View getView (int position, View convertView, ViewGroup parent){

        View view = null;

        //verifica se Lista está vazia
        if( agendamentos != null ){

            //Inicializa objetos para a montagem da view
            LayoutInflater inflater = ( LayoutInflater ) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );

            //Monta a nossa view a partir do xml
            view = inflater.inflate( R.layout.lista_agendamento, parent, false );

            //recupera elemento para exibicao
            TextView data = (TextView) view.findViewById( R.id.tv_agd_data );
            TextView tipo = (TextView) view.findViewById( R.id.tv_agd_tipo );

            Agendamento agendamento = agendamentos.get( position );
            data.setText( agendamento.getData() );
            tipo.setText( agendamento.getTipo() );

        }

        return view;

    }
}
