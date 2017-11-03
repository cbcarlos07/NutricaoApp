package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ham.org.br.nutricao.database.ScriptSQL;
import ham.org.br.nutricao.model.TipoRefeicao;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class RepositorioTipoRefeicao {
    private SQLiteDatabase conn;
    public RepositorioTipoRefeicao( SQLiteDatabase conexao ){
        this.conn = conexao;
    }

    public void addTipoRefeicao (TipoRefeicao tipoRefeicao ){
       // Log.d("addinsert", "Inserir!");
        ContentValues values = new ContentValues();
        Log.d("TipoRefeicao: ",""+tipoRefeicao.getHrInicio());
        values.put( ScriptSQL.TIPO_REFEICAO_ID, tipoRefeicao.getCodigo() );
        values.put( ScriptSQL.TIPO_REFEICAO_DSTIPO, tipoRefeicao.getDescricao() );
        values.put( ScriptSQL.TIPO_REFEICAO_CANCELAMENTO, tipoRefeicao.getCancelamento() );
        values.put( ScriptSQL.TIPO_REFEICAO_INICIO, tipoRefeicao.getInicio() );
        values.put( ScriptSQL.TIPO_REFEICAO_HRINICIO, tipoRefeicao.getHrInicio() );

        conn.insert( ScriptSQL.TIPO_REFEICAO_TABLE, null, values );
      //  Log.d("insert", "Tipo de Refeicao inserida com sucesso!");

        conn.close();
    }

    public ArrayList<TipoRefeicao> getAllTipoRefeicao(){
        ArrayList<TipoRefeicao> objList = new ArrayList<TipoRefeicao>();
        Cursor cursor = conn.query(ScriptSQL.TIPO_REFEICAO_TABLE, null, null, null,null, null, null);
        int colCodigo = cursor.getColumnIndex( ScriptSQL.TIPO_REFEICAO_ID );
        int colDescricao = cursor.getColumnIndex( ScriptSQL.TIPO_REFEICAO_DSTIPO );
        int colHrInicio = cursor.getColumnIndex( ScriptSQL.TIPO_REFEICAO_HRINICIO );
        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();

            do{
                TipoRefeicao obj = new TipoRefeicao();
                obj.setCodigo( cursor.getInt( colCodigo ) );
                obj.setDescricao( cursor.getString( colDescricao ) );
               // Log.d( "horaInicio", cursor.getString( colHrInicio ) );
                objList.add( obj );
            }while ( cursor.moveToNext() );
        }
        cursor.close();
        return objList;
    }

    public int getTipoRefeicaoCount(){
        String sql = "SELECT * FROM "+ScriptSQL.TIPO_REFEICAO_TABLE;

        Cursor cursor = conn.rawQuery( sql, null );
        int total = cursor.getCount();
        cursor.close();
        return total;
    }

    public TipoRefeicao getPrazos( int id ){

        Cursor cursor = conn.query( ScriptSQL.TIPO_REFEICAO_TABLE, null, ScriptSQL.TIPO_REFEICAO_ID+" = ?", new String[]{ String.valueOf( id ) }, null, null, null );
        TipoRefeicao tipoRefeicao = null;
        int hrInicio = cursor.getColumnIndex( ScriptSQL.TIPO_REFEICAO_HRINICIO );
        int cancelamento = cursor.getColumnIndex( ScriptSQL.TIPO_REFEICAO_CANCELAMENTO );
        if( cursor != null ){
            cursor.moveToFirst();
            tipoRefeicao = new TipoRefeicao();
            tipoRefeicao.setCancelamento( cursor.getInt( cancelamento ) );
            tipoRefeicao.setHrInicio( cursor.getString( hrInicio ) );

        }
        cursor.close();
        return tipoRefeicao;

    }

    public String getTipoRefeicao( int cod ){

        Cursor cursor = conn.query( ScriptSQL.TIPO_REFEICAO_TABLE, null, ScriptSQL.TIPO_REFEICAO_ID +" = ? ", new String[]{ String.valueOf( cod ) }, null, null, null, null );
        String tipo = "";
        int coluna = cursor.getColumnIndex( ScriptSQL.TIPO_REFEICAO_DSTIPO );
        if( cursor.moveToNext() ){
            tipo = cursor.getString( coluna );
        }
        return tipo;

    }



}
