package cu.entumovil.snb.core.models;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {

    private long id;

    private String Tit;

    private Date Fecha;

    private String res;

    private String Not;

    private String foto;

    private boolean pictureLoaded;

    public News() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTit() {
        return Tit;
    }

    public void setTit(String tit) {
        Tit = tit;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getNot() {
        return Not;
    }

    public void setNot(String not) {
        Not = not;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isPictureLoaded() {
        return pictureLoaded;
    }

    public void setPictureLoaded(boolean pictureLoaded) {
        this.pictureLoaded = pictureLoaded;
    }
}
