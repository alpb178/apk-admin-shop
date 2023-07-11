package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class ResetPassword implements Serializable {
    private String password;

    public ResetPassword(String password) {

        this.password = password;

    }
}


