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

    @Override
    protected void layoutPlotChildren() {
        if (getData().isEmpty()) return;
        Series<String, Number> series = getData().getFirst();

        for (Data<String, Number> item : series.getData()) {
            double x = getXAxis().getDisplayPosition(item.getXValue());
            double y = getYAxis().getDisplayPosition(item.getYValue());
            if (item.getNode() instanceof Candle) {
                Candle candle = (Candle) item.getNode();
                candle.update(x);
            }
        }
    }

    @Override
    protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {
        if (item.getExtraValue() instanceof OhlcvData) {
            OhlcvData ohlc = (OhlcvData) item.getExtraValue();
            Candle candle = new Candle(ohlc);
            item.setNode(candle);
            getPlotChildren().add(candle);
        }
    }

    @Override protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
        getPlotChildren().remove(item.getNode());
    }
    @Override protected void dataItemChanged(Data<String, Number> item) {}
    @Override protected void seriesAdded(Series<String, Number> series, int seriesIndex) {
        for (Data<String, Number> item : series.getData()) dataItemAdded(series, -1, item);
    }
    @Override protected void seriesRemoved(Series<String, Number> series) {
        for (Data<String, Number> item : series.getData()) dataItemRemoved(item, series);
    }

    private class Candle extends javafx.scene.Group {
        private final Line highLowLine = new Line();
        private final Path region = new Path();
        private final OhlcvData data;

        Candle(OhlcvData data) {
            this.data = data;
            if (data.getClose() < data.getOpen()) {
                highLowLine.setStyle("-fx-stroke: red;");
                region.setStyle("-fx-fill: red; -fx-stroke: red;");
            } else {
                highLowLine.setStyle("-fx-stroke: green;");
                region.setStyle("-fx-fill: green; -fx-stroke: green;");
            }
            getChildren().addAll(highLowLine, region);
        }

        void update(double x) {
            NumberAxis yAxis = (NumberAxis) getYAxis();
            double open = yAxis.getDisplayPosition(data.getOpen());
            double close = yAxis.getDisplayPosition(data.getClose());
            double high = yAxis.getDisplayPosition(data.getHigh());
            double low = yAxis.getDisplayPosition(data.getLow());
            double candleWidth = 10;

            highLowLine.setStartX(x);
            highLowLine.setEndX(x);
            highLowLine.setStartY(high);
            highLowLine.setEndY(low);

            region.getElements().clear();
            double minBody = Math.min(open, close);
            double maxBody = Math.max(open, close);

            javafx.scene.shape.MoveTo topLeft = new javafx.scene.shape.MoveTo(x - candleWidth / 2, minBody);
            javafx.scene.shape.LineTo topRight = new javafx.scene.shape.LineTo(x + candleWidth / 2, minBody);
            javafx.scene.shape.LineTo bottomRight = new javafx.scene.shape.LineTo(x + candleWidth / 2, maxBody);
            javafx.scene.shape.LineTo bottomLeft = new javafx.scene.shape.LineTo(x - candleWidth / 2, maxBody);
            javafx.scene.shape.ClosePath closePath = new javafx.scene.shape.ClosePath();

            region.getElements().addAll(topLeft, topRight, bottomRight, bottomLeft, closePath);
        }
    }
}