package cu.entumovil.snb.core.models;

public class Qualification {

    private Long id;

    private String Sigla;

    private String Equipo;

    private int JJ;

    private int JG;

    private int JE;

    private int JP;

    private String PRO;

    public Qualification() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigla() {
        return Sigla;
    }

    public void setSigla(String sigla) {
        this.Sigla = sigla;
    }

    public String getEquipo() {
        return Equipo;
    }

    public void setEquipo(String Equipo) {
        this.Equipo = Equipo;
    }

    public int getJJ() {
        return JJ;
    }

    public void setJJ(int JJ) {
        this.JJ = JJ;
    }

    public int getJG() {
        return JG;
    }

    public void setJG(int JG) {
        this.JG = JG;
    }

    public int getJE() {
        return JE;
    }

    public void setJE(int JE) {
        this.JE = JE;
    }

    public int getJP() {
        return JP;
    }

    public void setJP(int JP) {
        this.JP = JP;
    }

    public String getPRO() {
        return PRO;
    }

    public void setPRO(String PRO) {
        this.PRO = PRO;
    }
}
