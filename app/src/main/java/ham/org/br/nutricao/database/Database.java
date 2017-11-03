package ham.org.br.nutricao.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nutricao.db";
    private static final int DATABASE_VERSION = 4;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL( ScriptSQL.getCreateAgendamento() );
            sqLiteDatabase.execSQL( ScriptSQL.getCreateTipoRefeicao() );
            sqLiteDatabase.execSQL( ScriptSQL.getCreateCardapio() );
            sqLiteDatabase.execSQL( ScriptSQL.getCreateTipoPrato() );
            sqLiteDatabase.execSQL( ScriptSQL.getCreatePrato() );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS "+ ScriptSQL.TIPO_REFEICAO_TABLE  );
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS "+ ScriptSQL.AGENDAMENTO_TABLE  );
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS "+ ScriptSQL.CARDAPIO_TABLE  );
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS "+ ScriptSQL.TIPO_PRATO_TABLE  );
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS "+ ScriptSQL.PRATO_TABLE  );
            onCreate( sqLiteDatabase );
    }

}
