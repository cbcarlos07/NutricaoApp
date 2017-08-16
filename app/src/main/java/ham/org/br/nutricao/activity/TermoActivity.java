package ham.org.br.nutricao.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ham.org.br.nutricao.R;

public class TermoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termo);

        toolbar   = ( Toolbar ) findViewById( R.id.toolbarTermo );

        toolbar.setNavigationIcon( R.drawable.ic_action_arrow_left );
        setTitle( getString( R.string.lbl_tela_termo ) );
        setSupportActionBar( toolbar );

    }
}
