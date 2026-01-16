package org.isep.javaprojectarchusers;

import java.util.ArrayList;

public class TestBackend {
    public static void main(String[] args) {
        System.out.println("=== DÉBUT DU TEST BACKEND (Yahoo Finance) ===");

        // --- TEST 1 : UN INDICE ---
        testSymbol("^IXIC", false, "NASDAQ");

        System.out.println("\n------------------------------------------------\n");

        // --- TEST 2 : UNE ACTION ---
        testSymbol("AAPL", false, "APPLE");
    }

    private static void testSymbol(String symbol, boolean isCrypto, String name) {
        System.out.println("👉 Test récupération pour : " + name + " (" + symbol + ")");

        ArrayList<OhlcvData> data = AlphaVantageClient.getInstance().getMarketData(symbol, isCrypto);

        if (data.isEmpty()) {
            System.err.println("ÉCHEC : Liste vide returned (Vérifie ta connexion ou le cache)");
            return;
        }

        System.out.println("SUCCÈS : " + data.size() + " bougies récupérées.");

        // ANALYSE DE LA DERNIÈRE BOUGIE
        OhlcvData latest = data.get(0);

        System.out.println("\nAnalyse de la donnée la plus récente (" + latest.getDate() + ") :");
        System.out.printf("   High : %.2f  (Haut de mèche)\n", latest.getHigh());
        System.out.printf("   Open : %.2f\n", latest.getOpen());
        System.out.printf("   Close: %.2f\n", latest.getClose());
        System.out.printf("   Low  : %.2f  (Bas de mèche)\n", latest.getLow());


        // CORRECTION ICI : on utilise %.0f au lieu de %d car le volume est un Double
        System.out.printf("   Vol  : %.0f\n", latest.getVolume());

        boolean isFlat = (latest.getHigh() == latest.getLow()) && (latest.getOpen() == latest.getClose());

        if (isFlat) {
            System.err.println("ALERTE : La bougie est plate (High = Low). Le bug persiste !");
        } else {
            double amplitude = latest.getHigh() - latest.getLow();
            System.out.printf("VALIDATION : La bougie a une amplitude de %.2f $. Ce n'est pas un rectangle plat.\n", amplitude);
        }
    }
}