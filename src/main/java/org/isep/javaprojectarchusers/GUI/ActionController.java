package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.isep.javaprojectarchusers.AlphaVantageClient;
import org.isep.javaprojectarchusers.Asset;
import org.isep.javaprojectarchusers.OhlcvData;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.chart.ChartFactory;

import java.util.ArrayList;
import java.util.logging.Logger;


public class ActionController {

    @FXML
    private AnchorPane chartPane;

    private OHLCSeries series = new OHLCSeries("Price");

    private JFreeChart chart;

    private static final Logger logger = Logger.getLogger(ActionController.class.getName());

    private Asset asset;


    /**
     *
     */
    public void updateChart() {
        AlphaVantageClient api = AlphaVantageClient.getInstance();
        logger.info("getting api infos");
        ArrayList<OhlcvData> datas = api.getMarketData(asset.getAssetName(), true);


        for (OhlcvData data:datas) {
            series.add(
                    new Day(
                            data.getDate().getDayOfMonth(),
                            data.getDate().getMonthValue(),
                            data.getDate().getYear()
                    ),
                    data.getOpen(),
                    data.getHigh(),
                    data.getLow(),
                    data.getClose()
            );
        }


    }

    public void displayCandle() {

        OHLCSeriesCollection dataset = new OHLCSeriesCollection();

        dataset.addSeries(series);

        chart = ChartFactory.createCandlestickChart(
                "Trading Chart",
                "Time",
                "Price",
                dataset,
                false
        );

        ChartViewer viewer = new ChartViewer(chart);

        chartPane.getChildren().add(viewer);

        AnchorPane.setTopAnchor(viewer, 0.0);
        AnchorPane.setBottomAnchor(viewer, 0.0);
        AnchorPane.setLeftAnchor(viewer, 0.0);
        AnchorPane.setRightAnchor(viewer, 0.0);
    }

    @FXML
    public void initialize() {

        //logger.setLevel(Level.FINE);


    }

    public void updateDisplay() {
        updateChart();
        displayCandle();
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
