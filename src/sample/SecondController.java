package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SecondController {

    private SQLiteData dc = new SQLiteData();
    private Connection con = dc.connect();
    private ObservableList<CartProduct> cart;
    //getting access to MainWindowController
    private MainWindowController mainWindowController;


    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
    @FXML
    void initialize() {
        loadCart();
    }
    @FXML
    TableView tableCart;
    @FXML
    TableColumn columnCartName;
    @FXML
    TableColumn columnCartPieces;
    @FXML
    TableColumn columnCartPrice;
    @FXML
    private void loadCart() {
        try {
            Connection con = dc.connect();
           cart = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Cart");
            while (rs.next()) {
                //get string from db,whichever way
                cart.add(new CartProduct(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
        //Set cell value factory to tableview.
        columnCartName.setCellValueFactory(new PropertyValueFactory<>("cartpName"));
        columnCartPieces.setCellValueFactory(new PropertyValueFactory<>("cartpLeft"));
        columnCartPrice.setCellValueFactory(new PropertyValueFactory<>("cartPrice"));


       tableCart.setItems(null);
       tableCart.setItems(cart);
    }

    @FXML
    private void btnAddToCartClick() {
        try {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Podaj nazwę produktu!");
            dialog.setHeaderText(null);
            dialog.setContentText("Podaj nazwe produktu do dodania: ");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String query = "INSERT INTO Cart(Name,Pieces,Price) SELECT Product,Pieces,Price FROM Products WHERE Product=?";
                PreparedStatement pS = con.prepareStatement(query);
                pS.setString(1, result.get());
                pS.executeUpdate();
                pS.close();
                loadCart();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void btnDeleteFromCartClick(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Podaj nazwę produktu");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj nazwe produktu: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String query = "DELETE from Cart where Name= ?";
            try {
                PreparedStatement pS = con.prepareStatement(query);
                pS.setString(1, result.get());
                pS.executeUpdate();
                pS.close();
                loadCart();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Usunięto poprawnie!");
                alert.setHeaderText(null);
                alert.setContentText("Produkt pomyślnie usunięty!");
                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void btnClearCartClick(){
        try {
            String query="DELETE FROM Cart";
            PreparedStatement pS=con.prepareStatement(query);
            pS.execute();
            pS.close();
            loadCart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void btnExportClick(){

    }
}
