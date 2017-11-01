package ham.org.br.nutricao.database;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class ScriptSQL {
    public static final String TIPO_REFEICAO_TABLE        = "tipo_refeicao";
    public static final String TIPO_REFEICAO_ID           = "id";
    public static final String TIPO_REFEICAO_DSTIPO       = "dstipo";
    public static final String TIPO_REFEICAO_CANCELAMENTO = "ds_cancelamento";
    public static final String TIPO_REFEICAO_INICIO       = "ds_inicio";
    public static final String TIPO_REFEICAO_HRINICIO     = "hr_inicio";
    public static final String AGENDAMENTO_TABLE          = "agendamento";
    public static final String AGENDAMENTO_ID             = "id";
    public static final String AGENDAMENTO_CARDAPIO       = "carpapio";
    public static final String AGENDAMENTO_TIPO           = "tipo";
    public static final String AGENDAMENTO_CDTIPO         = "cdtipo";
    public static final String AGENDAMENTO_DATA           = "data";
    public static final String AGENDAMENTO_CRACHA         = "cracha";
    public static final String CARDAPIO_TABLE             = "cardapio";
    public static final String CARDAPIO_ID                = "id";
    public static final String CARDAPIO_DATA              = "data";
    public static final String TIPO_PRATO_TABLE           = "tipo_prato";
    public static final String TIPO_PRATO_ID              = "tipo_prato";
    public static final String TIPO_PRATO_DSTIPO          = "dstipo";
    public static final String PRATO_TABLE                = "prato";
    public static final String PRATO_ID                   = "id";
    public static final String PRATO_DSPRATO              = "dsprato";
    public static final String PRATO_DSINGREDIENTE        = "ingrediente";
    public static final String PRATO_TIPO_PRATO           = "tipo_prato";
    public static final String PRATO_CARDAPIO             = "cardapio";

    public static String getCreateTipoRefeicao(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS "+TIPO_REFEICAO_TABLE+"( ");
        sqlBuilder.append(TIPO_REFEICAO_ID+"     INTEGER, " );
        sqlBuilder.append(TIPO_REFEICAO_DSTIPO+" VARCHAR(255), ");
        sqlBuilder.append(TIPO_REFEICAO_CANCELAMENTO+ " VARCHAR(255)," );
        sqlBuilder.append(TIPO_REFEICAO_INICIO+ " INTEGER," );
        sqlBuilder.append(TIPO_REFEICAO_HRINICIO+ " VARCHAR(5)" );
        sqlBuilder.append(" ) ;");
        return sqlBuilder.toString();
    }

    public static String getCreateAgendamento(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS "+AGENDAMENTO_TABLE+"( ");
        sqlBuilder.append(AGENDAMENTO_ID+"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " );
        sqlBuilder.append(AGENDAMENTO_CARDAPIO+" INTEGER, ");
        sqlBuilder.append(AGENDAMENTO_TIPO+"     VARCHAR(255), ");
        sqlBuilder.append(AGENDAMENTO_CDTIPO+"   INTEGER, ");
        sqlBuilder.append(AGENDAMENTO_DATA+"     DATE, ");
        sqlBuilder.append(AGENDAMENTO_CRACHA+"   VARCHAR(255) ");
        sqlBuilder.append(" ) ;");
        return sqlBuilder.toString();
    }

    public static String getCreateCardapio(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS "+CARDAPIO_TABLE+"( ");
        sqlBuilder.append(CARDAPIO_ID+" INTEGER, " );
        sqlBuilder.append(CARDAPIO_DATA+" VARCHAR(255) ");
        sqlBuilder.append(" ) ;");
        return sqlBuilder.toString();
    }

    public static String getCreateTipoPrato(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS "+TIPO_PRATO_TABLE+"( ");
        sqlBuilder.append(TIPO_PRATO_ID+" INTEGER, " );
        sqlBuilder.append(TIPO_PRATO_DSTIPO+" VARCHAR(255) ");
        sqlBuilder.append(" ) ;");
        return sqlBuilder.toString();
    }

    public static String getCreatePrato(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS "+PRATO_TABLE+"( ");
        sqlBuilder.append(PRATO_ID+"            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " );
        sqlBuilder.append(PRATO_DSPRATO+"       VARCHAR(255), ");
        sqlBuilder.append(PRATO_DSINGREDIENTE+" VARCHAR(255),  ");
        sqlBuilder.append(PRATO_TIPO_PRATO+"    INTEGER, ");
        sqlBuilder.append(PRATO_CARDAPIO+"      INTEGER, ");
        sqlBuilder.append("FOREIGN KEY ("+PRATO_TIPO_PRATO+") REFERENCES "+TIPO_PRATO_TABLE+"("+TIPO_PRATO_ID+")");
        sqlBuilder.append("FOREIGN KEY ("+PRATO_CARDAPIO+") REFERENCES "+TIPO_PRATO_TABLE+"("+PRATO_ID+")");
        sqlBuilder.append(" ) ;");
        return sqlBuilder.toString();
    }
}
