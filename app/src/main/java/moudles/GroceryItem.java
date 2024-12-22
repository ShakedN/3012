package moudles;

public class GroceryItem {
    String name;
    double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroceryItem(String name, double price, int image, int id) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.id = id;
    }

    int image;
    int id;




}
