package cu.entumovil.snb.core.models;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {

    private int idJ;

    private String Fecha;

    private String Hora;

    private String Estadio;

    private String VS;

    private String HC;

    private String Estado;

    private String CVS;

    private String HVS;

    private String EVS;

    private String NomGan;

    private String CHC;

    private String HHC;

    private String EHC;

    private String NomPerd;

    private Linescore linescore;

    public Game() { }

    public int getIdJ() {
        return idJ;
    }

    public void setIdJ(int idJ) {
        this.idJ = idJ;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getEstadio() {
        return Estadio;
    }

    public void setEstadio(String estadio) {
        Estadio = estadio;
    }

    public int getCVS() {
        if (CVS.equalsIgnoreCase(""))
                CVS=String.valueOf(0);
        return Integer.parseInt(CVS);
    }

    public String getVS() {
        return VS;
    }

    public void setVS(String VS) {
        this.VS = VS;
    }

    public String getHC() {
        return HC;
    }

    public void setHC(String HC) {
        this.HC = HC;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public void setCVS(int CVS) {
        this.CVS = String.valueOf(CVS);
    }

    public int getHVS() {
        if (HVS.equalsIgnoreCase(""))
            HVS=String.valueOf(0);
        return Integer.parseInt(HVS);
    }

    public void setHVS(int HVS) {
        this.HVS = String.valueOf(HVS);
    }

    public int getEVS() {
        if (EVS.equalsIgnoreCase(""))
            EVS=String.valueOf(0);
        return Integer.valueOf(EVS);
    }

    public void setEVS(int EVS) {
        this.EVS = String.valueOf(EVS);
    }

    public String getNomGan() {
        return NomGan;
    }

    public void setNomGan(String nomGan) {
        NomGan = nomGan;
    }

    public int getCHC() {
        if (CHC.equalsIgnoreCase(""))
            CHC=String.valueOf(0);
        return Integer.valueOf(CHC);
    }

    public void setCHC(int CHC) {
        this.CHC = String.valueOf(CHC);
    }

    public int getHHC() {
        if (HHC.equalsIgnoreCase(""))
            HHC=String.valueOf(0);
        return Integer.valueOf(HHC);
    }

    public void setHHC(int HHC) {
        this.HHC = String.valueOf(HHC);
    }

    public int getEHC() {
        if (EHC.equalsIgnoreCase(""))
            EHC=String.valueOf(0);
        return Integer.valueOf(EHC);
    }

    public void setEHC(int EHC) {
        this.EHC = String.valueOf(EHC);
    }

    public String getNomPerd() {
        return NomPerd;
    }

    public void setNomPerd(String nomPerd) {
        NomPerd = nomPerd;
    }

    public Linescore getLinescore() {
        return linescore;
    }

    public void setLinescore(Linescore linescore) {
        this.linescore = linescore;
    }
}
