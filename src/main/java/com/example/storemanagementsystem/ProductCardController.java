package com.example.storemanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

public class ProductCardController implements Initializable {

    @FXML
    private AnchorPane product_card;

    @FXML
    private Button product_card_addBtn;

    @FXML
    private ImageView product_card_image;

    @FXML
    private Label product_card_name;

    @FXML
    private Label product_card_price;

    @FXML
    private Spinner<Integer> product_card_spinner;

    private String cmd;
    private ProductData productData;
    private SpinnerValueFactory<Integer>spin;
    private int quantity;
    private Connection connection = Database.getConnection();
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    private Alert alert;

    public void addBtn(){
//        MainMenuController mainMenuController = new MainMenuController();
//        mainMenuController.customerID();
        quantity = product_card_spinner.getValue();
        if(quantity == 0) return;
        String check = "";
        cmd = "SELECT stock FROM products WHERE name = '" + product_card_name.getText() + "'";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            int stock = 0;
            if(resultSet.next()){
                stock = resultSet.getInt("stock");
            }
            if(stock < quantity){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Not enough stock");
                alert.showAndWait();
            }
            else{
                cmd = "SELECT status FROM products WHERE name = '" + product_card_name.getText() + "'";
                preparedStatement = connection.prepareStatement(cmd);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    check = resultSet.getString("status");
                }
                if(!check.equals("In Stock")){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Product is out of stock");
                    alert.showAndWait();
                }
                else{
                    cmd = "INSERT INTO customers (customer_id,product_name,quantity,price,date,employee_username) VALUES(?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(cmd);
                    preparedStatement.setInt(1,Data.cid);
                    preparedStatement.setString(2,product_card_name.getText());
                    preparedStatement.setInt(3,quantity);
                    preparedStatement.setDouble(4,Double.valueOf(product_card_price.getText())*quantity);
                    Date date = new Date();
                    preparedStatement.setDate(5,new java.sql.Date(date.getTime()));
                    preparedStatement.setString(6,Data.username);
                    preparedStatement.executeUpdate();

                    int updStock = stock - quantity;
                    if(updStock == 0){
                        cmd = "UPDATE products SET name = '"
                                + product_card_name.getText() + "', type = '"
                                + productData.getType() + "', stock = " + updStock + ", price = " + productData.getPrice()
                                + ", status = '"
                                + "Out of Stock" + "', image = '"
                                + productData.getImage() + "', date = '"
                                + productData.getDate() + "' WHERE product_id = '"
                                + productData.getProduct_id() + "'";
                    }
                    else{
                        cmd = "UPDATE products SET name = '"
                                + product_card_name.getText() + "', type = '"
                                + productData.getType() + "', stock = " + updStock + ", price = " + productData.getPrice()
                                + ", status = '"
                                + "In Stock" + "', image = '"
                                + productData.getImage() + "', date = '"
                                + productData.getDate() + "' WHERE product_id = '"
                                + productData.getProduct_id() + "'";
                    }

                    preparedStatement = connection.prepareStatement(cmd);
                    preparedStatement.executeUpdate();

//                    mainMenuController = new MainMenuController();
//                    mainMenuController.displayOrderData();
//                    mainMenuController.displayTotalPrice();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("SUCCESS");
                    alert.setHeaderText(null);
                    alert.setContentText("Product added successfully");
                    alert.showAndWait();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setQuantity(){
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        product_card_spinner.setValueFactory(spin);

    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
        product_card_name.setText(productData.getProductName());
        product_card_price.setText(productData.getPrice().toString());
        String path = "File:" + productData.getImage();
        product_card_image.setImage(new Image(path,225,148,false,true));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQuantity();
    }
}