package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 31/07/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prato {


    private int id;
    private String prato;
    private String ingrediente;
    private int tipoprato;
    private int cardapio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoprato() {
        return tipoprato;
    }

    public void setTipoprato(int tipoprato) {
        this.tipoprato = tipoprato;
    }

    public int getCardapio() {
        return cardapio;
    }

    public void setCardapio(int cardapio) {
        this.cardapio = cardapio;
    }

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