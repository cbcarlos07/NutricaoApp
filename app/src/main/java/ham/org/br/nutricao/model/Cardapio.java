package ham.org.br.nutricao.model;

/**
 * Created by carlos.bruno on 30/10/2017.
 */

public class Cardapio {

    private int codigo;
    private String data;
    private int tipo;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
