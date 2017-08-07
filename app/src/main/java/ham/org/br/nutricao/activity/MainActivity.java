package ham.org.br.nutricao.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.TipoRefeicao;
import ham.org.br.nutricao.model.TiposRefeicao;
import ham.org.br.nutricao.service.Service;
import ham.org.br.nutricao.util.SlidingTabLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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



    }


}
