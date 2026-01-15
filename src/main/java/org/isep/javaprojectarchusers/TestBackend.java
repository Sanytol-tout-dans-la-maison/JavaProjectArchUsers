package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class TestBackend {
    public static void main(String[] args) {
        System.out.println("=== TEST DU BACKEND (Données Boursières) ===");

        // Test de ta méthode
        String symbol = "BTC";
        ArrayList<OhlcvData> data = AlphaVantageClient.getMarketData(symbol, true);

        // Vérification
        if (data.isEmpty()) {
            System.err.println("ERREUR : Aucune donnée récupérée !");
        } else {
            System.out.println("SUCCÈS : Récupération de " + data.size() + " éléments.");

            // Afficher les 3 premiers et 3 derniers pour prouver que c'est cohérent
            System.out.println("\n--- Aperçu des données ---");
            System.out.println("Première donnée : " + data.get(0));
            System.out.println("Dernière donnée : " + data.get(data.size() - 1));

            System.out.println("\n--- Structure d'une donnée complète ---");
            OhlcvData sample = data.get(0);
            System.out.printf("Date: %s | Open: %.2f | High: %.2f | Low: %.2f | Close: %.2f%n",
                    sample.getDate(), sample.getOpen(), sample.getHigh(), sample.getLow(), sample.getClose());
        }
    }
}