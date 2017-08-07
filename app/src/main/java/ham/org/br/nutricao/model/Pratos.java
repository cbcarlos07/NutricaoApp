package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 31/07/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pratos {

    @SerializedName("pratos")
    private ArrayList<Prato> pratos;

    public ArrayList<Prato> getPratos() {
        return pratos;
    }

    public void setPratos(ArrayList<Prato> pratos) {
        this.pratos = pratos;
    }
}

