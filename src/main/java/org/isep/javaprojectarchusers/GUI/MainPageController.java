package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

public class MainPageController {
    private static final Logger logger = Logger.getLogger(MainPageController.class.getName());

    @FXML
    private TabPane portfolioHolder;


    public void portfolioAsTab() {
        String portfolioName = "test";

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

        logger.info("Loading FXML: PortfolioView.fxml");
        try {
            portfolioContainer.setContent(FXMLLoader.load(resourcePath));



        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }

        portfolioHolder.getTabs().add(portfolioContainer);
    }



    public void portfolioAsWindow() {
        String portfolioName = "test";

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
            portfolioAnchor = FXMLLoader.load(resourcePath);


        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }
        Scene scene = new Scene(portfolioAnchor);
        portfolioStage.setScene(scene);
        portfolioStage.show();




    }

    @FXML
    public void initialize() {
        portfolioAsTab();
        portfolioAsWindow();

    }


}
