package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class SerieLeader implements Serializable {

    private long id;
    String TIT;
    String NOMBRE;
    String SIG;
    String Valor;
    String Mas;
    private String foto;

    public SerieLeader() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTIT() {
        return TIT;
    }

    public void setTIT(String TIT) {
        this.TIT = TIT;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getSIG() {
        return SIG;
    }

    public void setSIG(String SIG) {
        this.SIG = SIG;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getMas() {
        return Mas;
    }

    public void setMas(String mas) {
        Mas = mas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
