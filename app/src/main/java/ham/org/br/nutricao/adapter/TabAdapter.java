package ham.org.br.nutricao.adapter;

/**
 * Created by carlos.bruno on 07/08/2017.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.ViewGroup;


import java.util.HashMap;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.fragment.AgendamentoFragment;
import ham.org.br.nutricao.fragment.PesquisarFragment;

/**
 * Created by carlos.bruno on 11/06/2017.
 */
public class TabAdapter extends FragmentStatePagerAdapter{

    private Context context;
    // private String[] abas = new String[]{"HOME", "USUÁRIOS"};
    private int[] icones = new int[]{R.drawable.ic_action_search, R.drawable.ic_date_range };
    private int tamanhoIcone;
    private HashMap<Integer, Fragment> fragmentosUtilizados;
    public TabAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
        double escala = this.context.getResources().getDisplayMetrics().density; //tamanho do dispositivo
        tamanhoIcone = (int) (25 * escala);
        this.fragmentosUtilizados = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new PesquisarFragment();
                fragmentosUtilizados.put( position, fragment );
                break;
            case 1:
                fragment = new AgendamentoFragment();

                break;
        }

        return fragment;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentosUtilizados.remove( position );
    }

    public Fragment getFragment(int indice){

        return fragmentosUtilizados.get( indice );
    }

    @Override
    public int getCount() {

        //return abas.length;
        return icones.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // return abas[ position ];
        Drawable drawable = ContextCompat.getDrawable( this.context, icones[ position ] );
        drawable.setBounds( 0, 0, tamanhoIcone, tamanhoIcone );

        //Permite colocar uma imagemm dentro de um texto
        ImageSpan imageSpan = new ImageSpan( drawable ); //ImageSpan = coloca imagem dentro de texto

        //Classe utilizada para retornar CharSequence
        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan( imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        return spannableString;

    }
}