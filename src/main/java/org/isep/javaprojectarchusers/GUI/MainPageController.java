package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

public class MainPageController {
    private static final Logger logger = Logger.getLogger(MainPageController.class.getName());

    @FXML
    private TabPane portfolioHolder;



    public void loadPage() {
        logger.fine("Finding resource.");
        URL resourcePath;

        try {
            resourcePath = Objects.requireNonNull(getClass().getResource("PortfolioView.fxml"));
            logger.fine("Resource path: " + resourcePath);

        } catch (NullPointerException e) {
            logger.severe("The fxml file hasn't been found. "+ e);
            throw new RuntimeException(e);

        }


        logger.info("Loading FXML: PortfolioView.fxml");
        try {
            Tab splitLayout = FXMLLoader.load(resourcePath);

            portfolioHolder.getTabs().add(splitLayout);

        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }
    }

    @FXML
    public void initialize() {
        loadPage();

    }


}
