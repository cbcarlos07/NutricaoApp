package ham.org.br.nutricao.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import ham.org.br.nutricao.R;

public class PerfilActivity extends AppCompatActivity {
    private TextView tvNome;
    private TextView tvCracha;
    private TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvNome = (TextView) findViewById( R.id.tvNome );
        tvCracha = (TextView) findViewById( R.id.tvCracha );
        tvEmail = (TextView) findViewById( R.id.tvEmail );

        tvNome.setText(SenhaActivity.nomeBundle );
        tvCracha.setText(SenhaActivity.crachaBundle );
        tvEmail.setText(SenhaActivity.emailBundle );

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
