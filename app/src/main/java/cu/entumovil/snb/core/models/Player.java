package cu.entumovil.snb.core.models;

import java.io.Serializable;
import java.util.Date;

public class Player implements Serializable {

    private long id;

    private String Nombre;

    private String Sigla;

    private String AVE;

    private int Numero;

    private String Tira;

    private int Altura;

    private int Peso;

    private String Posicion;

    private Date Nacimiento;

    private String foto;

    public Player() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getSigla() {
        return Sigla;
    }

    public void setSigla(String sigla) {
        Sigla = sigla;
    }

    public String getAVE() {
        return AVE;
    }

    public void setAVE(String AVE) {
        this.AVE = AVE;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public String getTira() {
        return Tira;
    }

    public void setTira(String tira) {
        Tira = tira;
    }

    public int getAltura() {
        return Altura;
    }

    public void setAltura(int altura) {
        Altura = altura;
    }

    public int getPeso() {
        return Peso;
    }

    public void setPeso(int peso) {
        Peso = peso;
    }

    public String getPosicion() {
        return Posicion;
    }

    public void setPosicion(String posicion) {
        Posicion = posicion;
    }

    public Date getNacimiento() {
        return Nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        Nacimiento = nacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
