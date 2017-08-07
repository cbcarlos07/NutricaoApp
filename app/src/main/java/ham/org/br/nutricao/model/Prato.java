package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 31/07/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prato {

    @SerializedName("prato")

    private String prato;
    private String ingrediente;

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getPrato() {
        return prato;
    }

    public void setPrato(String prato) {
        this.prato = prato;
    }

    public String toString(){
        return prato;
    }
}