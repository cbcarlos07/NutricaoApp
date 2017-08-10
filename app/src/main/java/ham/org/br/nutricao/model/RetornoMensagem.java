package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 09/08/2017.
 */
import com.google.gson.annotations.SerializedName;
public class RetornoMensagem {


    @SerializedName("success")

    private Integer success;
    @SerializedName("email")

    private String email;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
