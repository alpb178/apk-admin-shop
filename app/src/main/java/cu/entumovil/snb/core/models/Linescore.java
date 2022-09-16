package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class Linescore implements Serializable {

    private long gameId;

    private String Estadio;

    private String LanzGan;

    private String LanzPer;

    private String VS;
    private String VS01;
    private String VS02;
    private String VS03;
    private String VS04;
    private String VS05;
    private String VS06;
    private String VS07;
    private String VS08;
    private String VS09;
    private int CarVS;
    private String VSHit;
    private String VSErr;

    private String HC;
    private String HC01;
    private String HC02;
    private String HC03;
    private String HC04;
    private String HC05;
    private String HC06;
    private String HC07;
    private String HC08;
    private String HC09;
    private int CarHC;
    private String HCHit;
    private String HCErr;

    public Linescore() { }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getEstadio() {
        return Estadio;
    }

    public void setEstadio(String estadio) {
        Estadio = estadio;
    }

    public String getLanzGan() {
        return LanzGan;
    }

    public void setLanzGan(String lanzGan) {
        LanzGan = lanzGan;
    }

    public String getLanzPer() {
        return LanzPer;
    }

    public void setLanzPer(String lanzPer) {
        LanzPer = lanzPer;
    }

    public String getVS() {
        return VS;
    }

    public void setVS(String VS) {
        this.VS = VS;
    }

    public String getVS01() {
        return VS01;
    }

    public void setVS01(String VS01) {
        this.VS01 = VS01;
    }

    public String getVS02() {
        return VS02;
    }

    public void setVS02(String VS02) {
        this.VS02 = VS02;
    }

    public String getVS03() {
        return VS03;
    }

    public void setVS03(String VS03) {
        this.VS03 = VS03;
    }

    public String getVS04() {
        return VS04;
    }

    public void setVS04(String VS04) {
        this.VS04 = VS04;
    }

    public String getVS05() {
        return VS05;
    }

    public void setVS05(String VS05) {
        this.VS05 = VS05;
    }

    public String getVS06() {
        return VS06;
    }

    public void setVS06(String VS06) {
        this.VS06 = VS06;
    }

    public String getVS07() {
        return VS07;
    }

    public void setVS07(String VS07) {
        this.VS07 = VS07;
    }

    public String getVS08() {
        return VS08;
    }

    public void setVS08(String VS08) {
        this.VS08 = VS08;
    }

    public String getVS09() {
        return VS09;
    }

    public void setVS09(String VS09) {
        this.VS09 = VS09;
    }

    public String getVSHit() {
        return VSHit;
    }

    public void setVSHit(String VSHit) {
        this.VSHit = VSHit;
    }

    public String getVSErr() {
        return VSErr;
    }

    public void setVSErr(String VSErr) {
        this.VSErr = VSErr;
    }

    public String getHC() {
        return HC;
    }

    public void setHC(String HC) {
        this.HC = HC;
    }

    public String getHC01() {
        return HC01;
    }

    public void setHC01(String HC01) {
        this.HC01 = HC01;
    }

    public String getHC02() {
        return HC02;
    }

    public void setHC02(String HC02) {
        this.HC02 = HC02;
    }

    public String getHC03() {
        return HC03;
    }

    public void setHC03(String HC03) {
        this.HC03 = HC03;
    }

    public String getHC04() {
        return HC04;
    }

    public void setHC04(String HC04) {
        this.HC04 = HC04;
    }

    public String getHC05() {
        return HC05;
    }

    public void setHC05(String HC05) {
        this.HC05 = HC05;
    }

    public String getHC06() {
        return HC06;
    }

    public void setHC06(String HC06) {
        this.HC06 = HC06;
    }

    public String getHC07() {
        return HC07;
    }

    public void setHC07(String HC07) {
        this.HC07 = HC07;
    }

    public String getHC08() {
        return HC08;
    }

    public void setHC08(String HC08) {
        this.HC08 = HC08;
    }

    public String getHC09() {
        return HC09;
    }

    public void setHC09(String HC09) {
        this.HC09 = HC09;
    }

    public String getHCHit() {
        return HCHit;
    }

    public void setHCHit(String HCHit) {
        this.HCHit = HCHit;
    }

    public String getHCErr() {
        return HCErr;
    }

    public void setHCErr(String HCErr) {
        this.HCErr = HCErr;
    }

    public int getCarVS() {
        return CarVS;
    }

    public void setCarVS(int carVS) {
        CarVS = carVS;
    }

    public int getCarHC() {
        return CarHC;
    }

    public void setCarHC(int carHC) {
        CarHC = carHC;
    }
}
