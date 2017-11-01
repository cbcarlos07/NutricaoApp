package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ham.org.br.nutricao.activity.SenhaActivity;
import ham.org.br.nutricao.database.Database;
import ham.org.br.nutricao.database.ScriptSQL;
import ham.org.br.nutricao.model.Agendamento;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class RepositorioAgendamento {
    private SQLiteDatabase conn;
    private SQLiteOpenHelper db;
    public RepositorioAgendamento( SQLiteDatabase conexao ){
        this.conn = conexao;
    }

    public long addAgendamento (Agendamento obj ){
       // Log.d("addinsert","Para inserir");

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.AGENDAMENTO_CARDAPIO, obj.getCardapio() );
        values.put( ScriptSQL.AGENDAMENTO_CDTIPO, obj.getCdTipo() );
        values.put( ScriptSQL.AGENDAMENTO_TIPO, obj.getTipo() );
        values.put( ScriptSQL.AGENDAMENTO_DATA, obj.getData() );
        values.put( ScriptSQL.AGENDAMENTO_CRACHA, SenhaActivity.crachaBundle);

        long teste = conn.insertOrThrow( ScriptSQL.AGENDAMENTO_TABLE, null, values );
        Log.d("insert","Inserido com sucesso res: "+teste);
        conn.close();
        return teste;
    }

    public ArrayList<Agendamento> getAllAgendamento(){
        ArrayList<Agendamento> agendamentoList = new ArrayList<Agendamento>();
        String cracha = SenhaActivity.crachaBundle;
        Cursor cursor = conn.rawQuery(
                                "SELECT * FROM "+
                                ScriptSQL.AGENDAMENTO_TABLE+"  WHERE "+
                                ScriptSQL.AGENDAMENTO_CRACHA+" = ? AND "+ScriptSQL.AGENDAMENTO_DATA+" >= strftime('%d/%m/%Y', 'now')",
                                new String[]{ cracha  } );
        int colCardapio = cursor.getColumnIndex( ScriptSQL.AGENDAMENTO_CARDAPIO );
        int colTipo = cursor.getColumnIndex( ScriptSQL.AGENDAMENTO_TIPO );
        int colCdTipo = cursor.getColumnIndex( ScriptSQL.AGENDAMENTO_CDTIPO );
        int colData = cursor.getColumnIndex( ScriptSQL.AGENDAMENTO_DATA );
        //int colDate = cursor.getColumnIndex( "agora" );
        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            do{
                Agendamento agendamento = new Agendamento();
                agendamento.setCardapio( cursor.getInt( colCardapio ) );
                agendamento.setTipo( cursor.getString( colTipo ) );
             //   Log.d("LodRAgdCdTipoSQlite",""+cursor.getInt( colCdTipo ));
                Log.d("LodRAgdDataSQlite",""+cursor.getString( colData ));
          //      Log.d("LodRAgdAgoraSQlite",""+cursor.getString( colDate ));
                agendamento.setCdTipo( cursor.getInt( colCdTipo ) );
                agendamento.setData( cursor.getString( colData ) );
                agendamentoList.add( agendamento );
            }while ( cursor.moveToNext() );

            cursor.close();
        }
        return agendamentoList;

    }

    public void excluirTudo(){
        conn.delete(ScriptSQL.AGENDAMENTO_TABLE, null, null);
        conn.close();
    }

    public int getAgendamentoCount(  ){
        String sql = "SELECT * FROM "+ScriptSQL.AGENDAMENTO_TABLE;
        Cursor cursor = conn.rawQuery( sql, null );
        int total = cursor.getCount();
        cursor.close();
        return total;
    }
}
