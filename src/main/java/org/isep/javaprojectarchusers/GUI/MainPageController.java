package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

public class MainPageController {
    private static final Logger logger = Logger.getLogger(MainPageController.class.getName());

    // --- ELEMENTS DE TON CAMARADE ---
    @FXML private TabPane portfolioHolder;
    @FXML private ListView<Portfolio> portfolioViewList;
    @FXML private TextField portfolioNameView;
    @FXML private TextField portfolioDescView;

    // --- TES ELEMENTS (AJOUTÉS) ---
    @FXML private Label welcomeLabel; // N'oublie pas d'ajouter ce Label dans le FXML !

    private PortfolioManager portfolioManager;

    // =========================================================================
    // 1. LOGIQUE DE BIENVENUE (TA PARTIE)
    // =========================================================================

    /**
     * Met à jour le label de bienvenue avec le nom de l'utilisateur.
     * Appelée juste après le login.
     */
    public void setUserName(String email) {
        if (email == null || email.isEmpty()) return;

        // On garde juste la partie avant le @ pour faire plus joli
        String name = email.split("@")[0];
        // Met la première lettre en majuscule
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        if (welcomeLabel != null) {
            welcomeLabel.setText("Bienvenue, " + name + " !");
        }
    }

    public void setManager(PortfolioManager portfolioManager) {
        this.portfolioManager = portfolioManager;
        // Une fois le manager reçu, on met à jour la liste visuelle
        updateVisuals();
    }

    // =========================================================================
    // 2. LOGIQUE DES ONGLETS / FENETRES (PARTIE DU CAMARADE)
    // =========================================================================

    /**
     * Create the portfolio interface in a tab on the main window.
     */
    public void portfolioAsTab(Portfolio portfolio) {
        String portfolioName = portfolio.getAddress();
        logger.fine("Finding resource.");
        URL resourcePath;

        try {
            resourcePath = Objects.requireNonNull(getClass().getResource("PortfolioView.fxml"));
            logger.fine("Resource path: " + resourcePath);
        } catch (NullPointerException e) {
            logger.severe("The fxml file hasn't been found. " + e);
            throw new RuntimeException(e);
        }

        Tab portfolioContainer = new Tab(portfolioName);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openInAnotherWindow = new MenuItem("Open in another window.");

        openInAnotherWindow.setOnAction(e -> {
            portfolioContainer.getTabPane().getTabs().remove(portfolioContainer);
            portfolioAsWindow(portfolio);
        });

        contextMenu.getItems().addAll(openInAnotherWindow);
        portfolioContainer.setContextMenu(contextMenu);

        logger.info("Loading FXML: PortfolioView.fxml");
        try {
            FXMLLoader loader = new FXMLLoader(resourcePath);
            portfolioContainer.setContent(loader.load());

            PortfolioController controller = loader.getController();
            controller.setPortfolio(portfolio);
            controller.updateVisuals();

        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);
        }

        portfolioHolder.getTabs().add(portfolioContainer);
        // On sélectionne l'onglet qu'on vient de créer
        portfolioHolder.getSelectionModel().select(portfolioContainer);
    }

    /**
     * Create the portfolio interface in a separate window.
     */
    public void portfolioAsWindow(Portfolio portfolio) {
        String portfolioName = portfolio.getAddress();
        URL resourcePath;

        try {
            resourcePath = Objects.requireNonNull(getClass().getResource("PortfolioView.fxml"));
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        Stage portfolioStage = new Stage();
        AnchorPane portfolioAnchor;

        try {
            FXMLLoader loader = new FXMLLoader(resourcePath);
            portfolioAnchor = loader.load();

            PortfolioController controller = loader.getController();
            controller.setPortfolio(portfolio);
            controller.updateVisuals();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(portfolioAnchor, 1200, 800); // J'ai agrandi un peu la taille par défaut
        portfolioStage.setTitle(portfolioName);
        portfolioStage.setScene(scene);
        portfolioStage.show();
    }

    @FXML
    public void initialize() {
        // Logique de la liste (Clic droit, etc.)
        portfolioViewList.setCellFactory(lv -> new ListCell<Portfolio>() {
            private final MenuItem openInWindow = new MenuItem("Open in another window");
            private final ContextMenu contextMenu = new ContextMenu(openInWindow);

            {
                openInWindow.setOnAction(e -> {
                    Portfolio portfolio = getItem();
                    if (portfolio != null) {
                        portfolioAsWindow(portfolio);
                    }
                });

                setOnContextMenuRequested(e -> {
                    if (getItem() != null) {
                        getListView().getSelectionModel().select(getItem());
                    }
                });
            }

            @Override
            protected void updateItem(Portfolio portfolio, boolean empty) {
                super.updateItem(portfolio, empty);
                if (empty || portfolio == null) {
                    setText(null);
                    setContextMenu(null);
                } else {
                    setText(portfolio.getAddress());
                    setContextMenu(contextMenu);
                }
            }
        });

        // Double clic (ou simple clic selon config) pour ouvrir dans un onglet
        portfolioViewList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Portfolio item = portfolioViewList.getSelectionModel().getSelectedItem();
                if (item != null) {
                    portfolioAsTab(item);
                }
            }
        });
    }

    public void updateVisuals() {
        setPortfolioViewList();
    }

    public void setPortfolioViewList() {
        portfolioViewList.getItems().clear();
        if (portfolioManager != null && portfolioManager.getPortfolioList() != null) {
            for (Portfolio portfolio : portfolioManager.getPortfolioList()) {
                portfolioViewList.getItems().add(portfolio);
            }
        }
    }

    public void addPortfolio() {
        if (portfolioNameView.getText().isEmpty()) return;

        portfolioManager.createPortfolio(portfolioNameView.getText(), portfolioDescView.getText());
        updateVisuals();

        // Optionnel : vider les champs après ajout
        portfolioNameView.clear();
        portfolioDescView.clear();
    }
}