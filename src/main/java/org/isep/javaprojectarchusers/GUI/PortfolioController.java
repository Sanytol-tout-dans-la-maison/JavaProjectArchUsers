package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;
import org.isep.javaprojectarchusers.AlphaVantageClient;
import org.isep.javaprojectarchusers.OhlcvData;

import java.util.ArrayList;

public class PortfolioController {

    @FXML
    private StackPane chartContainer;

    @FXML
    public void initialize() {
    }

    @FXML
    public void loadBitcoinData() {
        loadData("BTC", true);
    }

    @FXML
    public void loadStockData() {
        loadData("IBM", false);
    }

    private void loadData(String symbol, boolean isCrypto) {
        new Thread(() -> {
            ArrayList<OhlcvData> data = AlphaVantageClient.getMarketData(symbol, isCrypto);

            Platform.runLater(() -> {
                if (data.isEmpty()) {
                    return;
                }

                final CategoryAxis xAxis = new CategoryAxis();
                xAxis.setLabel("Date");
                final NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("Prix");

                CandleStickChart chart = new CandleStickChart(xAxis, yAxis);
                chart.setTitle("Cours du " + symbol);

                chart.setCandleData(data);

                chartContainer.getChildren().clear();
                chartContainer.getChildren().add(chart);
            });
        }).start();
    }
}