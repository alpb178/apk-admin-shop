package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class UserBlocked implements Serializable {
    Blocked data;

    public UserBlocked(Blocked data) {
        this.data = data;
    }

    public static class Blocked implements Serializable {


        private final boolean blocked;

        public Blocked(boolean blocked) {
            this.blocked = blocked;
        }
    }


}