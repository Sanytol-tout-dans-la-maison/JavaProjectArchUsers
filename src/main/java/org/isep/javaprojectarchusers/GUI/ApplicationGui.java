package org.isep.javaprojectarchusers.GUI;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Assets.Asset;
import org.isep.javaprojectarchusers.Assets.GeneralAssets;
import org.isep.javaprojectarchusers.Blockchain.Blockchain;
import org.isep.javaprojectarchusers.Encryption.Encryption;
import org.isep.javaprojectarchusers.Events.EventExtract;
import org.isep.javaprojectarchusers.Events.EventSave;
import org.isep.javaprojectarchusers.Events.Events;
import org.isep.javaprojectarchusers.Events.EventsManager;
import org.isep.javaprojectarchusers.GenerateGeneralAssets;
import org.isep.javaprojectarchusers.MainBackEnd;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;

import java.io.IOException;

public class ApplicationGui extends Application {

    private static final Logger logger = Logger.getLogger(ApplicationGui.class.getName());


    public static void main(String[] args) throws Exception {

        Encryption.decryptAllFiles(Encryption.getKey());
//        MainBackEnd.extractPortfolios();
//        Blockchain.extractBlockchain();
//        EventExtract.extract();
        GenerateGeneralAssets.generate();
        EventsManager.createEventsRandom(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10));
        EventsManager.sortEventsbyDate();
//

//        //à mettre à la fin
//        MainBackEnd.savePortfolios(Portfolio.getPortfolioArrayList());
//        Blockchain.saveBlockchain();
//        EventSave.save(Events.getEventList());
//        Encryption.encryptAllFiles(Encryption.getKey());

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        logger.setLevel(Level.FINE);

        logger.fine("Finding resource.");
        URL resourcePath;

        try {
            // ON CHANGE ICI : On vise l'authentification, pas la MainPage
            resourcePath = Objects.requireNonNull(getClass().getResource("authentication-interface.fxml"));
            logger.fine("Resource path: " + resourcePath);

        } catch (NullPointerException e) {
            logger.severe("The fxml file hasn't been found (authentication-interface.fxml). " + e);
            throw new RuntimeException(e);
        }

        Scene scene;
        logger.info("Loading FXML: authentication-interface.fxml");

        try {
            FXMLLoader loader = new FXMLLoader(resourcePath);

            // On charge la scène
            // On peut définir une taille fixe pour le login (ex: 600x400) ou laisser faire
            scene = new Scene(loader.load());

            // NOTE : On n'a plus besoin de récupérer le controller ici pour setManager
            // car AuthenticationController utilise ApplicationGui.pManager directement (static).

        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);
        }

        logger.fine("Setting window title.");
        primaryStage.setTitle("Login - Portfolio Manager");

        logger.fine("Putting loaded scene in the stage.");
        primaryStage.setScene(scene);

        // Optionnel : Bloquer le redimensionnement pour l'écran de login (c'est plus propre)
        primaryStage.setResizable(false);

        logger.info("Showing stage");
        primaryStage.show();
    }
}
