package org.isep.javaprojectarchusers;
import java.time.LocalDate;

public class OhlcvData {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    /**
     * Constructeur complet.
     * @param date La date de la bougie
     * @param open Le prix d'ouverture
     * @param high Le prix le plus haut de la journée
     * @param low Le prix le plus bas de la journée
     * @param close Le prix de fermeture (c'est souvent celui qu'on affiche sur une courbe simple)
     * @param volume Le volume d'échange
     */
    public OhlcvData(LocalDate date, double open, double high, double low, double close, double volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    // --- GETTERS  ---

    /** @return La date de la donnée */
    public LocalDate getDate() {
        return date;
    }

    /** @return Le prix d'ouverture */
    public double getOpen() {
        return open;
    }

    /** @return Le prix le plus haut de la journée */
    public double getHigh() {
        return high;
    }

    /** @return Le prix le plus bas de la journée */
    public double getLow() {
        return low;
    }

    /** @return Le prix de fermeture (Le plus important pour le graph) */
    public double getClose() {
        return close;
    }

    /** @return Le volume d'échange */
    public double getVolume() {
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
