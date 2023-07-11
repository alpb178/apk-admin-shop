package cu.entumovil.snb.core.models.jewelCatalog;


public class JewelCatalogCreate {

    private final String model;
    private final int image;
    private final String img_path;
    private final float weight;
    private final float carats;
    private final float price;
    private final float large;
    private final String description;
    private final int availability;
    private final int owner;

    public JewelCatalogCreate(String model, int image, String img_path, float weight, float carats, float price, float large, String description, int availability, int owner) {
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
}


