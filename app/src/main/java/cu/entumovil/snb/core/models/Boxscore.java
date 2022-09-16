package cu.entumovil.snb.core.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Boxscore implements Serializable {

    private long gameId;

    private ArrayList<Hitter> BateoVS, BateoHC;

    private ArrayList<Pitcher> PitcheoVS, PitcheoHC;

    public Boxscore() { }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public ArrayList<Hitter> getBateoVS() {
        return BateoVS;
    }

    public void setBateoVS(ArrayList<Hitter> bateoVS) {
        BateoVS = bateoVS;
    }

    public ArrayList<Hitter> getBateoHC() {
        return BateoHC;
    }

    public void setBateoHC(ArrayList<Hitter> bateoHC) {
        BateoHC = bateoHC;
    }

    public ArrayList<Pitcher> getPitcheoVS() {
        return PitcheoVS;
    }

    public void setPitcheoVS(ArrayList<Pitcher> pitcheoVS) {
        PitcheoVS = pitcheoVS;
    }

    public ArrayList<Pitcher> getPitcheoHC() {
        return PitcheoHC;
    }

    public void setPitcheoHC(ArrayList<Pitcher> pitcheoHC) {
        PitcheoHC = pitcheoHC;
    }
}
