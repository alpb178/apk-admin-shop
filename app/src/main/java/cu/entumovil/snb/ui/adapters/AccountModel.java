package cu.entumovil.snb.ui.adapters;

import android.graphics.drawable.Drawable;

/**
 * Created by laura on 11-Jul-17.
 */

public class AccountModel {
    String mail;
    Drawable icon;

    public AccountModel(String mail, Drawable icon) {
        this.mail = mail;
        this.icon = icon;
    }

    public String getMail() {
        return mail;
    }

    public Drawable getIcon() {
        return icon;
    }
}
