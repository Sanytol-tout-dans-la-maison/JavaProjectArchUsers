package org.isep.javaprojectarchusers.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.chart.ChartFactory;


public class ActionController {

    @FXML
    private AnchorPane chartPane;

    private OHLCSeries series = new OHLCSeries("Price");;
    private JFreeChart chart;




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
        Object[][] elements = {
                {new Day(8,1,2005),2,5,1,4},
                {new Day(7,1,2005),7,9,5,6}
        };

        addMultiplesOHLCData(elements);

        displayCandle();

    }

}
