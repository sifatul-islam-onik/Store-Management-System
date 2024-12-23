package com.example.storemanagementsystem.Controllers;

import com.example.storemanagementsystem.Models.CustomerData;
import com.example.storemanagementsystem.Models.ProductData;
import com.example.storemanagementsystem.StoreManagement;
import com.example.storemanagementsystem.Utilities.ConversionRate;
import com.example.storemanagementsystem.Utilities.Data;
import com.example.storemanagementsystem.Utilities.Database;
import com.example.storemanagementsystem.Utilities.InvoiceGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainMenuController implements Initializable {

    @FXML
    private Button conversion_rates_btn;

    @FXML
    private AnchorPane conversion_rates_panel;

    @FXML
    private ComboBox<?> currency_base;

    @FXML
    private TableView<Map.Entry<String, String>> currency_table;

    @FXML
    private TableColumn<?, ?> customer_col_cashier;

    @FXML
    private TableColumn<?, ?> customer_col_date;

    @FXML
    private TableColumn<?, ?> customer_col_id;

    @FXML
    private TableColumn<?, ?> customer_col_total;

    @FXML
    private AnchorPane customer_panel;

    @FXML
    private TableView<CustomerData> customer_table;

    @FXML
    private Button customers_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private TableColumn<?, ?> inventory_col__date;

    @FXML
    private TableColumn<?, ?> inventory_col_price;

    @FXML
    private TableColumn<?, ?> inventory_col_productid;

    @FXML
    private TableColumn<?, ?> inventory_col_productname;

    @FXML
    private TableColumn<?, ?> inventory_col_status;

    @FXML
    private TableColumn<?, ?> inventory_col_stock;

    @FXML
    private TableColumn<?, ?> inventory_col_type;

    @FXML
    private ImageView inventory_image;

    @FXML
    private AnchorPane inventory_panel;

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
    private TableView<ProductData> inventory_table;

    @FXML
    private ComboBox<?> inventory_type;

    @FXML
    private Button logout_btn;

    @FXML
    private Label main_menu_username;

    @FXML
    private AnchorPane main_panel;

    @FXML
    private TextField menu_amount;

    @FXML
    private Button menu_btn;

    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<?, ?> menu_col_price;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;

    @FXML
    private GridPane menu_grid;

    @FXML
    private AnchorPane menu_panel;

    @FXML
    private TableView<ProductData> menu_table;

    @FXML
    private Label menu_total;

    @FXML
    private AnchorPane dashboard_panel;

    @FXML
    private Label dashboard_numberofCustomer;

    @FXML
    private Label dashboard_soldProduct;

    @FXML
    private Label dashboard_todaysIncome;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private ComboBox<?> menu_filter_type;

    @FXML
    private Label next_update_time;

    @FXML
    private Label last_update_time;
    @FXML
    private TextField currency_input;

    @FXML
    private Label currency_output;

    private String cmd;
    private Alert alert;
    private Connection connection = Database.getConnection();
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void menuRefresh(){
        customerID();
        displayInventory();
        displayCardData();
        displayOrderData();
        displayTotalPrice();
        displayCustomerData();
        displayDashBoard();
        displayConversionRates();
    }

    public void switchForm(ActionEvent event) {
        if(event.getSource() == dashboard_btn) {
            dashboard_panel.setVisible(true);
            inventory_panel.setVisible(false);
            customer_panel.setVisible(false);
            menu_panel.setVisible(false);
            conversion_rates_panel.setVisible(false);
            displayDashBoard();
        }
        else if(event.getSource() == inventory_btn) {
            dashboard_panel.setVisible(false);
            inventory_panel.setVisible(true);
            customer_panel.setVisible(false);
            menu_panel.setVisible(false);
            conversion_rates_panel.setVisible(false);
            displayInventory();
        }
        else if(event.getSource() == menu_btn) {
            dashboard_panel.setVisible(false);
            inventory_panel.setVisible(false);
            customer_panel.setVisible(false);
            menu_panel.setVisible(true);
            conversion_rates_panel.setVisible(false);
            menuRefresh();
        }
        else if(event.getSource() == customers_btn) {
            dashboard_panel.setVisible(false);
            inventory_panel.setVisible(false);
            customer_panel.setVisible(true);
            menu_panel.setVisible(false);
            conversion_rates_panel.setVisible(false);
            displayCustomerData();
        }
        else if(event.getSource() == conversion_rates_btn){
            dashboard_panel.setVisible(false);
            inventory_panel.setVisible(false);
            customer_panel.setVisible(false);
            menu_panel.setVisible(false);
            conversion_rates_panel.setVisible(true);
            displayConversionRates();
        }
    }

    public void setCurrencyType(){
        ConversionRate conversionRate = new ConversionRate("USD");
        List<String>l = new ArrayList<>();
        l.addAll(conversionRate.getRates().keySet());
        Collections.sort(l);
        l.add(0,"USD");
        ObservableList data = FXCollections.observableArrayList(l);
        currency_base.setItems(data);
        currency_base.getSelectionModel().selectFirst();
    }

    public void displayConversionRates(){
        String currency = currency_base.getSelectionModel().getSelectedItem().toString();

        ConversionRate conversionRate = new ConversionRate(currency);

        last_update_time.setText(conversionRate.getLastUpdateUtc());
        next_update_time.setText(conversionRate.getNextUpdateUtc());

        TableColumn<Map.Entry<String,String>,String> col1 = new TableColumn<>("Currency");
        TableColumn<Map.Entry<String,String>,String> col2 = new TableColumn<>("Rate");

        col1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> entryStringCellDataFeatures) {
                return new SimpleStringProperty(entryStringCellDataFeatures.getValue().getKey());
            }
        });

        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> entryStringCellDataFeatures) {
                return new SimpleStringProperty(entryStringCellDataFeatures.getValue().getValue());
            }
        });

        ObservableList<Map.Entry<String,String>> items = FXCollections.observableArrayList(conversionRate.getRates().entrySet());
        currency_table.setItems(items);
        currency_table.getColumns().setAll(col1,col2);
    }

    public void displayDashboardNoOfCustomer(){
        cmd = "SELECT COUNT(id) FROM receipts";
        int count = 0;
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                count = resultSet.getInt("COUNT(id)");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        dashboard_numberofCustomer.setText(String.valueOf(count));
    }

    public void displayDashboardSoldProduct(){
        cmd = "SELECT SUM(quantity) FROM customers";
        int count = 0;
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                count = resultSet.getInt("SUM(quantity)");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        dashboard_soldProduct.setText(String.valueOf(count));
    }

    public void displayDashboardTotalIncome(){
        double total = 0;
        cmd = "SELECT SUM(total) FROM receipts";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                total = resultSet.getDouble("SUM(total)");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        dashboard_totalIncome.setText(total + " BDT");
    }

    public void displayDashboardTodaysIncome(){
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        double total = 0;
        cmd = "SELECT SUM(total) FROM receipts WHERE date = '" + sqlDate + "'";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                total = resultSet.getDouble("SUM(total)");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        dashboard_todaysIncome.setText(total + " BDT");
    }

    public void displayDashBoard(){
        displayDashboardNoOfCustomer();
        displayDashboardSoldProduct();
        displayDashboardTotalIncome();
        displayDashboardTodaysIncome();
    }

    public ObservableList<CustomerData> getCustomerData(){
        ObservableList<CustomerData> data = FXCollections.observableArrayList();
        cmd = "SELECT * FROM receipts";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            CustomerData customerData;
            while(resultSet.next()){
                customerData = new CustomerData(resultSet.getInt("id"),resultSet.getInt("customer_id"),resultSet.getDouble("total"),resultSet.getDate("date"),resultSet.getString("employee_username"));
                data.add(customerData);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public void displayCustomerData(){
        ObservableList<CustomerData> data = getCustomerData();

        customer_col_id.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customer_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customer_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customer_col_cashier.setCellValueFactory(new PropertyValueFactory<>("employee_username"));

        customer_table.setItems(data);
    }

    public ObservableList<ProductData> getOrderData() {
        int cID = Data.cid;
        ObservableList<ProductData> data = FXCollections.observableArrayList();
        cmd = "SELECT * FROM customers WHERE customer_id = " + cID;
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                 ProductData productData = new ProductData(resultSet.getInt("ID"),
                         resultSet.getString("product_name"),
                         resultSet.getInt("quantity"),
                         resultSet.getDouble("price"),
                         resultSet.getDate("date"));
                 data.add(productData);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public void displayOrderData(){
        ObservableList<ProductData> data = getOrderData();
        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        menu_table.setItems(data);
    }

    double getTotalPrice(){
        double total = 0;
        int cID = Data.cid;
        cmd = "SELECT SUM(price) FROM customers WHERE customer_id = " + cID;
        try {
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble("SUM(price)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public void displayTotalPrice() {
        double total = getTotalPrice();
        menu_total.setText(total + " BDT");
    }

    public void menuAmmount(){
        double total = getTotalPrice();
        double ammount = Double.valueOf(menu_amount.getText().toString());
        if(ammount<total){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Not sufficient ammount");
            alert.showAndWait();
            menu_change.setText("0.00 BDT");
        }
        else{
            double change = ammount - total;
            menu_change.setText(change + " BDT");
        }
    }

    public void menuPayment(){
        double total = getTotalPrice();
        int cID = Data.cid;
        double ammount = Double.valueOf(menu_amount.getText().toString());
        if(ammount<total){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Not sufficient ammount");
            alert.showAndWait();
            menu_change.setText("0.00 BDT");
            return;
        }
        if(total == 0) return;

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("PAYMENT");
        alert.setHeaderText(null);
        alert.setContentText("Pay BDT " + total + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            cmd = "INSERT INTO receipts (customer_id, total, date, employee_username) VALUES(?,?,?,?)";
            try{
                preparedStatement = connection.prepareStatement(cmd);
                preparedStatement.setInt(1, cID);
                preparedStatement.setDouble(2, total);
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                preparedStatement.setDate(3,sqlDate);
                preparedStatement.setString(4, Data.username);
                preparedStatement.executeUpdate();

                generateInvoice(cID,Data.username,date,ammount);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SUCCESS");
                alert.setHeaderText(null);
                alert.setContentText("Payment Successful...Invoice Generated");
                alert.showAndWait();

                File file = new File(cID + ".pdf");
                openFile(file);

                menu_total.setText("0.00 BDT");
                menu_change.setText("0.00 BDT");
                menu_amount.clear();
                menuRefresh();
            } catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void openFile(File file) throws Exception {
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void generateInvoice(int CustomerID,String EmployeeID,Date date,double amount){
        ObservableList<ProductData> data = getOrderData();
        List<ProductData> list = new ArrayList<>(data);
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator(list);
        SimpleDateFormat pattern = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
        invoiceGenerator.write(CustomerID,EmployeeID,pattern.format(date),amount);
    }

    public void menuRemove(){
        ProductData product =  menu_table.getSelectionModel().getSelectedItem();
        int ind = menu_table.getSelectionModel().getSelectedIndex();
        if(ind < 0 || product.getId() == 0) return;

        cmd = "SELECT stock FROM products WHERE name = '" + product.getProductName() + "'";
        int stock = 0;
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                stock = resultSet.getInt("stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int updStock = product.getQuantity() + stock;

        cmd = "UPDATE products SET stock = '" + updStock + "', status = '" +
                "In Stock" + "' WHERE name = '" + product.getProductName() + "'";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cmd = "DELETE FROM customers WHERE id = " + product.getId();
        try{
            preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.executeUpdate();
            menuRefresh();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<ProductData> getCardData(String category){
        ObservableList<ProductData> list = FXCollections.observableArrayList();
        if(category.equals("None")) cmd = "SELECT * FROM products";
        else cmd = "SELECT * FROM products WHERE type = '" + category + "'";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ProductData product = new ProductData(resultSet.getInt("id"),resultSet.getInt("product_id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getInt("stock"),resultSet.getDouble("price"),resultSet.getString("status"),resultSet.getString("image"),resultSet.getDate("date"));
                list.add(product);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public void displayCardData(){
        String category;
        if(menu_filter_type.getSelectionModel().getSelectedItem() == null) category = "None";
        else category = menu_filter_type.getSelectionModel().getSelectedItem().toString();

        ObservableList<ProductData> list = getCardData(category);

        int row = 0;
        int column = 0;

        menu_grid.getChildren().clear();
        menu_grid.getRowConstraints().clear();
        menu_grid.getColumnConstraints().clear();

        for(ProductData product : list){
            try{
                FXMLLoader load = new FXMLLoader();
                load.setLocation(StoreManagement.class.getResource("fxml/product-card.fxml"));
                AnchorPane pane = load.load();
                ProductCardController cardC = load.getController();
                cardC.setProductData(product);

                if (column == 4) {
                    column = 0;
                    row += 1;
                }

                menu_grid.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(3));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public ObservableList<ProductData> inventoryData(){
        ObservableList<ProductData> inventoryList = FXCollections.observableArrayList();
        cmd = "SELECT * FROM products";

        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            ProductData productData = null;
            while(resultSet.next()){
                productData = new ProductData(resultSet.getInt("id"),resultSet.getInt("product_id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getInt("stock"),resultSet.getDouble("price"),resultSet.getString("status"),resultSet.getString("image"),resultSet.getDate("date"));
                inventoryList.add(productData);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return inventoryList;
    }

    public void importBtn(){
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File file = openFile.showOpenDialog(main_panel.getScene().getWindow());
        if(file != null){
            Data.path = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString(),175,175,false,true);
            inventory_image.setImage(image);
        }
    }

    public void addBtn(){
        if(inventory_productid.getText().isEmpty() || inventory_productname.getText().isEmpty() || inventory_type.getSelectionModel().getSelectedItem() == null || inventory_status.getSelectionModel().getSelectedItem() == null || inventory_stock.getText().isEmpty() || inventory_price.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the required fields");
            alert.showAndWait();
            return;
        }
        cmd = "SELECT product_id FROM products WHERE product_id = '" + inventory_productid.getText() + "'";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Product id already exists");
                alert.showAndWait();
            }
            else{
                cmd = "INSERT INTO products (product_id,name,type,stock,price,status,image,date) VALUES(?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(cmd);
                preparedStatement.setString(1, inventory_productid.getText());
                preparedStatement.setString(2, inventory_productname.getText());
                preparedStatement.setString(3, inventory_type.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(4, inventory_stock.getText());
                preparedStatement.setString(5, inventory_price.getText());
                preparedStatement.setString(6, inventory_status.getSelectionModel().getSelectedItem().toString());
                Date date = new Date();
                preparedStatement.setDate(8, new java.sql.Date(date .getTime()));
                String path = Data.path;
                path.replace("\\","\\\\");
                preparedStatement.setString(7, path);
                preparedStatement.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product added successfully");
                alert.showAndWait();

                displayInventory();
                displayCardData();
                clearBtn();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearBtn(){
        inventory_price.clear();
        inventory_productname.clear();
        inventory_type.getSelectionModel().clearSelection();
        inventory_stock.clear();
        inventory_status.getSelectionModel().clearSelection();
        inventory_productid.clear();
        Data.path = "";
        Data.date = "";
        Data.id = 0;

        inventory_image.setImage(null);
    }

    public void selectDataInventory(){
        ProductData productData = (ProductData) inventory_table.getSelectionModel().getSelectedItem();
        int index = inventory_table.getSelectionModel().getSelectedIndex();
        if(index<0) return;
        inventory_productid.setText(String.valueOf(productData.getProduct_id()));
        inventory_price.setText(String.valueOf(productData.getPrice()));
        inventory_productname.setText(productData.getProductName());
        inventory_stock.setText(String.valueOf(productData.getStock()));
        Data.path = productData.getImage();
        String path = "File:" + Data.path;
        inventory_image.setImage(new Image(path,175,175,false,true));
        Data.date = String.valueOf(productData.getDate());
        Data.id = productData.getId();
    }

    public void updateBtn(){
        if(inventory_productid.getText().isEmpty() || inventory_productname.getText().isEmpty() || inventory_type.getSelectionModel().getSelectedItem() == null || inventory_status.getSelectionModel().getSelectedItem() == null || inventory_stock.getText().isEmpty() || inventory_price.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the required fields");
            alert.showAndWait();
            return;
        }
        String path = Data.path;
        path = path.replace("\\","\\\\");
        cmd = "UPDATE products SET product_id = '" + inventory_productid.getText() + "', name = '" +
                inventory_productname.getText() + "', type = '" + inventory_type.getSelectionModel().getSelectedItem() +
                "', stock = '" + inventory_stock.getText() + "', price = '" + inventory_price.getText() + "', status = '" +
                inventory_status.getSelectionModel().getSelectedItem() + "', image = '" + path + "', date = '" + Data.date +
                "' WHERE product_id = '" + inventory_productid.getText() + "'";
        try{
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update");
            alert.setHeaderText(null);
            alert.setContentText("Update the selected product?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                preparedStatement = connection.prepareStatement(cmd);
                preparedStatement.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update");
                alert.setHeaderText(null);
                alert.setContentText("Product updated successfully");
                alert.showAndWait();

                displayInventory();
                displayCardData();
                clearBtn();
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBtn(){
        if(inventory_productid.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the product id field");
            alert.showAndWait();
            return;
        }

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("DELETE");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            cmd = "DELETE FROM products WHERE product_id = '" + inventory_productid.getText() + "'";
            try{
                preparedStatement = connection.prepareStatement(cmd);
                preparedStatement.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("DELETE");
                alert.setHeaderText(null);
                alert.setContentText("Product deleted successfully");
                alert.showAndWait();
                displayInventory();
                displayCardData();
                clearBtn();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void displayInventory(){
        ObservableList<ProductData> inventoryList = inventoryData();
        inventory_col_productid.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        inventory_col_productname.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col__date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_table.setItems(inventoryList);
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
                Parent root = FXMLLoader.load(StoreManagement.class.getResource("fxml/store-management.fxml"));
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
                "None",
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
        menu_filter_type.setItems(data);
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


    public void customerID(){
        int cid = 0,cid2 = 0;
        cmd = "SELECT MAX(customer_id) FROM customers";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                cid = resultSet.getInt("MAX(customer_id)");
            }
            cmd = "SELECT MAX(customer_id) FROM receipts";
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                cid2 = resultSet.getInt("MAX(customer_id)");
            }
            if(cid == 0 || cid2 == cid) ++cid;
            Data.cid = cid;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateCurrency(){
        if(currency_table.getSelectionModel().getSelectedItem() == null){
            return;
        }
        int in = Integer.parseInt(currency_input.getText());
        double result = 0.0;
        String currency = currency_table.getSelectionModel().getSelectedItem().getKey();
        double rate = Double.parseDouble(currency_table.getSelectionModel().getSelectedItem().getValue());
        int index = currency_table.getSelectionModel().getSelectedIndex();
        if(index<0) return;

        result = in * rate;
        String out = currency_base.getSelectionModel().getSelectedItem().toString() + " = " + String.format("%.2f",result) + " " + currency;
        currency_output.setText(out);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        setInventoryType();
        setStatus();
        setCurrencyType();
        menuRefresh();
    }
}
