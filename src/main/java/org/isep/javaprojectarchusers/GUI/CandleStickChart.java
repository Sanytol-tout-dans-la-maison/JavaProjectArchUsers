package org.isep.javaprojectarchusers.GUI;

import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import java.util.ArrayList;
import org.isep.javaprojectarchusers.OhlcvData;

public class CandleStickChart extends XYChart<String, Number> {
    public CandleStickChart(Axis<String> xAxis, Axis<Number> yAxis) {
        super(xAxis, yAxis);
        setAnimated(false);
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
    }

    public void setCandleData(ArrayList<OhlcvData> dataList) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (OhlcvData day : dataList) {
            series.getData().add(new XYChart.Data<>(day.getDate().toString(), day.getClose(), day));
        }
        getData().clear();
        getData().add(series);
    }

   

}
