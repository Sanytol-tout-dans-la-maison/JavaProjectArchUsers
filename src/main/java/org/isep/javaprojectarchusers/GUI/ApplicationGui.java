package org.isep.javaprojectarchusers.GUI;

import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationGui extends Application {

    private static final Logger logger = Logger.getLogger(ApplicationGui.class.getName());



    public static void main(String[] args) {


        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        logger.setLevel(Level.FINE);


        logger.fine("Finding resource.");
        URL resourcePath;

        try {
            resourcePath = Objects.requireNonNull(getClass().getResource("MainPageView.fxml"));
            logger.fine("Resource path: " + resourcePath);

        } catch (NullPointerException e) {
            logger.severe("The fxml file hasn't been found. "+ e);
            throw new RuntimeException(e);

        }



        Scene scene;

        logger.info("Loading FXML: login-view.fxml");
        try {
            scene = new Scene(FXMLLoader.load(resourcePath));

        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }


        logger.fine("Setting window title.");
        primaryStage.setTitle("Login");

        logger.fine("Putting loaded scene in the stage.");
        primaryStage.setScene(scene);

        logger.info("Showing stage");
        primaryStage.show();
    }
}
