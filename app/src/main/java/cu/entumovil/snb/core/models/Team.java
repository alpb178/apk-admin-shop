package cu.entumovil.snb.core.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {

    private long id;

    private String name;

    private ArrayList<Player> players;

    public Team() {}

    public Team(long aId, String aName, ArrayList<Player> aPlayers) {
        this.id = aId;
        this.name = aName;
        this.players = aPlayers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}