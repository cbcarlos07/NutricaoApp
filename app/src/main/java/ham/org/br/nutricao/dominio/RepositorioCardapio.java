package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.database.Cursor;
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
        conn.close();
    }

    public void excluirTudo(){
        conn.delete(ScriptSQL.CARDAPIO_TABLE, null, null);
        conn.close();
    }

    public int getCardapioCount( int i  ){

        Cursor cursor = conn.query( ScriptSQL.CARDAPIO_TABLE, null, ScriptSQL.CARDAPIO_ID+" = ?", new String[]{ String.valueOf( i ) }, null, null, null );
        int total = 0;
        int coluna = cursor.getColumnIndex( ScriptSQL.CARDAPIO_DATA );
        if( cursor != null ){
            cursor.moveToFirst();
            total = 1;
        }
        cursor.close();
        return total;

    }

    public String getDataCardapio( int id ){

        Cursor cursor = conn.query( ScriptSQL.CARDAPIO_TABLE, null, ScriptSQL.CARDAPIO_ID+" = ?", new String[]{ String.valueOf( id ) }, null, null, null );
        String data = "";
        int hrInicio = cursor.getColumnIndex( ScriptSQL.CARDAPIO_DATA );
        if( cursor != null ){
            cursor.moveToFirst();
            data = cursor.getString( hrInicio );
        }
        cursor.close();
        return data;

    }
}
