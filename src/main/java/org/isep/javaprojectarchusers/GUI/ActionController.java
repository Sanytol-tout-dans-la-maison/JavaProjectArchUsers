package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.chart.ChartFactory;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ActionController {

    @FXML
    private AnchorPane chartPane;

    private OHLCSeries series = new OHLCSeries("Price");;
    private JFreeChart chart;

    private static final Logger logger = Logger.getLogger(ActionController.class.getName());




    /**Adds multiples entries in the OHLCSeries.
     * @param elements an array of array containing the key elements for OHLC:
     *                 <ul>
     *                 <li>{@link Day}: Day of the transaction</li>
     *                 <li>{@link Double}: Open</li>
     *                 <li>{@link Double}: High</li>
     *                 <li>{@link Double}: Low</li>
     *                 <li>{@link Double}: Close</li>
     *                 </ul>
     */
    public void addMultiplesOHLCData(Object[][] elements) {
        for (Object[] OHLCdata:elements) {
            series.add(
                    (Day) OHLCdata[0],
                    (Double) OHLCdata[1],
                    (Double) OHLCdata[2],
                    (Double) OHLCdata[3],
                    (Double) OHLCdata[4]
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
        logger.fine("creating OHLC");
        Object[][] elements = {
                {new Day(8,1,2005),2.0,5.0,1.0,4.0},
                {new Day(7,1,2005),7.0,9.0,5.0,6.0}
        };

        logger.fine("Adding the data in the series");
        addMultiplesOHLCData(elements);

        logger.info("Displaying the candle");
        displayCandle();

    }

}
