package org.isep.javaprojectarchusers.GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.AccountType;
import org.isep.javaprojectarchusers.Asset;
import org.isep.javaprojectarchusers.MainBackEnd;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;

public class ApplicationGui extends Application {

    private static final Logger logger = Logger.getLogger(ApplicationGui.class.getName());
    public static PortfolioManager pManager;


    public static void main(String[] args) {

        pManager = new PortfolioManager();


        pManager.createPortfolio("testPortfolio", "Desc for the test");

        Portfolio portfolio1 = pManager.getPortfolio("testPortfolio");


        portfolio1.createCheckingAccount("test");
        Account account1 = portfolio1.getAccount("test");
        MainBackEnd.addAccount(account1);

        pManager.buyAsset("testPortfolio", new Asset("BTC", 5), account1);
        pManager.buyAsset("testPortfolio", new Asset("test2", 5), account1);

        pManager.createPortfolio("testPortfolio2", "Desc for the test");

        Portfolio portfolio2 = pManager.getPortfolio("testPortfolio2");


        portfolio2.createCheckingAccount("test2");
        Account account2 = portfolio2.getAccount("test2");
        MainBackEnd.addAccount(account2);

        pManager.buyAsset("testPortfolio2", new Asset("test3", 5), account2);
        pManager.buyAsset("testPortfolio2", new Asset("test4", 5), account2);


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.setLevel(Level.FINE);


        logger.fine("Finding resource.");
        URL resourcePath;

        try {
            resourcePath = Objects.requireNonNull(getClass().getResource("MainPageView.fxml"));
            logger.fine("Resource path: " + resourcePath);

        } catch (NullPointerException e) {
            logger.severe("The fxml file hasn't been found. " + e);
            throw new RuntimeException(e);

        }


        Scene scene;

        logger.info("Loading FXML: MainPageView.fxml");
        try {
            FXMLLoader loader = new FXMLLoader(resourcePath);


            scene = new Scene(loader.load(), 800, 500);

            MainPageController controller = loader.getController();

            controller.setManager(pManager);
            controller.updateVisuals();


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
