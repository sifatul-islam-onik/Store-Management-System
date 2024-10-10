package com.example.storemanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class MainMenuController implements Initializable {

    @FXML
    private Button customers_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_add_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clear_btn;

    @FXML
    private TableColumn<ProductData,String> inventory_col__date;

    @FXML
    private TableColumn<ProductData,String> inventory_col_price;

    @FXML
    private TableColumn<ProductData,String> inventory_col_productid;

    @FXML
    private TableColumn<ProductData,String> inventory_col_productname;

    @FXML
    private TableColumn<ProductData,String> inventory_col_status;

    @FXML
    private TableColumn<ProductData,String> inventory_col_stock;

    @FXML
    private TableColumn<ProductData,String> inventory_col_type;

    @FXML
    private Button inventory_delete_btn;

    @FXML
    private ImageView inventory_image;

    @FXML
    private Button inventory_import_button;

    @FXML
    private AnchorPane inventory_panel;

    @FXML
    private TableView<ProductData> inventory_table;

    @FXML
    private Button inventory_update_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_panel;

    @FXML
    private Button menu_btn;

    @FXML
    private AnchorPane panel_dashboard;

    @FXML
    private Label main_menu_username;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_productid;

    @FXML
    private TextField inventory_productname;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private TextField inventory_stock;

    @FXML
    private ComboBox<?> inventory_type;

    private Alert alert;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public ObservableList<ProductData> inventoryData(){
        ObservableList<ProductData> inventoryList = FXCollections.observableArrayList();
        String cmd = "SELECT * FROM products";

        connection = Database.getConnection();

        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            ProductData productData = null;
            while(resultSet.next()){
                productData = new ProductData(resultSet.getInt("id"),resultSet.getInt("product_id"),resultSet.getString("product_name"),resultSet.getString("type"),resultSet.getInt("stock"),resultSet.getDouble("price"),resultSet.getString("status"),resultSet.getString("image"),resultSet.getDate("date"));
                inventoryList.add(productData);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return inventoryList;
    }

    public void displayInventory(){
        ObservableList<ProductData> inventoryList = inventoryData();
        inventory_col_productid.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        inventory_col_productname.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    public void logout(){
        try{
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                logout_btn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("store-management.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Store Management System");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setInventoryType(){
        String list[] = {
                "Sticker & Sticky Notes",
                "Adhesive, Gum and Glue stick",
                "Painting, Drawing & Art Supplies",
                "School and Office Equipment",
                "Organizers",
                "Pen, Pencil and Markers",
                "Diary, Notebook and Khata"};
        List<String>l = new ArrayList<>();
        for(String s:list){
            l.add(s);
        }
        ObservableList data = FXCollections.observableArrayList(l);
        inventory_type.setItems(data);
    }

    public void setStatus(){
        String list[] = {"In Stock","Out of Stock"};
        List<String>l = new ArrayList<>();
        for(String s:list){
            l.add(s);
        }
        ObservableList data = FXCollections.observableArrayList(l);
        inventory_status.setItems(data);
    }

    public void displayUsername(){
        main_menu_username.setText(Data.username);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        setInventoryType();
        setStatus();
        displayInventory();
    }
}
