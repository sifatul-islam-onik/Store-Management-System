package com.example.storemanagementsystem;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

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

    public void switchForm(ActionEvent event) {
        TranslateTransition slide = new TranslateTransition();
        if(event.getSource() == side_create_button) {
            side_alreadyHave_Label.setVisible(true);
            side_login_button.setVisible(true);
            side_create_button.setVisible(false);
            side_dontHave_label.setVisible(false);
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