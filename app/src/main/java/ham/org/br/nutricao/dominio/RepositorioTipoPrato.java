package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ham.org.br.nutricao.database.ScriptSQL;
import ham.org.br.nutricao.model.TipoPrato;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class RepositorioTipoPrato {

    private SQLiteDatabase conn;

    public RepositorioTipoPrato(SQLiteDatabase conexao){
        this.conn = conexao;
    }

    public void addTipoPrato (TipoPrato obj ){

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.TIPO_PRATO_ID, obj.getCdtipoprato() );
        values.put( ScriptSQL.TIPO_PRATO_DSTIPO, obj.getTipoprato() );

        conn.insert( ScriptSQL.TIPO_PRATO_TABLE, null, values );
        conn.close();
    }

    public void excluirTudo(){
        conn.delete( ScriptSQL.TIPO_PRATO_TABLE, null,  null );
        conn.close();
    }

    public String getTipoPrato( int id ){

        Cursor cursor = conn.query( ScriptSQL.TIPO_PRATO_TABLE, null, ScriptSQL.TIPO_PRATO_ID+" = ?", new String[]{ String.valueOf( id ) }, null, null, null );
        String tipoPrato = "";
        int coluna = cursor.getColumnIndex( ScriptSQL.TIPO_PRATO_DSTIPO );
        if( cursor != null ){
            cursor.moveToFirst();
            tipoPrato = cursor.getString( coluna );
        }
        cursor.close();
        return tipoPrato;

    }
}
