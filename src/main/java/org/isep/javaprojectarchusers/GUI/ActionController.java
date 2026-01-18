package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.isep.javaprojectarchusers.Assets.*;
import org.isep.javaprojectarchusers.*;
import org.isep.javaprojectarchusers.Accounts.Account;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ActionController {

    @FXML
    private AnchorPane chartPane;
    @FXML
    private ChoiceBox<String> accountPicker;
    @FXML
    private TextField amount;
    @FXML
    private Label assetInfoLabel;

    private OHLCSeries series = new OHLCSeries("Price");
    private JFreeChart chart;
    private GeneralAssets asset;
    private Portfolio portfolio;
    private PortfolioController parentController;
    private double lastClosingPrice = 0.0;

    @FXML
    public void updateChart() {
        AlphaVantageClient api = AlphaVantageClient.getInstance();
        boolean isCrypto = (asset instanceof CryptocurrencyToken);
        // true pour forcer le refresh quand on clique sur le bouton refresh
        ArrayList<OhlcvData> datas = api.getMarketData(asset.getGeneralAssetName(), isCrypto, false);

        series.clear();
        if (!datas.isEmpty()) {
            for (OhlcvData data : datas) {
                series.add(new Day(data.getDate().getDayOfMonth(), data.getDate().getMonthValue(), data.getDate().getYear()),
                        data.getOpen(), data.getHigh(), data.getLow(), data.getClose());
            }
            lastClosingPrice = datas.getLast().getClose();
        }
    }

    public void displayCandle() {
        OHLCSeriesCollection dataset = new OHLCSeriesCollection();
        dataset.addSeries(series);
        chart = ChartFactory.createCandlestickChart(asset.getGeneralAssetName() + " Chart", "Time", "Price", dataset, false);
        ChartViewer viewer = new ChartViewer(chart);
        chartPane.getChildren().clear();
        chartPane.getChildren().add(viewer);
        AnchorPane.setTopAnchor(viewer, 0.0);
        AnchorPane.setBottomAnchor(viewer, 0.0);
        AnchorPane.setLeftAnchor(viewer, 0.0);
        AnchorPane.setRightAnchor(viewer, 0.0);
    }

    public void updateDisplay() {
        updateChart();
        displayCandle();
        updateOwnedInfo();
    }

    private void updateOwnedInfo() {
        if(portfolio == null || asset == null) return;
        double quantityOwned = 0.0;
        for(Asset a : portfolio.getAssetList()) {
            if(a.getAssetName().equals(asset.getGeneralAssetName())) {
                quantityOwned = a.getValue();
                break;
            }
        }
        double valueOwned = quantityOwned * lastClosingPrice;
        assetInfoLabel.setText(String.format("You own: %.4f | Value: %.2f $", quantityOwned, valueOwned));
    }

    public void setAsset(GeneralAssets asset) { this.asset = asset; }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        if (accountPicker != null && portfolio != null) {
            accountPicker.getItems().clear();
            for (Account acc : portfolio.getAccountList()) {
                accountPicker.getItems().add(acc.getUserName());
            }
            if (!accountPicker.getItems().isEmpty()) accountPicker.getSelectionModel().selectFirst();
        }
    }

    public void setParentController(PortfolioController parentController) {
        this.parentController = parentController;
    }

    // --- LOGIQUE ACHAT ---
    // Dans ActionController.java

//    @FXML
//    public void buyAsset() {
//        processTransaction(true);
//    }
//
//    private void processTransaction(boolean isBuying) {
//        try {
//            if (amount.getText().isEmpty() || lastClosingPrice <= 0) {
//                showAlert("Error", "Check amount or internet connection.");
//                return;
//            }
//
//            double amountMoney = Double.parseDouble(amount.getText());
//            double quantity = amountMoney / lastClosingPrice;
//            String accountName = accountPicker.getValue();
//            if(accountName == null) {
//                showAlert("Error", "Please select an account first.");
//                return;
//            }
//
//            Account account = portfolio.getAccount(accountName);
//
//            // Préparation de l'asset
//            Asset transAsset = (asset instanceof CryptocurrencyToken) ?
//                    new CryptocurrencyToken(asset.getAssetName()) : new Stock(asset.getAssetName(), 0.0);
//            transAsset.setValue(quantity);
//
//            boolean success = false;
//
//            if (isBuying) {
//                // --- CORRECTION BUDGET ---
//                // On tente de retirer l'argent.
//                // La méthode withdraw renvoie TRUE si ça marche, FALSE si fonds insuffisants.
//                boolean canPay = account.withdraw(amountMoney);
//
//                if (!canPay) {
//                    // ECHEC : Pas assez d'argent
//                    showAlert("Insufficient Funds",
//                            "You tried to spend " + amountMoney + "$ but you only have " + account.getBalance() + "$");
//                    return; // On arrête tout, la transaction ne se fait pas.
//                }
//
//                // Si on est ici, c'est que l'argent a été retiré de l'objet Account.
//                // On peut lancer l'achat dans le Manager.
//                success = PortfolioManager.buyAsset(portfolio.getAddress(), transAsset, account);
//
//            } else {
//                // Vente (Sell)
//                success = PortfolioManager.sellAsset(portfolio.getAddress(), transAsset, account);
//                if(success) {
//                    account.deposit(amountMoney); // On crédite le compte
//                } else {
//                    showAlert("Error", "Not enough assets to sell!");
//                    return;
//                }
//            }
//
//            if (success) {
//                showAlert("Success", (isBuying ? "Bought " : "Sold ") + String.format("%.4f", quantity) + " units.");
//                updateOwnedInfo();
//                if (parentController != null) parentController.updateVisuals(); // Met à jour le solde affiché
//                amount.clear();
//            }
//
//        } catch (NumberFormatException e) {
//            showAlert("Input Error", "Invalid number.");
//        }
//    }
//
//    // --- LOGIQUE VENTE ---
//    @FXML
//    public void sellAsset() { // Assure-toi que ton FXML a onAction="#sellAsset" sur le bouton Sell
//        processTransaction(false);
//    }
//

    public void buyExistingAsset(String address, Asset asset, Account account){
        PortfolioManager.buyAsset(address, asset, account);
    }

    public void buyNewAsset(String address, String assetName, ASSET_TYPE assetType, Account account){
        PortfolioManager.buyAsset(address, assetName, assetType, account);
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}