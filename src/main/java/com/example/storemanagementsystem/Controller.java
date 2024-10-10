package com.example.storemanagementsystem;

import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private TextField forgot_answer;

    @FXML
    private Button forgot_back_button;

    @FXML
    private Button forgot_change_back;

    @FXML
    private Button forgot_change_button;

    @FXML
    private AnchorPane forgot_form1;

    @FXML
    private AnchorPane forgot_form2;

    @FXML
    private PasswordField forgot_new_password;

    @FXML
    private PasswordField forgot_new_password2;

    @FXML
    private Hyperlink forgot_password;

    @FXML
    private ComboBox<?> forgot_question;

    @FXML
    private TextField forgot_username;

    @FXML
    private Button forgot_verify_button;

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

    public void setForgotQuestions(){
        List<String> questionsList = new ArrayList<>();
        questionsList.addAll(Arrays.asList(questions));
        ObservableList list = FXCollections.observableArrayList(questionsList);
        forgot_question.setItems(list);
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
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Login Successful");
                alert.showAndWait();
                Data.username = login_username.getText();

                Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Main Menu");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                login_button.getScene().getWindow().hide();
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
                alert.setContentText("Password too short...Choose a password of length atleast 8");
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
        else if(event.getSource() == forgot_password){
            login_form.setVisible(false);
            forgot_form1.setVisible(true);
            setForgotQuestions();
        }
        else if(event.getSource() == forgot_back_button){
            login_form.setVisible(true);
            forgot_form1.setVisible(false);
        }
        else if(event.getSource() == forgot_verify_button){
            if(forgot_username.getText().isEmpty() || forgot_question.getSelectionModel().getSelectedItem() == null || forgot_answer.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Required Fields are empty");
                alert.showAndWait();
                return;
            }

            connection = Database.getConnection();
            String cmd = "SELECT username, question, answer FROM employees WHERE username = ? AND question = ? AND answer = ?";

            try{
                preparedStatement = connection.prepareStatement(cmd);
                preparedStatement.setString(1, forgot_username.getText());
                preparedStatement.setString(2,(String)forgot_question.getSelectionModel().getSelectedItem());
                preparedStatement.setString(3,forgot_answer.getText());
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    forgot_form1.setVisible(false);
                    forgot_form2.setVisible(true);
                }
                else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Credentials does not match");
                    alert.showAndWait();
                    return;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(event.getSource() == forgot_change_button){
            if(forgot_new_password.getText().isEmpty() || forgot_new_password2.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Required Fields are empty");
                alert.showAndWait();
                return;
            }
            if(forgot_new_password.getText().length() < 8) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Password too short...Choose a password of length atleast 8");
                alert.showAndWait();
                return;
            }
            if(!forgot_new_password.getText().equals(forgot_new_password2.getText())){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match");
                alert.showAndWait();
                forgot_new_password.clear();
                forgot_new_password2.clear();
                return;
            }
            connection = Database.getConnection();
            String cmd = "Update employees SET password = '" + forgot_new_password.getText() + "' WHERE username = '" + forgot_username.getText() + "'";
            try{
                preparedStatement = connection.prepareStatement(cmd);
                preparedStatement.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Password changed successfully");
                alert.showAndWait();

                forgot_username.setText("");
                forgot_new_password.setText("");
                forgot_new_password2.setText("");
                forgot_answer.setText("");
                forgot_question.getSelectionModel().clearSelection();

                forgot_form2.setVisible(false);
                login_form.setVisible(true);

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(event.getSource() == forgot_change_back){
            forgot_form1.setVisible(true);
            forgot_form2.setVisible(false);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}