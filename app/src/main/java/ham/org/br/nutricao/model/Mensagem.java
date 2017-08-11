package ham.org.br.nutricao.model;

import java.util.List;

/**
 * Created by carlos.bruno on 07/08/2017.
 */


import com.google.gson.annotations.SerializedName;

public class Mensagem {

    @SerializedName("publicado")

    private String publicado;
    @SerializedName("tempoRestante")

    private String tempoRestante;
    @SerializedName("motivo")

    private String motivo;
    @SerializedName("alerta")

    private String alerta;
    @SerializedName("agendar")

    private String agendar;
    @SerializedName("cancelar")

    private String cancelar;

    @SerializedName("acao")


    private String acao;

    @SerializedName("cardapio")

    private int cardapio;

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

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
    }

    public String getTempoRestante() {
        return tempoRestante;
    }

    public void setTempoRestante(String tempoRestante) {
        this.tempoRestante = tempoRestante;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public String getAgendar() {
        return agendar;
    }

    public void setAgendar(String agendar) {
        this.agendar = agendar;
    }

    public String getCancelar() {
        return cancelar;
    }

    public void setCancelar(String cancelar) {
        this.cancelar = cancelar;
    }

}