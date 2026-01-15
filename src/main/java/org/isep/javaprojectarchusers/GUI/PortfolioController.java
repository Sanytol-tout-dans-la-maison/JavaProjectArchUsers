package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

public class PortfolioController {
    @FXML
    private HBox portfolioSep;

    private static final Logger logger = Logger.getLogger(PortfolioController.class.getName());

    @FXML
    public void initialize() {

        logger.fine("Finding resource.");
        URL resourcePath;

        try {
            resourcePath = Objects.requireNonNull(getClass().getResource("actionView.fxml"));
            logger.fine("Resource path: " + resourcePath);

        } catch (NullPointerException e) {
            logger.severe("The fxml file hasn't been found. " + e);
            throw new RuntimeException(e);

        }


        logger.info("Loading FXML: actionView.fxml");
        try {
            portfolioSep.getChildren().add(FXMLLoader.load(resourcePath));


        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }


    }

}
