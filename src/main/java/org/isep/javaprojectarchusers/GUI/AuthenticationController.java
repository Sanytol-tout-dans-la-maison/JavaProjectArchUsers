package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.MainBackEnd;
import org.isep.javaprojectarchusers.PortfolioManager; // Ton backend

import java.io.IOException;

public class AuthenticationController {

    @FXML
    private TextField idField;
    @FXML
    private PasswordField passwordField;


    public void login() throws IOException {
        String email = idField.getText();
        String password = passwordField.getText();

        try {
            boolean success = PortfolioManager.login(email, password);

            if (success) {
                switchToWelcomePage(email);
            } else {
                showAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert();
        }
    }

    private void switchToWelcomePage(String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(AuthenticationController.class.getResource("MainPageView.fxml"));
        Parent root = loader.load();

        MainPageController controller = loader.getController();


        controller.setUserName(email);

        Scene scene = new Scene(root, 1000, 700);
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.setTitle("Portfolio Manager - Accueil");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }


    @FXML
    public void switchToRegistration() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration-window.fxml"));
        Parent root = fxmlLoader.load();

        // Note: Assure-toi que registration-window.fxml existe bien
        Scene scene = new Scene(root);
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.setTitle("Register");
        stage.setScene(scene);
    }


    private void switchToPortfolioView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PortfolioView.fxml"));
        Parent root = loader.load();

        // On passe les infos à ton contrôleur
        PortfolioController controller = loader.getController();
        if (!PortfolioManager.getPortfolioList().isEmpty()) {
            controller.setPortfolio(PortfolioManager.getPortfolioList().get(0));
            controller.updateVisuals();
        }

        Stage stage = (Stage) idField.getScene().getWindow();
        Scene scene = new Scene(root, 1200, 800); // Taille confortable
        stage.setTitle("My Crypto Portfolio - Dashboard");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }


    private static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User information");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect email and password !");
        alert.showAndWait();
    }
}