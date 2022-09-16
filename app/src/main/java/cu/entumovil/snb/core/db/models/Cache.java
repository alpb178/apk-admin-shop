package cu.entumovil.snb.core.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;

import cu.entumovil.snb.core.utils.Utils;

@DatabaseTable(tableName = "caches")
public class Cache {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String guiType;

    @DatabaseField
    private String json;

    @DatabaseField
    private Date created;

    @DatabaseField
    private String selectedDate;

    @DatabaseField
    private long objectId;

    public Cache() {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        this.created = calendar.getTime();
        this.selectedDate = Utils.formatDate(calendar.getTime(), true);
    }

    public Cache(String type, String cache) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        this.guiType = type;
        this.json = cache;
        this.created = calendar.getTime();
        this.selectedDate = Utils.formatDate(calendar.getTime(), true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuiType() {
        return guiType;
    }

    public void setGuiType(String guiType) {
        this.guiType = guiType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }
}
