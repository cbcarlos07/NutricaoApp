package ham.org.br.nutricao.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by carlos.bruno on 14/08/2017.
 */

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private final String NOME_FILE = "nutricao.preferencias";
    private final String NOME_USUARIO = "nomeLogado";
    private final String NR_CRACHA = "crachaLogado";
    private final String EMAIL_USER = "emailLogado";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

   public Preferences (Context c){
       context = c;
       preferences = context.getSharedPreferences( NOME_FILE, MODE );
       editor = preferences.edit();
   }

   public  void salvarDados( String user, String cracha, String email ){

       editor.putString( NOME_USUARIO, user );
       editor.putString( NR_CRACHA, cracha );
       editor.putString( EMAIL_USER, email );
       editor.commit();
   }

   public String getNome(){
       return preferences.getString( NOME_USUARIO, null );
   }

   public String getCracha(){
       return preferences.getString( NR_CRACHA, null );
   }

    public String getEmail(){
        return preferences.getString( EMAIL_USER, null );
    }






}
