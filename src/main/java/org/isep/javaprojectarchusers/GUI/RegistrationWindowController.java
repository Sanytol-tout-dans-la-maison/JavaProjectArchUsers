package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RegistrationWindowController {
    @FXML
    TextField registrationNameField;
    @FXML
    TextField registrationPhoneField;
    @FXML
    TextField registrationEmailField;
    @FXML
    TextField registrationPasswordField;
    @FXML
    TextField registrationConfirmPassword;

    public void register() throws IOException, NoSuchAlgorithmException {
        switch(PortfolioManager.register(registrationEmailField.getText(), registrationPasswordField.getText(),registrationConfirmPassword.getText())) {
            case 1:
                switchToAuthentification();
                break;
            case 0:
                showEmptyAlert();
                break;
            default:
                showAlert();
                break;
        }

    }

    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User information");
        alert.setHeaderText(null);
        alert.setContentText("Passwords do not match !");
        alert.showAndWait();
    }

    private void showEmptyAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User information");
        alert.setHeaderText(null);
        alert.setContentText("Some fields are blank !");
        alert.showAndWait();
    }

    public void switchToAuthentification() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("authentication-interface.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) registrationNameField.getScene().getWindow();
        stage.setTitle("Authentication");
        stage.setScene(scene);
    }

    public void clear(){
        registrationNameField.clear();
        registrationPhoneField.clear();
        registrationEmailField.clear();
        registrationPasswordField.clear();
        registrationConfirmPassword.clear();
    }
}
