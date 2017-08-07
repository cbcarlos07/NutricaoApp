package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 31/07/2017.
 */


import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TipoPrato {

        @SerializedName("tipoprato")

        private String tipoprato;

        @SerializedName("pratos")

        private ArrayList<Prato> pratoList = new ArrayList<>();


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
