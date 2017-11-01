package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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

    public long addCardapio (Cardapio obj ){

        ContentValues values = new ContentValues();
        values.put( ScriptSQL.CARDAPIO_ID, obj.getCodigo() );
        values.put( ScriptSQL.CARDAPIO_TIPO, obj.getTipo() );
        values.put( ScriptSQL.CARDAPIO_DATA, obj.getData() );

        long teste = conn.insertOrThrow( ScriptSQL.CARDAPIO_TABLE, null, values );
        conn.close();
        return teste;
    }

    public void excluir( int id ){

        conn.delete(ScriptSQL.CARDAPIO_TABLE, ScriptSQL.CARDAPIO_ID+" = ?", new String[] { String.valueOf( id ) });
        conn.close();
    }

    public void excluirTudo(){
        conn.delete(ScriptSQL.CARDAPIO_TABLE, null, null);
        conn.close();
    }

    public int getCdCardapio( String data, int tipo  ){
        int total = 0;
        try{
            Cursor cursor = conn.query( ScriptSQL.CARDAPIO_TABLE, null, ScriptSQL.CARDAPIO_DATA+" = ? AND "+ScriptSQL.CARDAPIO_TIPO+" = ?", new String[]{ data, String.valueOf( tipo ) }, null, null, null );

            int coluna = cursor.getColumnIndex( ScriptSQL.CARDAPIO_ID );
            cursor.moveToFirst();
            if( cursor.moveToNext() ){

                total = cursor.getInt( coluna );
                Log.d("53.LodRCCardCod",""+total);
            }
            cursor.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }

        return total;

    }

    public void listarCardapios(){
        Cursor cursor = conn.query( ScriptSQL.CARDAPIO_TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        int col1 = cursor.getColumnIndex( ScriptSQL.CARDAPIO_ID );
        int col2 = cursor.getColumnIndex( ScriptSQL.CARDAPIO_TIPO );
        int col3 = cursor.getColumnIndex( ScriptSQL.CARDAPIO_DATA );
        while (cursor.moveToNext()){
            Log.d("67.LodRCarId", ""+cursor.getInt( col1 ));
            Log.d("68.LodRCarTipo", ""+cursor.getInt( col2 ));
            Log.d("69.LodRCarData", ""+cursor.getString( col3 ));
        }
        cursor.close();

    }

    public String getDataCardapio( int id ){
        Log.d("LodRCGetDataCard", "GetdataCardapio");
        Cursor cursor = conn.query( ScriptSQL.CARDAPIO_TABLE, null, ScriptSQL.CARDAPIO_ID+" = ? ", new String[]{ String.valueOf( id ) }, null, null, null );
        String data = "";
        String tipo = "";
        int cod =   0;
        int dataInicio = cursor.getColumnIndex( ScriptSQL.CARDAPIO_DATA );
        int typo = cursor.getColumnIndex( ScriptSQL.CARDAPIO_TIPO );
        Log.d("LodRCardCursor", ""+cursor.moveToNext());
        cursor.moveToFirst();
        if( cursor.moveToNext() ){
            data = cursor.getString( dataInicio );
            tipo = cursor.getString( typo );
            Log.d("LodRCGetCard", data);
            Log.d("LodRCGetTipo", tipo);

        }else{
            Log.d("LodRecData", "Nao achou nada");
        }
        cursor.close();
        return data;

    }
}
