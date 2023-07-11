package cu.entumovil.snb.core.models.jewel;

import java.io.Serializable;

import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;

public class Jewel implements Serializable {
    private int id;
   private String code;
    private int count;
    private String status;
    private String created_date;
    private String updated_date;
    private JewelCatalog jewl_catalogue;
    private User users_permissions_client;
    private User users_permissions_vendor;

    public Jewel(JewelCatalog jewl_catalogue,User users_permissions_client) {
        this.jewl_catalogue = jewl_catalogue;
        this.users_permissions_client=users_permissions_client;
    }

    public Jewel(int id, String code, int count, String status, String created_date, String updated_date, JewelCatalog jewl_catalogue, User users_permissions_client, User users_permissions_vendor) {
        this.id = id;
        this.code = code;
        this.count = count;
        this.status = status;
        this.created_date = created_date;
        this.updated_date = updated_date;
        this.jewl_catalogue = jewl_catalogue;
        this.users_permissions_client = users_permissions_client;
        this.users_permissions_vendor = users_permissions_vendor;
    }

    public Jewel(String code, int count, String status, JewelCatalog jewl_catalogue, User users_permissions_client, User users_permissions_vendor) {
        this.code = code;
        this.count = count;
        this.status = status;
        this.jewl_catalogue = jewl_catalogue;
        this.users_permissions_client = users_permissions_client;
        this.users_permissions_vendor = users_permissions_vendor;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JewelCatalog getJewl_catalogue() {
        return jewl_catalogue;
    }

    public void setJewl_catalogue(JewelCatalog jewl_catalogue) {
        this.jewl_catalogue = jewl_catalogue;
    }

    public User getUsers_permissions_client() {
        return users_permissions_client;
    }

    public void setUsers_permissions_client(User users_permissions_client) {
        this.users_permissions_client = users_permissions_client;
    }

    public User getUsers_permissions_vendor() {
        return users_permissions_vendor;
    }

    public void setUsers_permissions_vendor(User users_permissions_vendor) {
        this.users_permissions_vendor = users_permissions_vendor;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
