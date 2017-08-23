package ham.org.br.nutricao.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by carlos.bruno on 23/08/2017.
 */

public class Message {


    @SerializedName("publicado")

    private String publicado;
    @SerializedName("motivo")

    private String motivo;
    @SerializedName("cardapio")

    private int cardapio;
    @SerializedName("acao")

    private String acao;
    @SerializedName("tipo_prato")

    private List<TipoPrato> tipoPrato = null;

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getCardapio() {
        return cardapio;
    }

    public void setCardapio(int cardapio) {
        this.cardapio = cardapio;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public List<TipoPrato> getTipoPrato() {
        return tipoPrato;
    }

    public void setTipoPrato(List<TipoPrato> tipoPrato) {
        this.tipoPrato = tipoPrato;
    }

}
