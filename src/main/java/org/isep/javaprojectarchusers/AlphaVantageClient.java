package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Client API Bourse (Adapté pour Yahoo Finance).
 * Remplace Alpha Vantage pour éviter les quotas, tout en gardant la structure du projet.
 */
public class AlphaVantageClient {

    private static AlphaVantageClient instance;

    // Constructeur privé
    private AlphaVantageClient() {}

    // Singleton
    public static synchronized AlphaVantageClient getInstance() {
        if (instance == null) instance = new AlphaVantageClient();
        return instance;
    }

    // --- CONFIGURATION YAHOO ---
    // Yahoo n'a pas besoin de clé API pour les données basiques !
    private static final String BASE_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";
    private static final String CACHE_FILE = "src/main/resources/org/isep/javaprojectarchusers/market_data_cache.json";

    public ArrayList<OhlcvData> getMarketData(String symbol, boolean isCrypto) {
        String jsonResponse = "";

        // 1. Adapter le symbole pour Yahoo (Yahoo utilise BTC-USD pour le bitcoin)
        String yahooSymbol = symbol;
        if (isCrypto && !symbol.contains("-")) {
            yahooSymbol = symbol + "-USD"; // Ex: BTC devient BTC-USD
        }

        // 2. Gestion du Cache (Identique à avant)
        // On essaie de charger le cache d'abord pour être rapide
        jsonResponse = loadCache(yahooSymbol);

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            try {
                System.out.println("[Backend] Connexion Yahoo Finance pour " + yahooSymbol + "...");
                jsonResponse = downloadFromYahoo(yahooSymbol);
                saveCache(yahooSymbol, jsonResponse); // On sauvegarde
            } catch (Exception e) {
                System.err.println("[Backend] Erreur Yahoo : " + e.getMessage());
                // Si échec, on tente de recharger un vieux cache ou Mock
                return generateMock(symbol);
            }
        }

        // 3. Parsing (C'est ici que ça change pour s'adapter à Yahoo)
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            return parseYahooJson(jsonResponse);
        }
        return generateMock(symbol);
    }

    // --- NOUVELLE MÉTHODE DE TÉLÉCHARGEMENT (YAHOO) ---
    private String downloadFromYahoo(String symbol) throws IOException {
        // URL : interval=1d (jour), range=3mo (3 mois d'historique)
        // Tu peux mettre range=1y, 5y, etc. C'est GRATUIT et ILLIMITÉ.
        String urlStr = BASE_URL + symbol + "?interval=1d&range=6mo";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        // Yahoo bloque parfois les scripts Java, on se fait passer pour un navigateur
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        conn.setConnectTimeout(5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) response.append(line);
        reader.close();
        return response.toString();
    }

    // --- NOUVELLE MÉTHODE DE PARSING (YAHOO STRUCTURE) ---
    // --- REMPLACEZ VOTRE ANCIENNE MÉTHODE PAR CELLE-CI ---
    private ArrayList<OhlcvData> parseYahooJson(String json) {
        ArrayList<OhlcvData> list = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            // 1. Accès à la structure des données Yahoo
            JsonNode chart = root.get("chart");
            if (chart == null || !chart.has("result") || chart.get("result").isNull()) return list;

            JsonNode result = chart.get("result").get(0);
            JsonNode timestamps = result.get("timestamp");

            // L'objet "indicators" contient les listes de prix séparées
            JsonNode indicators = result.get("indicators");
            JsonNode quote = indicators.get("quote").get(0);

            // 2. Récupération des 5 listes distinctes
            JsonNode opens = quote.get("open");
            JsonNode highs = quote.get("high");
            JsonNode lows = quote.get("low");
            JsonNode closes = quote.get("close");
            JsonNode volumes = quote.get("volume");

            if (timestamps == null || closes == null) return list;

            // 3. Boucle de reconstruction
            for (int i = 0; i < timestamps.size(); i++) {
                // Sécurité : si une donnée est null (jour férié/bug), on ignore
                if (timestamps.get(i).isNull() || closes.get(i).isNull()) continue;

                // Récupération des vraies valeurs distinctes
                LocalDate date = java.time.Instant.ofEpochSecond(timestamps.get(i).asLong())
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();

                double open = opens.get(i).asDouble();
                double high = highs.get(i).asDouble();
                double low = lows.get(i).asDouble();
                double close = closes.get(i).asDouble();
                long volume = volumes.get(i).asLong();

                // Création de l'objet avec les vraies dimensions
                list.add(new OhlcvData(date, open, high, low, close, volume));
            }

            // Inversion pour avoir le plus récent en premier
            java.util.Collections.reverse(list);
            System.out.println("[Backend] " + list.size() + " bougies complètes récupérées.");

        } catch (Exception e) {
            System.err.println("Erreur parsing Yahoo : " + e.getMessage());
        }
        return list;
    }

    // --- GESTION CACHE SIMPLIFIÉE (Un fichier par symbole pour éviter les mélanges) ---
    private void saveCache(String symbol, String data) {
        try {
            // On ajoute le symbole dans le nom du fichier : cache_BTC-USD.json
            String filename = CACHE_FILE.replace(".json", "_" + symbol + ".json");
            Files.createDirectories(Paths.get(filename).getParent());
            Files.write(Paths.get(filename), data.getBytes());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String loadCache(String symbol) {
        try {
            String filename = CACHE_FILE.replace(".json", "_" + symbol + ".json");
            if (Files.exists(Paths.get(filename))) {
                // Optionnel : Tu peux vérifier la date du fichier ici si tu veux
                return new String(Files.readAllBytes(Paths.get(filename)));
            }
        } catch (IOException e) { return null; }
        return null;
    }

    // --- MOCK DE SECOURS ---
    private ArrayList<OhlcvData> generateMock(String symbol) {
        ArrayList<OhlcvData> mocks = new ArrayList<>();
        double price = 150.0;
        for (int i = 0; i < 30; i++) {
            mocks.add(new OhlcvData(LocalDate.now().minusDays(i), price, price, price, price, 0));
            price *= 0.99;
        }
        return mocks;
    }
}