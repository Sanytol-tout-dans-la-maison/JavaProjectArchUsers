package org.isep.javaprojectarchusers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Market {

    private static Market instance;
    private ArrayList<Asset> marketAssets;

    private Market() {
        marketAssets = new ArrayList<>();

        // Initialisation des actifs avec les noms corrects
        CryptocurrencyToken btc = new CryptocurrencyToken("Bitcoin");
        btc.setValue(35000.0);
        marketAssets.add(btc);

        CryptocurrencyToken eth = new CryptocurrencyToken("Ethereum");
        eth.setValue(2000.0);
        marketAssets.add(eth);

        Stock aapl = new Stock("Apple");
        aapl.setValue(150.0);
        marketAssets.add(aapl);
    }

    public static Market getInstance() {
        if (instance == null) {
            instance = new Market();
            instance.simulateMarketHistory();
        }
        return instance;
    }

    private void simulateMarketHistory() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        // On utilise la classe EventsManager pour classer les events par date
        EventsManager em = new EventsManager();
        HashMap<LocalDate, ArrayList<Events>> eventsMap = em.sortEventsbyDate();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            ArrayList<Events> dailyEvents = eventsMap.get(date);

            if (dailyEvents != null) {
                for (Events event : dailyEvents) {
                    applyEvent(event);
                }
            }

            // Enregistrement dans l'historique pour les graphiques
            for (Asset asset : marketAssets) {
                asset.addHistory(date, asset.getValue());
            }
        }
    }

    private void applyEvent(Events event) {
        for (Asset asset : marketAssets) {
            // Vérification si l'événement concerne l'actif actuel
            if (event.getAsset() != null && event.getAsset().getAssetName().equalsIgnoreCase(asset.getAssetName())) {

                // CORRECTION FINALE : On utilise getValueImpact() comme défini dans Events.java
                double variation = event.getValueImpact();

                // Mise à jour de la valeur de l'actif
                asset.setValue(asset.getValue() * (1 + variation));
            }
        }
    }

    public ArrayList<Asset> getMarketAssets() {
        return marketAssets;
    }
}