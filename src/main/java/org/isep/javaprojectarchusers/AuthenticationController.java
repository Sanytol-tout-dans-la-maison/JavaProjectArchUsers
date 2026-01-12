package org.isep.javaprojectarchusers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class AuthenticationController {

    @FXML
        private TextField idField;
    @FXML
        private TextField passwordField;

    @FXML
    public void switchToRegistration() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-window.fxml"));
        Parent root = fxmlLoader.load();

//    ShowViewController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.setTitle("Register");
        stage.setScene(scene);
    }

//    public void login() throws IOException, NoSuchAlgorithmException {
//        if(PortfolioManager.login(idField.getText(), passwordField.getText())) switchToChat();
//        else showAlert();
//    }

    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User information");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect email and password !");
        alert.showAndWait();
    }

//    public void switchToChat(String name) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-interface.fxml"));
//        Parent root = fxmlLoader.load();
//
//        HomeInterfaceController controller = fxmlLoader.getController();
//        controller.updateLabel(name);
//
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) idField.getScene().getWindow();
//        stage.setTitle("Your account");
//        stage.setScene(scene);
//    }
}
