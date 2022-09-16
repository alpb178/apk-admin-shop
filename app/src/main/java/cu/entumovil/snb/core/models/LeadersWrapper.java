package cu.entumovil.snb.core.models;

import java.io.Serializable;
import java.util.ArrayList;

public class LeadersWrapper implements Serializable {

    private ArrayList<SerieLeader> leadersWrapper;

    public LeadersWrapper() { }

    public ArrayList<SerieLeader> getLeadersWrapper() {
        return leadersWrapper;
    }

    public void setLeadersWrapper(ArrayList<SerieLeader> leadersWrapper) {
        this.leadersWrapper = leadersWrapper;
    }
}
