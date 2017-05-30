package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class CartProduct {
    private final SimpleStringProperty cartpName;
    private final SimpleStringProperty cartpLeft;
    private final SimpleStringProperty cartPrice;

    public CartProduct(String cartpName, String cartpLeft, String cartPrice) {
        this.cartpName = new SimpleStringProperty(cartpName);
        this.cartpLeft = new SimpleStringProperty(cartpLeft);
        this.cartPrice = new SimpleStringProperty(cartPrice);
    }


    public String getCartpName() {
        return cartpName.get();
    }

    public String getCartpLeft() {return cartpLeft.get();}

    public String getCartPrice() {return cartPrice.get();}

    public void setCartpName(String cartpName1) {cartpName.set(cartpName1);}

    public void setCartpLeft (String cartpLeft1) {cartpName.set(cartpLeft1);}

    public void setCartPrice (String cartPrice1) {cartPrice.set(cartPrice1);}

    public StringProperty cartpNameProperty(){return cartpName;}

    public StringProperty cartpLeftProperty(){return cartpLeft;}

    public StringProperty cartPriceProperty(){return cartPrice;}
}