package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.Events.Events;
import org.isep.javaprojectarchusers.Events.EventsManager;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class MainPageController {
    private static final Logger logger = Logger.getLogger(MainPageController.class.getName());

    @FXML
    private TabPane portfolioHolder;
    @FXML
    private ListView<Portfolio> portfolioViewList;
    @FXML
    private TextField portfolioNameView;
    @FXML
    private TextField portfolioDescView;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label eventLabel;


    Map<Portfolio, ArrayList<Tab>> tabsMap = new HashMap<>();
    Map<Portfolio, ArrayList<Stage>> windowMap = new HashMap<>();

    public void setUserName(String email) {
        if (email == null || email.isEmpty()) return;

        String name = email.split("@")[0];
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        if (welcomeLabel != null) {
            welcomeLabel.setText("Bienvenue, " + name + " !");
        }
    }

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

        tabsMap.computeIfAbsent(portfolio, k -> new ArrayList<Tab>()).add(portfolioContainer);


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
        portfolioHolder.getSelectionModel().select(portfolioContainer);
    }

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

        windowMap.computeIfAbsent(portfolio, k -> new ArrayList<Stage>()).add(portfolioStage);

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
        displayEvent();
        portfolioViewList.setCellFactory(lv -> new ListCell<Portfolio>() {
            private final MenuItem openInWindow = new MenuItem("Open in another window");
            private final MenuItem deletePortfolio = new MenuItem("Delete");
            private final ContextMenu contextMenu = new ContextMenu(openInWindow, deletePortfolio);


            {
                openInWindow.setOnAction(e -> {
                    Portfolio portfolio = getItem();

                    if (portfolio != null) {
                        portfolioAsWindow(portfolio);

                    }

                });

                deletePortfolio.setOnAction(e -> {
                    Portfolio portfolio = getItem();

                    if (portfolio != null) {
                        delPortfolio(portfolio);
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

        portfolioViewList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Portfolio item = portfolioViewList.getSelectionModel().getSelectedItem();
                if (item != null) {
                    portfolioAsTab(item);
                }
            }
        });
    }

    private void delPortfolio(Portfolio portfolio) {
        Alert delAlert = new Alert(Alert.AlertType.CONFIRMATION);
        delAlert.setTitle("Confirm deletion");
        delAlert.setHeaderText("Are you sure?");
        delAlert.setContentText("This action is definitive. It cannot be undone");

        Optional<ButtonType> result = delAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            PortfolioManager.removePortfolio(portfolio);
            portfolioViewList.getItems().remove(portfolio);


            List<Tab> tabs = tabsMap.remove(portfolio);
            if (tabs != null) {
                portfolioHolder.getTabs().removeAll(tabs);

            }

            List<Stage> windows = windowMap.remove(portfolio);
            if (windows != null) {
                windows.forEach(Stage::close);
            }


        }
    }

    public void updateVisuals() {
        setPortfolioViewList();
    }

    public void setPortfolioViewList() {
        portfolioViewList.getItems().clear();
        if (PortfolioManager.getPortfolioList() != null) {
            for (Portfolio portfolio : PortfolioManager.getPortfolioList()) {
                portfolioViewList.getItems().add(portfolio);
            }
        }
    }

    public void addPortfolio() {
        if (portfolioNameView.getText().isEmpty()) return;

        PortfolioManager.createPortfolio(portfolioNameView.getText(), portfolioDescView.getText());
        updateVisuals();
        portfolioNameView.clear();
        portfolioDescView.clear();
    }

    public void displayEvent(){
        if(EventsManager.checkEventsByDate(LocalDate.now())) {
            StringBuilder builder = new StringBuilder();
            for (Events e : EventsManager.getEventsByDate(LocalDate.now())) builder.append(e.toString()).append(", ");
            eventLabel.setText("Active Event: " + builder);
        }
        else eventLabel.setText("No active event today");
    }
}