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
