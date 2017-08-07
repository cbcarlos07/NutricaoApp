package ham.org.br.nutricao.service;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ham.org.br.nutricao.R;

/**
 * Created by carlos.bruno on 02/08/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> listTipoPrato; //cabeçalho dos grupos
    //
    private HashMap<String, List<String>> listPratos;
    public ExpandableListAdapter(Context c, List<String> tipoPratos,
                                 HashMap<String, List<String>> pratos) {
        this.context = c;
        this.listTipoPrato = tipoPratos;
        this.listPratos = pratos;

    }

    @Override
    public int getGroupCount() {
        return this.listTipoPrato.size();
    }

    @Override
    public int getChildrenCount(int grupoPosicao) {
        return this.listPratos.get( this.listTipoPrato.get( grupoPosicao ) ).size() ;
    }

    @Override
    public Object getGroup(int grupoPosicao) {
        return this.listTipoPrato.get( grupoPosicao );

    }

    @Override
    public Object getChild(int grupoPosicao, int itemPosicao) {
        return this.listPratos.get( this.listTipoPrato.get( grupoPosicao ) ).get( itemPosicao );

    }

    @Override
    public long getGroupId(int grupoPosicao) {
        return grupoPosicao;
    }

    @Override
    public long getChildId(int grupoPosicao, int itemPosicao) {
        return itemPosicao;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int grupoPosicao, boolean b, View view, ViewGroup viewGroup) {
        String tituloGrupo = (String) getGroup( grupoPosicao );
        if( view == null ){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.list_group, null );
        }

        TextView lblTipoPrato = (TextView) view.findViewById( R.id.tv_tipo_prato );
        lblTipoPrato.setTypeface(null, Typeface.BOLD);
        lblTipoPrato.setText( tituloGrupo );

        return view;
    }

    @Override
    public View getChildView(int grupoPosicao, int itemPosicao, boolean isLastItem, View view, ViewGroup viewGroup) {
        String item = (String) getChild( grupoPosicao, itemPosicao );
        if( view == null ){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.lista_item, null );
        }

        TextView lblTextoItem = (TextView) view.findViewById( R.id.tv_pratos );
      //  lblTextoItem.setTypeface(null, Typeface.BOLD);
        lblTextoItem.setText( item );

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
