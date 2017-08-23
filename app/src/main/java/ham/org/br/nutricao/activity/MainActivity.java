package ham.org.br.nutricao.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.adapter.TabAdapter;
import ham.org.br.nutricao.adapter.ViewPagerAdapter;
import ham.org.br.nutricao.fragment.AgendamentosFragment;
import ham.org.br.nutricao.fragment.PesquisarFragment;
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.util.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static Activity mainActivity;
    public static boolean mainAtivo = false;
    private int[] tabIcons = {
             R.drawable.ic_action_search,
             R.drawable.ic_date_range
           };
    private int tamanhoIcone;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Nutrição" );
        setSupportActionBar( toolbar );
        mainActivity = this;

        //slidingTabLayout = ( SlidingTabLayout ) findViewById( R.id.slt_tabs );
        viewPager        = ( ViewPager ) findViewById( R.id.vp_pagina );
        setupViewerPager( viewPager );
        tabLayout        = ( TabLayout ) findViewById( R.id.tabs );
        tabLayout.setupWithViewPager( viewPager );
        tabLayout.setSelectedTabIndicatorColor( ContextCompat.getColor( this, R.color.colorTabIndicator ) );
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.tabSelecionado);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.tabNaoSelecionado );
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        double escala = this.getResources().getDisplayMetrics().density; //tamanho do dispositivo
        tamanhoIcone = (int) (25 * escala);


         setupTabIcons();



       /* TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager(), this );
        viewPager.setAdapter( tabAdapter );*/




        /*slidingTabLayout.setCustomTabView( R.layout.tab_view, R.id.text_item_tab );
        slidingTabLayout.setDistributeEvenly( true );
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor( this, R.color.colorTabIndicator ));
        slidingTabLayout.setViewPager( viewPager );*/

        TabLayout.Tab tab = tabLayout.getTabAt( 1 );
        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.tabNaoSelecionado );
        tab.getIcon().setColorFilter( tabIconColor, PorterDuff.Mode.SRC_IN );





    }

    private void setupTabIcons(){
        Drawable drawable1 = ContextCompat.getDrawable( this, tabIcons[ 0 ] );
        Drawable drawable2 = ContextCompat.getDrawable( this, tabIcons[ 1 ] );
        drawable1.setBounds( 0, 0, tamanhoIcone, tamanhoIcone );
        drawable2.setBounds( 0, 0, tamanhoIcone, tamanhoIcone );

        //
        tabLayout.getTabAt( 0 ).setIcon( drawable1 );
        tabLayout.getTabAt( 1 ).setIcon( drawable2 );




    }



    private void setupViewerPager(ViewPager viewPager ){

        ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager() );
        adapter.addFragment( new PesquisarFragment(), "Pesquisar" );
        adapter.addFragment( new AgendamentosFragment(), "Agendar" );
        viewPager.setAdapter( adapter );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ){

            case R.id.action_perfil:
                abrirPerfil();
                break;

            case R.id.action_sair:
                sair();
                break;
            case R.id.action_alterar_senha:
                alterarSenha();;
                break;

        }
        return super.onOptionsItemSelected( item );
    }

    private void abrirPerfil(){

        Intent intent = new Intent( MainActivity.this, PerfilActivity.class );
        startActivity( intent );

    }

    private void sair(){
        Preferences preferences = new Preferences( MainActivity.this );
        preferences.salvarDados( null, null, null );

        Intent intent = new Intent( MainActivity.this, CrachaActivity.class );
        startActivity( intent );
        if( CardapioActivity.cardapioAtivo ){
            CardapioActivity.cardapioActivity.finish();
        }

        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAtivo = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAtivo = false;
    }

    private void alterarSenha(){

        Intent intent = new Intent( this, CriarSenhaActivity.class );
        intent.putExtra( "acao", "A" );
        intent.putExtra( "nome", SenhaActivity.nomeBundle );
        intent.putExtra( "email", SenhaActivity.emailBundle );
        intent.putExtra( "cracha", SenhaActivity.crachaBundle );

        startActivity( intent );
        finish();

    }
}
