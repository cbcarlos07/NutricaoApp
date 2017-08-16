package ham.org.br.nutricao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.RetornoMensagem;
import ham.org.br.nutricao.service.ServiceAPI;
import ham.org.br.nutricao.util.GeradorDeSenha;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EsqueceuActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_msg_esqueceu;
    private ProgressBar progressBar;
    private Button btn_voltar_esqueceu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu);

        tv_msg_esqueceu = ( TextView ) findViewById( R.id.tv_msg_esqueceu );
        progressBar     = ( ProgressBar ) findViewById( R.id.progressBarEsqueceu );
        btn_voltar_esqueceu = ( Button ) findViewById( R.id.btn_voltar_esqueceu );
        progressBar.setVisibility( View.INVISIBLE );

        btn_voltar_esqueceu.setOnClickListener( this );






    }







    @Override
    public void onClick(View view) {

        Intent intent = new Intent( this, CrachaActivity.class );
        startActivity( intent );
        finish();


    }
}
