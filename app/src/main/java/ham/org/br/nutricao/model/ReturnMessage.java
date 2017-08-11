package ham.org.br.nutricao.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by carlos.bruno on 11/08/2017.
 */

public class ReturnMessage {

    @SerializedName("mensagem")
    private RetornoMensagem retornoMensagem;

    public RetornoMensagem getRetornoMensagem() {
        return retornoMensagem;
    }

    public void setRetornoMensagem(RetornoMensagem retornoMensagem) {
        this.retornoMensagem = retornoMensagem;
    }
}
