package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 31/07/2017.
 */


import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TipoPrato {

        @SerializedName("tipoprato")

        private String tipoprato;

        @SerializedName("cd_cardapio")
        private int cardapio;

        @SerializedName("cd_tipo_prato")
        private int cdtipoprato;


        @SerializedName("pratos")

        private ArrayList<Prato> pratoList = new ArrayList<>();

    public int getCdtipoprato() {
        return cdtipoprato;
    }

    public void setCdtipoprato(int cdtipoprato) {
        this.cdtipoprato = cdtipoprato;
    }

    public int getCardapio() {
        return cardapio;
    }

    public void setCardapio(int cardapio) {
        this.cardapio = cardapio;
    }

    public String getTipoprato() {
            return tipoprato;
        }

        public void setTipoprato(String tipoprato) {
            this.tipoprato = tipoprato;
        }

        public ArrayList<Prato> getPratoList() {
            return pratoList;
        }

        public void setPratoList(ArrayList<Prato> pratoList) {
            this.pratoList = pratoList;
        }

        public String toString(){
                return tipoprato;
            }

}
