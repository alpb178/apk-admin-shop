package cu.entumovil.snb.core.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;

@DatabaseTable(tableName = "favorites")
public class Favorite {

    @DatabaseField
    private Long id;

    @DatabaseField
    private String favoriteType;

    @DatabaseField
    private String name;

    @DatabaseField
    private Date storageDate;

    public Favorite() {
        Calendar calendar = Calendar.getInstance();
        this.storageDate = calendar.getTime();
    }

    public Favorite(Long id, String favoriteType, String name) {
        Calendar calendar = Calendar.getInstance();
        this.id = id;
        this.favoriteType = favoriteType;
        this.name = name;
        this.storageDate = calendar.getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(String favoriteType) {
        this.favoriteType = favoriteType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }
}
