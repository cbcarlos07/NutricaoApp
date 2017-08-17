package ham.org.br.nutricao.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import ham.org.br.nutricao.helper.Preferences;
import ham.org.br.nutricao.util.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    public static Activity mainActivity;
    public static boolean mainAtivo = false;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Nutrição" );
        setSupportActionBar( toolbar );
        mainActivity = this;

        slidingTabLayout = ( SlidingTabLayout ) findViewById( R.id.slt_tabs );
        viewPager        = ( ViewPager ) findViewById( R.id.vp_pagina );

        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager(), this );
        viewPager.setAdapter( tabAdapter );

        slidingTabLayout.setCustomTabView( R.layout.tab_view, R.id.text_item_tab );
        slidingTabLayout.setDistributeEvenly( true );
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor( this, R.color.colorTabIndicator ));
        slidingTabLayout.setViewPager( viewPager );







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

    }
}
