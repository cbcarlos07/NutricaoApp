package ham.org.br.nutricao.service;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos.bruno on 01/08/2017.
 */

public class Permissao {
    public static boolean validaPermissoes (int requestCode, Activity activity, String[] permissoes){
       // Log.i("Permissao","classe permissao");
        if(Build.VERSION.SDK_INT >= 23 ){
            List<String> listaPermissoes = new ArrayList<String>();
            /*
            Percorre as permissoes passadas, verificando uma a uma
            se já tem a permissão liberada
             */
            for(String permissao : permissoes){
                //Log.i("Permissao","permissao: "+permissao);

                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if( !validaPermissao ){
                  //  Log.i("Permissao","Permissao negada");
                    listaPermissoes.add(permissao);
                }

                /* Caso a lista esteja vazia, não é necessário solicitar permissao*/
                if( listaPermissoes.isEmpty() ) {
                 //   Log.i("Permissao","permissao garantida");
                    return true;
                }

                String[] novasPermissoes = new String[ listaPermissoes.size() ];
                listaPermissoes.toArray( novasPermissoes );

                //Solicita  permissao
                ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
            }

        }

        return true;
    }


}
