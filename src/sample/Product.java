package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Product {
    private final SimpleStringProperty productName;
    private final SimpleStringProperty pLeft;
    private final SimpleStringProperty price;

    public Product(String productName, String pLeft, String price) {
        this.productName = new SimpleStringProperty(productName);
        this.pLeft = new SimpleStringProperty(pLeft);
        this.price = new SimpleStringProperty(price);
    }

    public String getProductName() {
        return productName.get();
    }

    public String getPLeft() {
        return pLeft.get();
    }

    public String getPrice() {
        return price.get();
    }

    public void setProductName(String pName) {
        productName.set(pName);
    }

    public void setpLeft(String pLeft1) {
        pLeft.set(pLeft1);
    }

    public void setPrice(String price1) {
        price.set(price1);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public StringProperty pLeftProperty() {
        return pLeft;
    }

    public StringProperty priceProperty() {
        return price;
    }
}
