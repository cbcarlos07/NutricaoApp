package ham.org.br.nutricao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ham.org.br.nutricao.R;
import ham.org.br.nutricao.model.TipoRefeicao;

public class RefeicaoActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<TipoRefeicao> adapter;
    static public List<TipoRefeicao> listaTipo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);
        final List<TipoRefeicao> tipos =  listaTipo;
        toolbar = (Toolbar) findViewById( R.id.toolbarRef );
        toolbar.setTitle( "Tipo de Refeição" );
        //toolbar.setNavigationIcon( R.drawable.ic_action_arrow_left );
        setSupportActionBar( toolbar );
        listaTipo = null;

        listView = (ListView) findViewById( R.id.lv_tipos_refeicoes );


        adapter = new ArrayAdapter<TipoRefeicao>(
           getApplicationContext(),
           R.layout.item_tipo_refeicao,
           R.id.tv_item_tipo_prato,
           tipos
        );

        listView.setAdapter( adapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TipoRefeicao tipoRefeicao = tipos.get( position );
                Intent dados = new Intent();
                dados.putExtra("codigo", tipoRefeicao.getCodigo());
                dados.putExtra("descricao", tipoRefeicao.getDescricao());
                setResult(2, dados);
                finish();

               // Toast.makeText(RefeicaoActivity.this, "Codigo da refeicao: "+tipoRefeicao.getCodigo(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
