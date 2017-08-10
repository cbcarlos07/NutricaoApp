package ham.org.br.nutricao.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.adapter.TabAdapter;
import ham.org.br.nutricao.util.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Nutrição" );
        setSupportActionBar( toolbar );


        slidingTabLayout = ( SlidingTabLayout ) findViewById( R.id.slt_tabs );
        viewPager        = ( ViewPager ) findViewById( R.id.vp_pagina );

        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager(), this );
        viewPager.setAdapter( tabAdapter );

        slidingTabLayout.setCustomTabView( R.layout.tab_view, R.id.text_item_tab );
        slidingTabLayout.setDistributeEvenly( true );
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor( this, R.color.colorIcone ));
        slidingTabLayout.setViewPager( viewPager );


    }


}
