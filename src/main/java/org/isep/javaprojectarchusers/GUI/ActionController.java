package org.isep.javaprojectarchusers.GUI;

import javafx.event.ActionEvent;
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
    private GeneralAssets generalAsset;
    private Portfolio portfolio;
    private PortfolioController parentController;
    private double lastClosingPrice = 0.0;

    @FXML
    public void updateChart() {
        AlphaVantageClient api = AlphaVantageClient.getInstance();
        boolean isCrypto = (generalAsset instanceof CryptocurrencyToken);
        ArrayList<OhlcvData> datas = api.getMarketData(generalAsset.getGeneralAssetName(), isCrypto, false);

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
        chart = ChartFactory.createCandlestickChart(generalAsset.getGeneralAssetName() + " Chart", "Time", "Price", dataset, false);
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
        if (portfolio == null || generalAsset == null) return;
        double quantityOwned = 0.0;
        for (Asset a : portfolio.getAssetList()) {
            if (a.getAssetName().equals(generalAsset.getGeneralAssetName())) {
                quantityOwned = a.getValue();
                break;
            }
        }
        double valueOwned = quantityOwned * lastClosingPrice;
        assetInfoLabel.setText(String.format("You own: %.4f | Value: %.2f $", quantityOwned, valueOwned));
    }

    public void setAsset(GeneralAssets asset) {
        this.generalAsset = asset;
    }

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

    public void buyExistingAsset(Asset asset, Account account) {
        PortfolioManager.buyAsset(this.portfolio.getAddress(), asset, portfolio.getAccount(accountPicker.getValue()));
    }

    public void buyNewAsset(Account account) {
        PortfolioManager.buyAsset(this.portfolio.getAddress(), this.generalAsset.getGeneralAssetName(), this.generalAsset.getGeneralAssetType(), account);
        assetInfoLabel.setText("You own: " + portfolio.getNumberOfAssets(generalAsset.getGeneralAssetName()) + " | Value: " + generalAsset.getValue());
    }

    public boolean sellAsset() {
        boolean b = PortfolioManager.sellAsset(this.portfolio.getAddress(), generalAsset.getGeneralAssetName(), portfolio.getAccount(accountPicker.getValue()));
        assetInfoLabel.setText("You own: " + portfolio.getNumberOfAssets(generalAsset.getGeneralAssetName()) + " | Value: " + generalAsset.getValue());
        return b;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void buyAsset(ActionEvent actionEvent) {
        buyNewAsset(portfolio.getAccount(accountPicker.getValue()));
    }
}