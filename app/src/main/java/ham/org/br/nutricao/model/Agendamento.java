package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 09/08/2017.
 */
import com.google.gson.annotations.SerializedName;
public class Agendamento {

    @SerializedName("cardapio")

    private String cardapio;
    @SerializedName("data")

    private String data;
    @SerializedName("tipo")

    private String tipo;

    public String getCardapio() {
        return cardapio;
    }

    public void setCardapio(String cardapio) {
        this.cardapio = cardapio;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
