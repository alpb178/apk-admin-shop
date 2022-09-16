package cu.entumovil.snb.core.models;

public class History {

    private String id;

    private String Tit;

    private String Desc;

    private String foto;

    public History() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTit() {
        return Tit;
    }

    public void setTit(String tit) {
        Tit = tit;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
