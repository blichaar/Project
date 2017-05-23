package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;


public class MainWindowController {
    @FXML
    TableView tableShoppingCart;
    @FXML
    TableView table;
    @FXML
    TableColumn columnName;
    @FXML
    TableColumn columnpLeft;
    @FXML
    TableColumn columnPrice;
    @FXML
    TextField fieldName;
    @FXML
    TextField fieldPieces;
    @FXML
    TextField fieldPrice;
    private ObservableList<Product> data;
    private SQLiteData dc = new SQLiteData();
    private Connection con = dc.connect();

    @FXML
    private void btnAuthorsClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Autorzy");
        alert.setHeaderText(null);
        alert.setContentText("Twórcy aplikacji:" +
                "\n- Mateusz Blicharski" +
                "\n- Paweł Wojtusik");
        alert.showAndWait();
    }
    @FXML
    private void showCartClick(){
        if(tableShoppingCart.getOpacity()==1)
            tableShoppingCart.setOpacity(0);
        else
            tableShoppingCart.setOpacity(1);
    }
    
    @FXML
    private void loadDatabase() {
        try {
            Connection con = dc.connect();
            data = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Products");
            while (rs.next()) {
                //get string from db,whichever way
                data.add(new Product(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
//Set cell value factory to tableview.
        columnName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnpLeft.setCellValueFactory(new PropertyValueFactory<>("pLeft"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

        table.setItems(null);
        table.setItems(data);
    }

    @FXML
    private void btnAddClick() throws SQLException {
        try {
            data = FXCollections.observableArrayList();
            String sql = "INSERT INTO Products(Product,Pieces,Price) VALUES (?,?,?)";
            Statement stat = con.createStatement();
            PreparedStatement pS = con.prepareStatement(sql);

            if (fieldPrice.getText().isEmpty() || fieldPieces.getText().isEmpty() || fieldName.getText().isEmpty() ||
                    fieldPieces.getText().matches("[a-zA-z]+/s") || fieldPrice.getText().matches("[a-zA-Z]")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Autorzy");
                alert.setHeaderText(null);
                alert.setContentText("Pola tekstowe puste lub wpisane dane są nieprawidłowe!");
                alert.showAndWait();

            } else {
                String sql2 = "SELECT * FROM Products where Product='" + fieldName.getText() + "'";
                ResultSet rs = stat.executeQuery(sql2);
                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Produkt istnieje");
                    alert.setHeaderText(null);
                    alert.setContentText("Produkt znajduje się już w bazie danych!");
                    alert.showAndWait();
                    rs.close();
                } else {
                    pS.setString(1, fieldName.getText());
                    pS.setString(2, fieldPieces.getText());
                    pS.setString(3, fieldPrice.getText());
                    pS.executeUpdate();
                    loadDatabase();
                }
            }
            fieldName.clear();
            fieldPieces.clear();
            fieldPrice.clear();
            pS.close();
        } catch (SQLException ex) {
            System.out.println("error" + ex);
        }
    }
}