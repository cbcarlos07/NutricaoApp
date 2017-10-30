package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ham.org.br.nutricao.database.ScriptSQL;
import ham.org.br.nutricao.model.Prato;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class RepositorioPrato {

    private SQLiteDatabase conn;
    public RepositorioPrato( SQLiteDatabase conexao ){
        this.conn = conexao;
    }

    public void addPrato (Prato obj ){

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.PRATO_ID, obj.getPrato() );
        values.put( ScriptSQL.PRATO_DSPRATO, obj.getPrato() );
        values.put( ScriptSQL.PRATO_DSINGREDIENTE, obj.getIngrediente() );
        values.put( ScriptSQL.PRATO_TIPO_PRATO, obj.getTipoprato());
        values.put( ScriptSQL.PRATO_CARDAPIO, obj.getCardapio());

        conn.insert( ScriptSQL.PRATO_TABLE, null, values );
        conn.close();
    }

    public List<Prato> getPratos( int id ){
        List<Prato> pratoList = new ArrayList<Prato>();
        Cursor cursor = conn.query( ScriptSQL.PRATO_TABLE, null, ScriptSQL.PRATO_ID, new String[]{ String.valueOf( id ) }, null, null,  null );
        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            Prato prato = new Prato();
            prato.setId( cursor.getInt( 1 ) );
            prato.setPrato( cursor.getString( 2 ) );
            prato.setIngrediente( cursor.getString( 3 ) );
            prato.setTipoprato( cursor.getInt( 4 ) );
            prato.setCardapio( cursor.getInt( 5 ) );
            pratoList.add( prato );
        }
        cursor.close();
        return pratoList;
    }

}
