package org.isep.javaprojectarchusers;

import org.isep.javaprojectarchusers.Assets.*;
import org.isep.javaprojectarchusers.Events.Events;
import org.isep.javaprojectarchusers.Events.EventsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Market {

    private static Market instance;
    private ArrayList<GeneralAssets> marketAssets;

    private Market() {
        marketAssets = new ArrayList<>();

        // Initialisation des actifs avec les noms corrects
        GeneralAssets btc = new GeneralAssets("Bitcoin",  ASSET_TYPE.CryptocurrencyToken);
        btc.setValue(35000.0);
        marketAssets.add(btc);

        GeneralAssets eth = new GeneralAssets("Ethereum", ASSET_TYPE.CryptocurrencyToken);
        eth.setValue(2000.0);
        marketAssets.add(eth);

        GeneralAssets aapl = new GeneralAssets("Apple", ASSET_TYPE.Stock);
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
            for (GeneralAssets generalAsset : marketAssets) {
                generalAsset.addHistory(date, generalAsset.getValue());
            }
        }
    }

    private void applyEvent(Events event) {
        for (GeneralAssets generalAsset : marketAssets) {
            // Vérification si l'événement concerne l'actif actuel
            if (event.getAsset() != null && event.getAsset().getAssetName().equalsIgnoreCase(generalAsset.getGeneralAssetName())) {

                // CORRECTION FINALE : On utilise getValueImpact() comme défini dans Events.java
                double variation = event.getValueImpact();

                // Mise à jour de la valeur de l'actif
                generalAsset.setValue(generalAsset.getValue() * (1 + variation));
            }
        }
    }

    public ArrayList<GeneralAssets> getMarketAssets() {
        return marketAssets;
    }
}