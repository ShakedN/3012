package moudles;

import com.google.firebase.database.DataSnapshot;

public class GroceryItem {
    String name;
    double price;
    int amount;
    int image;
    int id;
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


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

    public GroceryItem(String name, double price, int image, int id,int amount) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.id = id;
        this.amount=amount;
    }
    public GroceryItem() {}
    public static GroceryItem fromDataSnapshot(DataSnapshot snapshot) {
        GroceryItem item = new GroceryItem();
        item.setAmount(snapshot.child("amount").getValue(Integer.class));
        item.setId(snapshot.child("id").getValue(Integer.class));
        item.setName(snapshot.child("name").getValue(String.class));
        item.setPrice(snapshot.child("price").getValue(Double.class));
        return item;
    }




}
