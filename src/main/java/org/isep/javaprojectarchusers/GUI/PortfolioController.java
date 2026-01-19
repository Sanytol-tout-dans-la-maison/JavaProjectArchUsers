package org.isep.javaprojectarchusers.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.isep.javaprojectarchusers.Accounts.AccountType;
import org.isep.javaprojectarchusers.AlphaVantageClient;
import org.isep.javaprojectarchusers.Assets.ASSET_TYPE;
import org.isep.javaprojectarchusers.Assets.Asset;
import org.isep.javaprojectarchusers.Assets.CryptocurrencyToken;
import org.isep.javaprojectarchusers.Assets.GeneralAssets;
import org.isep.javaprojectarchusers.OhlcvData;
import org.isep.javaprojectarchusers.Portfolio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PortfolioController {
    private static final Logger logger = Logger.getLogger(PortfolioController.class.getName());
    private Portfolio portfolio;
    @FXML
    private HBox portfolioSep;
    @FXML
    private ListView<Account> accountList;
    @FXML
    private ListView<Asset> assetList;
    @FXML
    private ComboBox<String> cryptoMenu;
    @FXML
    private ComboBox<String> stockMenu;
    @FXML
    private ComboBox<String> materialMenu;
    private AnchorPane actionPane = new AnchorPane();

    @FXML
    public void initialize() {
        portfolioSep.getChildren().add(actionPane);

        accountList.setCellFactory(lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                } else {
                    setText(account.getUserName() + " : " + String.format("%.2f", account.getBalance()) + " $");
                    if (account.getBalance() < 100) setStyle("-fx-text-fill: red;");
                    else setStyle("-fx-text-fill: black;");
                }
            }
        });

        accountList.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, selectedAccount) -> {
                    if (selectedAccount != null) {
                        if (selectedAccount.getAccountType() == AccountType.CHECKING) {
                            showCheckingAccount(selectedAccount);
                        } else {
                            showSavingAccount(selectedAccount);
                        }
                    }
                    ;
                }
        );

        assetList.setCellFactory(lv -> new ListCell<Asset>() {
            @Override
            protected void updateItem(Asset asset, boolean empty) {
                super.updateItem(asset, empty);
                if (empty || asset == null) {
                    setText(null);
                } else {
                    setText(getPrettyName(asset.getAssetName()));
                }
            }
        });

        assetList.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, selectedAsset) -> {
                    if (selectedAsset != null) showAsset(selectedAsset);
                }
        );

        initMarketMenus();
    }

    /**
     * Traducteur Symboles -> Noms (Mise à jour avec la liste étendue)
     */
    private String getPrettyName(String symbol) {
        switch (symbol) {
            case "BTC":
                return "Bitcoin";
            case "ETH":
                return "Ethereum";
            case "SOL":
                return "Solana";
            case "XRP":
                return "Ripple (XRP)";
            case "ADA":
                return "Cardano";
            case "DOGE":
                return "Dogecoin";
            case "DOT":
                return "Polkadot";
            case "LTC":
                return "Litecoin";
            case "LINK":
                return "Chainlink";
            case "MATIC":
                return "Polygon";


            case "AAPL":
                return "Apple";
            case "NVDA":
                return "Nvidia";
            case "TSLA":
                return "Tesla";
            case "MSFT":
                return "Microsoft";
            case "^FCHI":
                return "CAC 40";
            case "AMZN":
                return "Amazon";
            case "GOOGL":
                return "Google";
            case "META":
                return "Meta (Facebook)";
            case "NFLX":
                return "Netflix";
            case "KO":
                return "Coca-Cola";
            case "MCD":
                return "McDonald's";
            case "DIS":
                return "Disney";
            case "V":
                return "Visa";


            case "GC=F":
                return "Gold";
            case "SI=F":
                return "Silver";
            case "CL=F":
                return "Crude Oil";
            case "PL=F":
                return "Platinum";
            case "PA=F":
                return "Palladium";
            case "HG=F":
                return "Copper";
            case "NG=F":
                return "Natural Gas";
            case "ZC=F":
                return "Corn";
            case "KC=F":
                return "Coffee";

            default:
                return symbol;
        }
    }

    @FXML
    public void handleStats() {
        if (portfolio == null) return;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        AlphaVantageClient api = AlphaVantageClient.getInstance();

        double totalCash = 0.0;
        double totalAssetsValue = 0.0;
        double estimatedDailyPnL = 0.0;

        for (Account acc : portfolio.getAccountList()) {
            totalCash += acc.getBalance();
        }
        if (totalCash > 0) {
            pieChartData.add(new PieChart.Data("Cash (Available)", totalCash));
        }

        for (Asset asset : portfolio.getAssetList()) {
            double quantity = asset.getValue();
            if (quantity <= 0.000001) continue;

            boolean isCrypto = (asset instanceof CryptocurrencyToken);
            ArrayList<OhlcvData> data = api.getMarketData(asset.getAssetName(), isCrypto, false);

            if (!data.isEmpty()) {
                double currentPrice = data.getLast().getClose();
                double openPrice = data.getLast().getOpen();

                double assetVal = quantity * currentPrice;
                totalAssetsValue += assetVal;
                estimatedDailyPnL += (currentPrice - openPrice) * quantity;

                if (assetVal > 1.0) {
                    pieChartData.add(new PieChart.Data(getPrettyName(asset.getAssetName()), assetVal));
                }
            }
        }

        double globalNetWorth = totalCash + totalAssetsValue;

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Portfolio Allocation");
        chart.setLabelsVisible(true);
        chart.setLegendVisible(false);

        Label totalLabel = new Label(String.format("Global Net Worth: %.2f $", globalNetWorth));
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        Label pnlLabel = new Label();
        if (estimatedDailyPnL >= 0) {
            pnlLabel.setText(String.format("Today's Trend: +%.2f $ 📈", estimatedDailyPnL));
            pnlLabel.setStyle("-fx-text-fill: #27ae60;");
        } else {
            pnlLabel.setText(String.format("Today's Trend: %.2f $ 📉", estimatedDailyPnL));
            pnlLabel.setStyle("-fx-text-fill: #c0392b;");
        }
        pnlLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label detailsLabel = new Label(String.format("(Cash: %.2f $ | Assets: %.2f $)", totalCash, totalAssetsValue));
        detailsLabel.setStyle("-fx-text-fill: gray;");

        VBox statsBox = new VBox(5);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.getChildren().addAll(totalLabel, pnlLabel, detailsLabel);

        BorderPane layout = new BorderPane();
        layout.setCenter(chart);
        layout.setBottom(statsBox);
        BorderPane.setAlignment(statsBox, Pos.CENTER);

        actionPane.getChildren().clear();
        actionPane.getChildren().add(layout);

        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 10.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);
    }

    public void updateVisuals() {
        //assetList.refresh();
        //accountList.refresh();
        genAssetList();
        genAccountList();
        handleStats();
    }

    private void showAsset(GeneralAssets generalAssets) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("actionView.fxml"));
            actionPane.getChildren().clear();
            AnchorPane assetPane = loader.load();
            HBox.setHgrow(assetPane, Priority.ALWAYS);

            actionPane.getChildren().add(assetPane);

            ActionController controller = loader.getController();
            controller.setAsset(generalAssets);
            if (this.portfolio != null) controller.setPortfolio(this.portfolio);
            controller.setParentController(this);

            controller.updateDisplay();
        } catch (IOException e) {
            logger.severe("Erreur chargement ActionView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void genAssetList() {
        assetList.getItems().clear();
        if (portfolio != null) {
            for (Asset asset : portfolio.getAssetList()) {
                assetList.getItems().add(asset);
            }
        }
    }

    void genAccountList() {
        accountList.getItems().clear();
        if (portfolio != null) {
            for (Account account : portfolio.getAccountList()) {
                accountList.getItems().add(account);
            }
        }
    }

    // --- REMPLISSAGE DES MENUS ---
    private void initMarketMenus() {
        cryptoMenu.getItems().addAll(
                "Bitcoin", "Ethereum", "Solana", "XRP", "Cardano",
                "Dogecoin", "Polkadot", "Litecoin", "Chainlink", "Polygon"
        );
        stockMenu.getItems().addAll(
                "Apple", "Nvidia", "Tesla", "Microsoft", "CAC 40",
                "Amazon", "Google", "Meta", "Netflix", "Coca-Cola", "McDonald's", "Disney", "Visa"
        );
        materialMenu.getItems().addAll(
                "Gold", "Silver", "Crude Oil", "Platinum", "Palladium",
                "Copper", "Natural Gas", "Corn", "Coffee"
        );
    }

    @FXML
    public void handleCryptoSelection() {
        handleSelection(cryptoMenu, stockMenu, materialMenu, true);
    }

    @FXML
    public void handleStockSelection() {
        handleSelection(stockMenu, cryptoMenu, materialMenu, false);
    }

    @FXML
    public void handleMaterialSelection() {
        handleSelection(materialMenu, stockMenu, cryptoMenu, false);
    }

    // --- SELECTION ET MAPPING DES SYMBOLES ---
    private void handleSelection(ComboBox<String> active, ComboBox<String> other1, ComboBox<String> other2, boolean isCrypto) {
        String selectedName = active.getValue();
        if (selectedName == null) return;

        other1.getSelectionModel().clearSelection();
        other2.getSelectionModel().clearSelection();

        String symbol = switch (selectedName) {
            // Cryptos
            case "Bitcoin" -> "BTC";
            case "Ethereum" -> "ETH";
            case "Solana" -> "SOL";
            case "XRP" -> "XRP";
            case "Cardano" -> "ADA";
            case "Dogecoin" -> "DOGE";
            case "Polkadot" -> "DOT";
            case "Litecoin" -> "LTC";
            case "Chainlink" -> "LINK";
            case "Polygon" -> "MATIC";

            // Actions
            case "Apple" -> "AAPL";
            case "Nvidia" -> "NVDA";
            case "Tesla" -> "TSLA";
            case "Microsoft" -> "MSFT";
            case "CAC 40" -> "^FCHI";
            case "Amazon" -> "AMZN";
            case "Google" -> "GOOGL";
            case "Meta" -> "META";
            case "Netflix" -> "NFLX";
            case "Coca-Cola" -> "KO";
            case "McDonald's" -> "MCD";
            case "Disney" -> "DIS";
            case "Visa" -> "V";

            // Commodities (Codes Yahoo Finance)
            case "Gold" -> "GC=F";
            case "Silver" -> "SI=F";
            case "Crude Oil" -> "CL=F";
            case "Platinum" -> "PL=F";
            case "Palladium" -> "PA=F";
            case "Copper" -> "HG=F";
            case "Natural Gas" -> "NG=F";
            case "Corn" -> "ZC=F";
            case "Coffee" -> "KC=F";

            default -> "";
        };

        if (!symbol.isEmpty()) {
            GeneralAssets tempGeneralAsset = isCrypto ? new GeneralAssets(symbol, ASSET_TYPE.CryptocurrencyToken) : new GeneralAssets(symbol, ASSET_TYPE.Stock);
            showAsset(tempGeneralAsset);
        }
    }

    @FXML
    public void handleNewAccount() {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("New Account");
        dialog.setHeaderText("Create a new Account");

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField("MyAccount");

        ComboBox<String> typeCombo = new ComboBox<>();

        typeCombo.getItems().addAll("Checking Account", "Savings Account");
        typeCombo.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Account type:"), 0, 0);
        grid.add(typeCombo, 1, 0);

        grid.add(new Label("Account name:"), 0, 1);
        grid.add(nameField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(button -> {
            if (button == createButtonType && portfolio != null) {
                String name = nameField.getText();
                String type = typeCombo.getValue();

                if ("Checking Account".equals(type)) {
                    portfolio.createCheckingAccount(name);

                } else {
                    portfolio.createSavingAccount(name);

                }


                genAccountList();
            }

        });

    }

    public void showCheckingAccount(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkingAccountView.fxml"));
            actionPane.getChildren().clear();
            AnchorPane assetPane = loader.load();
            HBox.setHgrow(assetPane, Priority.ALWAYS);

            actionPane.getChildren().add(assetPane);

            CheckingAccountController controller = loader.getController();

            controller.setAccount(account);
            controller.setPortfolioController(this);

            controller.updateDisplay();
        } catch (IOException e) {
            logger.severe("Erreur chargement ActionView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showSavingAccount(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("savingAccountView.fxml"));
            actionPane.getChildren().clear();
            AnchorPane assetPane = loader.load();
            HBox.setHgrow(assetPane, Priority.ALWAYS);

            actionPane.getChildren().add(assetPane);

            SavingAcountController controller = loader.getController();

            controller.setAccount(account);
            controller.setPortfolioController(this);

            controller.init();

            controller.updateDisplay();

        } catch (IOException e) {
            logger.severe("Erreur chargement ActionView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}