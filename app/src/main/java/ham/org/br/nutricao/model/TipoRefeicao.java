package ham.org.br.nutricao.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos.bruno on 03/08/2017.
 */

public class TipoRefeicao {
    @SerializedName("codigo")
    private int codigo;
    @SerializedName("tipo")
    private String descricao;
    @SerializedName("cancelamento")
    private int cancelamento;
    @SerializedName("inicio")
    private int inicio;
    @SerializedName("hrInicio")
    private String hrInicio;

    public String getHrInicio() {
        return hrInicio;
    }

    public void setHrInicio(String hrInicio) {
        this.hrInicio = hrInicio;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(int cancelamento) {
        this.cancelamento = cancelamento;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String toString(){
        return descricao;
    }
}
