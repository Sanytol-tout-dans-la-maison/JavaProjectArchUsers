package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;

/**
 * Contrôleur gérant l'inscription d'un nouvel utilisateur.
 */
public class RegistrationController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    /**
     * Appelé par le bouton "Register".
     * Tente de créer le compte via le PortfolioManager.
     */
    @FXML
    public void register() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        try {
            // Appel à la méthode statique que nous avons réparée
            int code = PortfolioManager.register(email, password, confirm);

            if (code == 1) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès !");
                returnToLogin(); // On retourne automatiquement au login
            } else if (code == 0) {
                showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            } else if (code == -1) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
            } else if (code == -2) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Cet email est déjà utilisé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur Système", "Impossible de créer le compte : " + e.getMessage());
        }
    }

    /**
     * Appelé par le bouton "Clear".
     * Vide tous les champs.
     */
    @FXML
    public void clear() {
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    /**
     * Retourne à l'écran de connexion.
     */
    @FXML
    private void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("authentication-interface.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setTitle("Login - Portfolio Manager");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}