package cu.entumovil.snb.core.models.jewelCatalog;


import java.io.Serializable;

public class JewelCatalog implements Serializable {

    private int id;
    private String code;
    private String model;
    private Integer image;
    private String img_path;
    private float weight;
    private int measure_unit_weight;
    private String measure_unit_weight_symbol;
    private float carats;
    private int measure_unit_carats;
    private String measure_unit_carats_symbol;
    private float price;
    private int measure_unit_price;
    private String measure_unit_price_symbol;
    private float large;
    private int measure_unit_large;
    private String measure_unit_large_symbol;
    private String description;
    private String date_created;
    private String date_update;
    private int availability;
    private int owner;
    private String owner_name;
    private boolean isDelete;

    public JewelCatalog(int id, String code, String model, Integer image, String img_path, float weight, int measure_unit_weight, String measure_unit_weight_symbol, float carats, int measure_unit_carats, String measure_unit_carats_symbol, float price, int measure_unit_price, String measure_unit_price_symbol, float large, int measure_unit_large, String measure_unit_large_symbol, String description, String date_created, String date_update, int availability, int owner, String owner_name, boolean isDelete) {
        this.id = id;
        this.code = code;
        this.model = model;
        this.image = image;
        this.img_path = img_path;
        this.weight = weight;
        this.measure_unit_weight = measure_unit_weight;
        this.measure_unit_weight_symbol = measure_unit_weight_symbol;
        this.carats = carats;
        this.measure_unit_carats = measure_unit_carats;
        this.measure_unit_carats_symbol = measure_unit_carats_symbol;
        this.price = price;
        this.measure_unit_price = measure_unit_price;
        this.measure_unit_price_symbol = measure_unit_price_symbol;
        this.large = large;
        this.measure_unit_large = measure_unit_large;
        this.measure_unit_large_symbol = measure_unit_large_symbol;
        this.description = description;
        this.date_created = date_created;
        this.date_update = date_update;
        this.availability = availability;
        this.owner = owner;
        this.owner_name = owner_name;
        this.isDelete = isDelete;
    }

    public JewelCatalog(String code, String model, Integer image, String img_path, float weight, int measure_unit_weight, String measure_unit_weight_symbol, float carats, int measure_unit_carats, String measure_unit_carats_symbol, float price, int measure_unit_price, String measure_unit_price_symbol, float large, int measure_unit_large, String measure_unit_large_symbol, String description, String date_created, String date_update, int availability, int owner, String owner_name, boolean isDelete) {
        this.code = code;
        this.model = model;
        this.image = image;
        this.img_path = img_path;
        this.weight = weight;
        this.measure_unit_weight = measure_unit_weight;
        this.measure_unit_weight_symbol = measure_unit_weight_symbol;
        this.carats = carats;
        this.measure_unit_carats = measure_unit_carats;
        this.measure_unit_carats_symbol = measure_unit_carats_symbol;
        this.price = price;
        this.measure_unit_price = measure_unit_price;
        this.measure_unit_price_symbol = measure_unit_price_symbol;
        this.large = large;
        this.measure_unit_large = measure_unit_large;
        this.measure_unit_large_symbol = measure_unit_large_symbol;
        this.description = description;
        this.date_created = date_created;
        this.date_update = date_update;
        this.availability = availability;
        this.owner = owner;
        this.owner_name = owner_name;
        this.isDelete = isDelete;
    }

    public JewelCatalog(String model, Integer image, String img_path, float weight, float carats, float price, float large, String description, int availability, int owner) {
        this.model = model;
        this.image = image;
        this.img_path = img_path;
        this.weight = weight;
        this.carats = carats;
        this.price = price;
        this.large = large;
        this.description = description;
        this.availability = availability;
        this.owner = owner;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getImage_path() {
        return img_path;
    }

    public void setImage_path(String image_path) {
        this.img_path = image_path;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getMeasure_unit_weight() {
        return measure_unit_weight;
    }

    public void setMeasure_unit_weight(int measure_unit_weight) {
        this.measure_unit_weight = measure_unit_weight;
    }

    public String getMeasure_unit_weight_symbol() {
        return measure_unit_weight_symbol;
    }

    public void setMeasure_unit_weight_symbol(String measure_unit_weight_symbol) {
        this.measure_unit_weight_symbol = measure_unit_weight_symbol;
    }

    public float getCarats() {
        return carats;
    }

    public void setCarats(float carats) {
        this.carats = carats;
    }

    public int getMeasure_unit_carats() {
        return measure_unit_carats;
    }

    public void setMeasure_unit_carats(int measure_unit_carats) {
        this.measure_unit_carats = measure_unit_carats;
    }

    public String getMeasure_unit_carats_symbol() {
        return measure_unit_carats_symbol;
    }

    public void setMeasure_unit_carats_symbol(String measure_unit_carats_symbol) {
        this.measure_unit_carats_symbol = measure_unit_carats_symbol;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMeasure_unit_price() {
        return measure_unit_price;
    }

    public void setMeasure_unit_price(int measure_unit_price) {
        this.measure_unit_price = measure_unit_price;
    }

    public String getMeasure_unit_price_symbol() {
        return measure_unit_price_symbol;
    }

    public void setMeasure_unit_price_symbol(String measure_unit_price_symbol) {
        this.measure_unit_price_symbol = measure_unit_price_symbol;
    }

    public float getLarge() {
        return large;
    }

    public void setLarge(float large) {
        this.large = large;
    }

    public int getMeasure_unit_large() {
        return measure_unit_large;
    }

    public void setMeasure_unit_large(int measure_unit_large) {
        this.measure_unit_large = measure_unit_large;
    }

    public String getMeasure_unit_large_symbol() {
        return measure_unit_large_symbol;
    }

    public void setMeasure_unit_large_symbol(String measure_unit_large_symbol) {
        this.measure_unit_large_symbol = measure_unit_large_symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}


