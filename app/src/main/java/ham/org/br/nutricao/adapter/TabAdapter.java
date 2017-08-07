package ham.org.br.nutricao.adapter;

/**
 * Created by carlos.bruno on 07/08/2017.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import ham.org.br.nutricao.activity.AgendamentoFragment;
import ham.org.br.nutricao.fragment.PesquisarFragment;

/**
 * Created by carlos.bruno on 11/06/2017.
 */
public class TabAdapter extends FragmentStatePagerAdapter{

    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"}; //Maiuscula é a recomendação

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch ( position ){
            case 0:
                fragment = new PesquisarFragment();
                break;
            case 1:
                fragment = new AgendamentoFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length; //quantidade de abas
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[ position ];
    }
}