package ham.org.br.nutricao.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        ContentValues values = new ContentValues();
        values.put( ScriptSQL.TIPO_REFEICAO_ID, tipoRefeicao.getCodigo() );
        values.put( ScriptSQL.TIPO_REFEICAO_DSTIPO, tipoRefeicao.getDescricao() );

        conn.insert( ScriptSQL.TIPO_REFEICAO_TABLE, null, values );
        conn.close();
    }

    public ArrayList<TipoRefeicao> getAllTipoRefeicao(Context context){
        ArrayList<TipoRefeicao> objList = new ArrayList<TipoRefeicao>();
        Cursor cursor = conn.query(ScriptSQL.TIPO_REFEICAO_TABLE, null, null, null,null, null, null);

        if( cursor.getCount() > 0 ){
            cursor.moveToFirst();
            do{
                TipoRefeicao obj = new TipoRefeicao();
                obj.setCodigo( cursor.getInt( 1 ) );
                obj.setDescricao( cursor.getString( 2 ) );
                objList.add( obj );
            }while ( cursor.moveToNext() );
        }
        conn.close();
        return objList;
    }

    public int getTipoRefeicaoCount(){
        String sql = "SELECT * FROM "+ScriptSQL.TIPO_REFEICAO_TABLE;

        Cursor cursor = conn.rawQuery( sql, null );
        cursor.close();
        return cursor.getCount();
    }



}
