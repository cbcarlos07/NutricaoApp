package ham.org.br.nutricao.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos.bruno on 14/08/2017.
 */

public class CrachaValida {

    @SerializedName("success")

    private Integer success;
    @SerializedName("ativo")

    private Integer ativo;
    @SerializedName("nome")

    private String nome;
    @SerializedName("email")

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
