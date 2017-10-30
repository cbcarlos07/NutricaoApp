package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 09/08/2017.
 */
import com.google.gson.annotations.SerializedName;
public class Agendamento {
    private int id;



    @SerializedName("cardapio")

    private int cardapio;
    @SerializedName("data")

    private String data;
    @SerializedName("tipo")

    private String tipo;
    @SerializedName("cdtipo")

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int cdTipo;

    public int getCdTipo() {
        return cdTipo;
    }

    public void setCdTipo(int cdTipo) {
        this.cdTipo = cdTipo;
    }

    public int getCardapio() {
        return cardapio;
    }

    public void setCardapio(int cardapio) {
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
