package cu.entumovil.snb.core.models;

import java.util.Date;

public class Record {

    private String NombreRecord;

    private String NumRomano;

    private String TipoSerie;

    private String SerieAno;

    private String Recordista;

    private String SiglaEq;

    private String fmtoCANT;

    private String Cant;

    private String fmtoCANT1;

    private String Cant1;

    private String fmtoCANT2;

    private String Cant2;

    private String NumSerie;

    private String fecha;

    public Record() { }

    public String getNombreRecord() {
        return NombreRecord;
    }

    public void setNombreRecord(String nombreRecord) {
        NombreRecord = nombreRecord;
    }

    public String getNumRomano() {
        return NumRomano;
    }

    public void setNumRomano(String numRomano) {
        NumRomano = numRomano;
    }

    public String getTipoSerie() {
        return TipoSerie;
    }

    public void setTipoSerie(String tipoSerie) {
        TipoSerie = tipoSerie;
    }

    public String getSerieAno() {
        return SerieAno;
    }

    public void setSerieAno(String serieAno) {
        SerieAno = serieAno;
    }

    public String getRecordista() {
        return Recordista;
    }

    public void setRecordista(String recordista) {
        Recordista = recordista;
    }

    public String getSiglaEq() {
        return SiglaEq;
    }

    public void setSiglaEq(String siglaEq) {
        SiglaEq = siglaEq;
    }

    public String getFmtoCANT() {
        return fmtoCANT;
    }

    public void setFmtoCANT(String fmtoCANT) {
        this.fmtoCANT = fmtoCANT;
    }

    public String getCant() {
        return Cant;
    }

    public void setCant(String cant) {
        Cant = cant;
    }

    public String getFmtoCANT1() {
        return fmtoCANT1;
    }

    public void setFmtoCANT1(String fmtoCANT1) {
        this.fmtoCANT1 = fmtoCANT1;
    }

    public String getCant1() {
        return Cant1;
    }

    public void setCant1(String cant1) {
        Cant1 = cant1;
    }

    public String getFmtoCANT2() {
        return fmtoCANT2;
    }

    public void setFmtoCANT2(String fmtoCANT2) {
        this.fmtoCANT2 = fmtoCANT2;
    }

    public String getCant2() {
        return Cant2;
    }

    public void setCant2(String cant2) {
        Cant2 = cant2;
    }

    public String getNumSerie() {
        return NumSerie;
    }

    public void setNumSerie(String numSerie) {
        NumSerie = numSerie;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
