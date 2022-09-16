package cu.entumovil.snb.core.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;

@DatabaseTable(tableName = "news")
public class News {

    @DatabaseField
    private long id;

    @DatabaseField
    private String jsonNews;

    @DatabaseField(defaultValue = "false")
    public boolean pictureLoaded;

    @DatabaseField
    private Date storageDate;

    public News() {
        Calendar calendar = Calendar.getInstance();
        this.storageDate = calendar.getTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJsonNews() {
        return jsonNews;
    }

    public void setJsonNews(String jsonNews) {
        this.jsonNews = jsonNews;
    }

    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }

    public boolean isPictureLoaded() {
        return pictureLoaded;
    }

    public void setPictureLoaded(boolean pictureLoaded) {
        this.pictureLoaded = pictureLoaded;
    }
}
