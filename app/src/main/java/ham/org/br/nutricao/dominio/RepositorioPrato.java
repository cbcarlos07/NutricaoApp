package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public long addPrato (Prato obj ){

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.PRATO_DSPRATO, obj.getPrato() );
        values.put( ScriptSQL.PRATO_DSINGREDIENTE, obj.getIngrediente() );
        values.put( ScriptSQL.PRATO_TIPO_PRATO, obj.getTipoprato());
        values.put( ScriptSQL.PRATO_CARDAPIO, obj.getCardapio());

        long teste = conn.insertOrThrow( ScriptSQL.PRATO_TABLE, null, values );
        conn.close();
        return teste;
    }

    public List<Prato> getTiposPratos( int id ){
        List<Prato> pratoList = new ArrayList<Prato>();
        Cursor cursor = conn.query( ScriptSQL.PRATO_TABLE, null, ScriptSQL.PRATO_CARDAPIO+" = ?", new String[]{ String.valueOf( id ) }, ScriptSQL.PRATO_TIPO_PRATO, null,  null );
        int colCod = cursor.getColumnIndex( ScriptSQL.PRATO_ID );
        int colPrato = cursor.getColumnIndex( ScriptSQL.PRATO_DSPRATO );
        int colIngred = cursor.getColumnIndex( ScriptSQL.PRATO_DSINGREDIENTE );
        int colTpPrato = cursor.getColumnIndex( ScriptSQL.PRATO_TIPO_PRATO );
        int colCardapi = cursor.getColumnIndex( ScriptSQL.PRATO_CARDAPIO );
        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                Prato prato = new Prato();
                prato.setId( cursor.getInt( colCod ) );
                prato.setPrato( cursor.getString( colPrato ) );
                prato.setIngrediente( cursor.getString( colIngred ) );
                prato.setTipoprato( cursor.getInt( colTpPrato ) );
                prato.setCardapio( cursor.getInt( colCardapi ) );
                pratoList.add( prato );
            }


        }
        cursor.close();
        return pratoList;
    }

    public void getListPratos( int id ){
        List<Prato> pratoList = new ArrayList<Prato>();
        Cursor cursor = conn.query( ScriptSQL.PRATO_TABLE, null, ScriptSQL.PRATO_CARDAPIO+" = ?", new String[]{ String.valueOf( id ) }, ScriptSQL.PRATO_TIPO_PRATO, null,  null );
        int colCod = cursor.getColumnIndex( ScriptSQL.PRATO_ID );
        int colPrato = cursor.getColumnIndex( ScriptSQL.PRATO_DSPRATO );
        int colIngred = cursor.getColumnIndex( ScriptSQL.PRATO_DSINGREDIENTE );
        int colTpPrato = cursor.getColumnIndex( ScriptSQL.PRATO_TIPO_PRATO );
        int colCardapi = cursor.getColumnIndex( ScriptSQL.PRATO_CARDAPIO );
        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                Prato prato = new Prato();
                prato.setId( cursor.getInt( colCod ) );
                prato.setPrato( cursor.getString( colPrato ) );
                Log.d("78.LodRPPrato",cursor.getString( colPrato ));
                prato.setIngrediente( cursor.getString( colIngred ) );
                prato.setTipoprato( cursor.getInt( colTpPrato ) );
                prato.setCardapio( cursor.getInt( colCardapi ) );
                pratoList.add( prato );
            }


        }
        cursor.close();

    }

    public List<Prato> getPratos( int id, int tipo ){
        List<Prato> pratoList = new ArrayList<Prato>();
        Cursor cursor = conn.query( ScriptSQL.PRATO_TABLE, null, ScriptSQL.PRATO_CARDAPIO+" = ? AND "+ScriptSQL.PRATO_TIPO_PRATO +" = ? " , new String[]{ String.valueOf( id ),String.valueOf( tipo )  }, ScriptSQL.PRATO_DSPRATO, null,  null );
        int colCod = cursor.getColumnIndex( ScriptSQL.PRATO_ID );
        int colPrato = cursor.getColumnIndex( ScriptSQL.PRATO_DSPRATO );
        int colIngred = cursor.getColumnIndex( ScriptSQL.PRATO_DSINGREDIENTE );
        int colTpPrato = cursor.getColumnIndex( ScriptSQL.PRATO_TIPO_PRATO );
        int colCardapi = cursor.getColumnIndex( ScriptSQL.PRATO_CARDAPIO );
        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                Prato prato = new Prato();
                prato.setId( cursor.getInt( colCod ) );
                prato.setPrato( cursor.getString( colPrato ) );
                Log.d("Pratos",cursor.getString( colPrato ));
                prato.setIngrediente( cursor.getString( colIngred ) );
                prato.setTipoprato( cursor.getInt( colTpPrato ) );
                prato.setCardapio( cursor.getInt( colCardapi ) );
                pratoList.add( prato );
            }


        }
        cursor.close();
        return pratoList;
    }

}
