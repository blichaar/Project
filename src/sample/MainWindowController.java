package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.Optional;


public class MainWindowController {
    private ObservableList<Product> data;
    private SQLiteData dc = new SQLiteData();
    private Connection con = dc.connect();
    private SecondController secondController;
    @FXML
    Button btnShowCart;
    @FXML
    public TableView table;
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


    @FXML
    void initialize() {
        loadDatabase();
    }

    @FXML
    private void btnUpdateClick() {

        try {
            Statement stat = con.createStatement();
            if (checkIfExists()) {

            } else {
                String sql2 = "SELECT * FROM Products where Product='" + fieldName.getText() + "'";
                ResultSet rs = stat.executeQuery(sql2);
                if (rs.next()) {
                    String query = "UPDATE Products set Product='" + fieldName.getText() + "',Pieces='" + fieldPieces.getText() + "',Price='"
                            + fieldPrice.getText() + "' where Product='" + fieldName.getText() + "'";
                    PreparedStatement pS = con.prepareStatement(query);
                    pS.execute();
                    loadDatabase();
                    pS.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Zaktualizowano pomyślnie!");
                    alert.setHeaderText(null);
                    alert.setContentText("Produkt zaktualizowany pomyślnie!");
                    alert.showAndWait();

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Nie znaleziono produktu!");
                    alert.setHeaderText(null);
                    alert.setContentText("Przedmiot nie został znaleziony w bazie!");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fieldName.clear();
        fieldPieces.clear();
        fieldPrice.clear();
    }

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
                    !fieldPieces.getText().matches("\\d+") || fieldPrice.getText().matches("[a-zA-Z]")) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Błędne dane");
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Produkt dodany!");
                    alert.setHeaderText(null);
                    alert.setContentText("Produkt pomyślnie dodany do bazy!");

                    alert.showAndWait();
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

    @FXML
    private void btnDeleteFromBaseClick() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Podaj nazwę produktu");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj nazwe produktu: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String query = "DELETE from Products where Product= ?";
            try {
                PreparedStatement pS = con.prepareStatement(query);
                pS.setString(1, result.get());
                pS.executeUpdate();
                pS.close();
                loadDatabase();

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

    private boolean checkIfExists() {
        if (fieldPrice.getText().isEmpty() || fieldPieces.getText().isEmpty() || fieldName.getText().isEmpty() ||
                !fieldPieces.getText().matches("\\d+") || fieldPrice.getText().matches("[a-zA-Z]")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błędne dane");
            alert.setHeaderText(null);
            alert.setContentText("Pola tekstowe puste lub wpisane dane są nieprawidłowe!");
            alert.showAndWait();
            return true;
        } else {
            return false;
        }
    }
}