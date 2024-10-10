package com.example.storemanagementsystem;

import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Hyperlink forgot_password;

    @FXML
    private Button login_button;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_username;

    @FXML
    private TextField register_answer;

    @FXML
    private Button register_button;

    @FXML
    private PasswordField register_password;

    @FXML
    private PasswordField register_password_2;

    @FXML
    private ComboBox<?> register_question;

    @FXML
    private TextField register_username;

    @FXML
    private Label side_alreadyHave_Label;

    @FXML
    private Button side_create_button;

    @FXML
    private Label side_dontHave_label;

    @FXML
    private Button side_login_button;

    @FXML
    private AnchorPane sideform;

    @FXML
    private AnchorPane signup_form;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private String[] questions = {"what is your pet name?","what is your favourite color?","what is your favourite food?"};

    public void setQuestions() {
        List<String> questionsList = new ArrayList<>();
        questionsList.addAll(Arrays.asList(questions));
        ObservableList list = FXCollections.observableArrayList(questionsList);
        register_question.setItems(list);
    }

    private Alert alert;

    public void login(){
        if(login_username.getText().isEmpty() || login_password.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Required Fields are empty");
            alert.showAndWait();
            return;
        }
        connection = Database.getConnection();
        String cmd = "SELECT username, password FROM employees WHERE username = ? AND password = ?";

        try{
            preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setString(1, login_username.getText());
            preparedStatement.setString(2, login_password.getText());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                //will implement later
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Login Successful");
                alert.showAndWait();
            }
            else{
                login_password.clear();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Credentials does not match");
                alert.showAndWait();
                return;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void register(){
        if(register_username.getText().isEmpty() || register_password.getText().isEmpty() || register_password_2.getText().isEmpty() || register_question.getSelectionModel().getSelectedItem() == null || register_answer.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }

        connection = Database.getConnection();
        String cmd;

        try{
            cmd = "SELECT username FROM employees WHERE username = '"+register_username.getText()+"'";
            preparedStatement = connection.prepareStatement(cmd);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists");
                alert.showAndWait();
                return;
            }
            else if(!register_password.getText().equals(register_password_2.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match");
                alert.showAndWait();
                return;
            }
            else if(register_password.getText().length()<8){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Password too short");
                alert.showAndWait();
                return;
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        cmd = "INSERT INTO employees (username,password,question,answer,date) VALUES (?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setString(1, register_username.getText());
            preparedStatement.setString(2, register_password.getText());
            preparedStatement.setString(3, register_question.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(4, register_answer.getText());

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            preparedStatement.setString(5, String.valueOf(sqlDate));

            preparedStatement.executeUpdate();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully registered!");
            alert.showAndWait();
            register_username.setText("");
            register_password.setText("");
            register_password_2.setText("");
            register_question.getSelectionModel().clearSelection();
            register_answer.setText("");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event) {
        TranslateTransition slide = new TranslateTransition();
        if(event.getSource() == side_create_button) {
            side_alreadyHave_Label.setVisible(true);
            side_login_button.setVisible(true);
            side_create_button.setVisible(false);
            side_dontHave_label.setVisible(false);
            setQuestions();
            slide.setNode(sideform);
            slide.setToX(300);
            slide.setDuration(Duration.seconds(0.5));
            slide.play();
        }
        else if(event.getSource() == side_login_button) {
            side_alreadyHave_Label.setVisible(false);
            side_login_button.setVisible(false);
            side_create_button.setVisible(true);
            side_dontHave_label.setVisible(true);
            slide.setNode(sideform);
            slide.setToX(0);
            slide.setDuration(Duration.seconds(0.5));
            slide.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}