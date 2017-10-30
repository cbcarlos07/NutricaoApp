package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import ham.org.br.nutricao.database.ScriptSQL;
import ham.org.br.nutricao.model.Cardapio;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class RepositorioCardapio {
    private SQLiteDatabase conn;
    public RepositorioCardapio (SQLiteDatabase conexao){
        this.conn = conexao;
    }

    public void addCardapio (Cardapio obj ){

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.CARDAPIO_ID, obj.getCodigo() );
        values.put( ScriptSQL.CARDAPIO_DATA, obj.getData() );

        conn.insert( ScriptSQL.CARDAPIO_TABLE, null, values );
        conn.close();
    }

    public void excluir( int id ){

        conn.delete(ScriptSQL.CARDAPIO_TABLE, ScriptSQL.CARDAPIO_ID+" = ?", new String[] { String.valueOf( id ) });
    }

    public void excluirTudo(){
        conn.delete(ScriptSQL.CARDAPIO_TABLE, null, null);
        conn.close();
    }
}
