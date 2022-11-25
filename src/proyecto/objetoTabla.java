package proyecto;

public class objetoTabla {
    String var,valor;
    Tokens tipo;
    //VAR NOMBRE DE LA VARIABLE.
    //VALOR ES LA CANTIDAD O EL DATO.
    //TIPO ES EL TIPO DE DATO.
    public objetoTabla(String var, String valor, Tokens tipo) {
        this.var = var;
        this.valor = valor;
        this.tipo = tipo;
    }

    public objetoTabla(String var, Tokens tipo) {
        this.var = var;
        this.tipo = tipo;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Tokens getTipo() {
        return tipo;
    }

    public void setTipo(Tokens tipo) {
        this.tipo = tipo;
    }
    
    
}
