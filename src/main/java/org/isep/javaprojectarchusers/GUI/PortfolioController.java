package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Asset;
import org.isep.javaprojectarchusers.Portfolio;
import org.isep.javaprojectarchusers.PortfolioManager;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;



public class PortfolioController {
    private Portfolio portfolio;

    @FXML
    private HBox portfolioSep;

    @FXML
    private ListView<Account> accountList;
    @FXML
    private ListView<Asset> assetList;

    private AnchorPane actionPane= new AnchorPane();


    private static final Logger logger = Logger.getLogger(PortfolioController.class.getName());

    @FXML
    public void initialize() {
    }

    public void updateVisuals() {
        genAssetList();
        genAccountList();
    }



    private void showAsset(Asset asset){

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
            FXMLLoader loader = new FXMLLoader(resourcePath);

            actionPane.getChildren().clear();
            actionPane.getChildren().add(loader.load());


            portfolioSep.getChildren().add(actionPane);

            ActionController controller = loader.getController();

            controller.setAsset(asset);


        } catch (IOException e) {
            logger.severe("IOException error during FXMLLoader.load(resourcePath): " + e);
            throw new RuntimeException(e);

        }

    }


    /** Set the portfolio this controller is assigned to.
     * @param portfolio the {@link Portfolio} the class is linked to.
     */
    public void setPortfolio(Portfolio portfolio) {
        logger.info("Portfolio set successfully");
        this.portfolio = portfolio;
    }


    /**
     * Generate the buttons to select which action to interact.
     */

    private void genAssetList() {

        for (Asset asset : portfolio.getAssetList()) {
            assetList.getItems().add(asset);
        }

        assetList.setCellFactory(lv -> new ListCell<Asset>() {
            @Override
            protected void updateItem(Asset asset, boolean empty) {
                super.updateItem(asset, empty);

                if (empty || asset == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(asset.getAssetName());
                }
            }
        });

        assetList.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, selectedAsset) -> {
                    if (selectedAsset != null) {
                        showAsset(selectedAsset);
                    }
                }
        );

    }

    private void genAccountList() {

        for (Account account : portfolio.getAccountList()) {
            accountList.getItems().add(account);
        }

        accountList.setCellFactory(lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);

                if (empty || account == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(account.getUserName());
                }
            }
        });

        accountList.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, selectedAsset) -> {
                    if (selectedAsset != null) {

                    }
                }
        );


    }


}
