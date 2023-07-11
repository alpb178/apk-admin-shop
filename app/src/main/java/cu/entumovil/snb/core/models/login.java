package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class login implements Serializable {


    private String identifier;
    private String password;

    public login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
