package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class JewelSalePost implements Serializable {

    private int count;
    private String status;
    private int users_permissions_client;
    private int users_permissions_vendor;
    private int jewl_catalogue;

    public JewelSalePost(int count, String status, int users_permissions_client, int users_permissions_vendor, int jewl_catalogue) {
        this.count = count;
        this.status = status;
        this.users_permissions_client = users_permissions_client;
        this.users_permissions_vendor = users_permissions_vendor;
        this.jewl_catalogue = jewl_catalogue;
    }
}
