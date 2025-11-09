package foodordering.models;

public class MenuItem {
    private int id;
    private String name;
    private int categoryId;
    private double price;
    private String imagePath;

    public MenuItem() {}

    public MenuItem(int id, String name, int categoryId, double price, String imagePath) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
