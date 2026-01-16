package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.Asset;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
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

    private PortfolioManager portfolioManager;


    /**
     * Create the portfolio interface in a tab on the main window.
     *
     * @param portfolio The portfolio to display.
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
    }


    /**
     * Create the portfolio interface in a separate window.
     *
     * @param portfolio The portfolio to display.
     */
    public void portfolioAsWindow(Portfolio portfolio) {
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

        Stage portfolioStage = new Stage();
        AnchorPane portfolioAnchor;


        logger.info("Loading FXML: PortfolioView.fxml");
        try {

            FXMLLoader loader = new FXMLLoader(resourcePath);

            portfolioAnchor = loader.load();

            PortfolioController controller = loader.getController();

            controller.setPortfolio(portfolio);
            controller.updateVisuals();


        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }


        Scene scene = new Scene(portfolioAnchor, 800, 500);

        portfolioStage.setTitle(portfolioName);
        portfolioStage.setScene(scene);

        portfolioStage.show();

    }

    @FXML
    public void initialize() {

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

        portfolioViewList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Portfolio item = portfolioViewList.getSelectionModel().getSelectedItem();

                if (item != null) {
                    portfolioAsTab(item);

                }

            }

        });

    }

    public void setManager(PortfolioManager portfolioManager) {
        this.portfolioManager = portfolioManager;
    }

    public void updateVisuals() {
        setPortfolioViewList();
    }

    public void setPortfolioViewList() {
        portfolioViewList.getItems().clear();

        for (Portfolio portfolio : portfolioManager.getPortfolioList()) {
            portfolioViewList.getItems().add(portfolio);
        }


    }

    public void addPortfolio() {
        portfolioManager.createPortfolio(portfolioNameView.getText(), portfolioDescView.getText());
        updateVisuals();
    }

}
