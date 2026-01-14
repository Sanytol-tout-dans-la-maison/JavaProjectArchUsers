package org.isep.javaprojectarchusers;
import java.time.LocalDate;

public class OhlcvData {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public OhlcvData(LocalDate date, double open, double high, double low, double close, long volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public long getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "OhlcvData{" +
                "date=" + date +
                ", 1. open=" + open +
                ", 2. high=" + high +
                ", 3. low=" + low +
                ", 4. close=" + close +
                ", 5. volume=" + volume +
                '}';
    }

}
