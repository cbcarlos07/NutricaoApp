package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    public void addAgendamento (Agendamento obj ){

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.AGENDAMENTO_CARDAPIO, obj.getCardapio() );
        values.put( ScriptSQL.AGENDAMENTO_CDTIPO, obj.getCdTipo() );
        values.put( ScriptSQL.AGENDAMENTO_TIPO, obj.getTipo() );
        values.put( ScriptSQL.AGENDAMENTO_DATA, obj.getData() );

        conn.insertOrThrow( ScriptSQL.AGENDAMENTO_TABLE, null, values );
        conn.close();
    }

    public List<Agendamento> getAllAgendamento(Context context){
        List<Agendamento> agendamentoList = new ArrayList<Agendamento>();
        Cursor cursor = conn.query(ScriptSQL.AGENDAMENTO_TABLE, null, null, null,null, null, null);

        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            do{
                Agendamento agendamento = new Agendamento();
                agendamento.setCardapio( cursor.getInt( 2 ) );
                agendamento.setCardapio( cursor.getInt( 2 ) );
                agendamento.setTipo( cursor.getString( 3 ) );
                agendamento.setCdTipo( cursor.getInt( 4 ) );
                agendamento.setData( cursor.getString( 5 ) );
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
        cursor.close();
        return cursor.getCount();
    }
}
