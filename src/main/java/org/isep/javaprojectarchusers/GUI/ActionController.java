package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.isep.javaprojectarchusers.ASSET_TYPE;
import org.isep.javaprojectarchusers.AlphaVantageClient;
import org.isep.javaprojectarchusers.Asset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.chart.ChartFactory;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
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

        api.getMarketData(asset.getAssetName(), (ASSET_TYPE.CryptocurrencyToken == asset.getAssetType()));


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

        XYPlot plot = chart.getXYPlot();

        DateAxis axis = (DateAxis) plot.getDomainAxis();

        axis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 7));

        axis.setDateFormatOverride(new SimpleDateFormat("dd MMM yyyy"));

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
