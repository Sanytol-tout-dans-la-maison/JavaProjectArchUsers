package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

    private ArrayList<Portfolio> portfolioList;


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

        // Attach context menu to tab
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


        Scene scene = new Scene(portfolioAnchor);

        portfolioStage.setTitle(portfolioName);
        portfolioStage.setScene(scene);

        portfolioStage.show();

    }

    @FXML
    public void initialize() {
    }

    public void setPortfolioList(ArrayList<Portfolio> portfolioList) {
        this.portfolioList = portfolioList;
    }

    public void updateVisuals() {
        for (Portfolio portfolio : portfolioList) {
            portfolioAsTab(portfolio);
        }
    }

    public void test() {
        System.out.println("switch");
    }
}
