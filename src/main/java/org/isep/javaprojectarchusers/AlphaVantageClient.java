package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class AlphaVantageClient {

    private static final String API_KEY = "LKAC3T7BRU8XTY5X"; // Ta clé
    private static final String BASE_URL = "https://www.alphavantage.co/query?";

    public static ArrayList<OhlcvData> getMarketData(String symbol, boolean isCrypto) {
        ArrayList<OhlcvData> dataList = new ArrayList<>();

        // Construction de l'URL
        String function = isCrypto ? "DIGITAL_CURRENCY_DAILY" : "TIME_SERIES_DAILY";
        String symbolParam = isCrypto ? "&symbol=" + symbol + "&market=USD" : "&symbol=" + symbol;
        // on ajoute outputsize=compact pour économiser de la data (100 derniers jours)
        String urlStr = BASE_URL + "function=" + function + symbolParam + "&outputsize=compact&apikey=" + API_KEY;

        try {
            System.out.println("1. Tentative de connexion API pour " + symbol + "...");

            // --- 1. CONNEXION RESEAU ---
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5 secondes max pour se connecter
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Erreur HTTP : " + conn.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // --- 2. ANALYSE DU JSON (JACKSON) ---
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());

            // --- 3. VERIFICATION DES ERREURS API ---
            // C'est ici qu'on évite le crash !
            if (root.has("Note") || root.has("Information") || root.has("Error Message")) {
                System.err.println("⚠️ API LIMITÉE OU ERREUR : " + root.toString());
                System.out.println("-> Passage automatique aux données de simulation (Mock).");
                return getMockData(symbol);
            }

            // Récupération du nœud principal (Time Series)
            String timeSeriesKey = isCrypto ? "Time Series (Digital Currency Daily)" : "Time Series (Daily)";
            JsonNode timeSeries = root.get(timeSeriesKey);

            if (timeSeries == null) {
                System.err.println("⚠️ Pas de données 'Time Series' trouvées.");
                return getMockData(symbol);
            }

            // --- 4. PARSING DES DONNÉES RÉELLES ---
            Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String dateStr = entry.getKey();
                JsonNode stats = entry.getValue();

                double open, high, low, close, volume;

                // Utilisation de .path() au lieu de .get() pour éviter le NullPointerException
                if (isCrypto) {
                    open = stats.path("1a. open (USD)").asDouble(0);
                    high = stats.path("2a. high (USD)").asDouble(0);
                    low = stats.path("3a. low (USD)").asDouble(0);
                    close = stats.path("4a. close (USD)").asDouble(0);
                    volume = stats.path("5. volume").asDouble(0);

                    // Fallback si l'API change de format (parfois "4. close" au lieu de "4a...")
                    if (close == 0) close = stats.path("4. close").asDouble(0);
                } else {
                    open = stats.path("1. open").asDouble(0);
                    high = stats.path("2. high").asDouble(0);
                    low = stats.path("3. low").asDouble(0);
                    close = stats.path("4. close").asDouble(0);
                    volume = stats.path("5. volume").asDouble(0);
                }

                // On ajoute seulement si on a réussi à lire un prix
                if (close != 0) {
                    dataList.add(new OhlcvData(LocalDate.parse(dateStr), open, high, low, close, volume));
                }
            }

            // Remettre dans l'ordre chronologique (le JSON arrive souvent inversé)
            Collections.reverse(dataList);
            System.out.println("✅ Données API récupérées avec succès (" + dataList.size() + " jours)");

        } catch (Exception e) {
            // --- 5. GESTION DES CRASH ---
            System.err.println("❌ Erreur ou Pas d'internet : " + e.getMessage());
            System.out.println("-> Chargement des données de SECOURS.");
            return getMockData(symbol);
        }

        if (dataList.isEmpty()) return getMockData(symbol); // Sécurité ultime
        return dataList;
    }

    // --- GENERATEUR DE DONNÉES DE SECOURS (MOCK) ---
    private static ArrayList<OhlcvData> getMockData(String symbol) {
        ArrayList<OhlcvData> mocks = new ArrayList<>();
        double price = symbol.equals("BTC") ? 20000.0 : 150.0; // Prix de base selon l'actif

        // On génère 60 jours de données
        for (int i = 0; i < 60; i++) {
            LocalDate date = LocalDate.now().minusDays(60 - i);

            // Mouvement aléatoire
            double change = (Math.random() - 0.5) * (price * 0.05);
            double close = price + change;
            double open = price;
            double high = Math.max(open, close) * 1.01;
            double low = Math.min(open, close) * 0.99;
            double volume = 1000 + Math.random() * 5000;

            mocks.add(new OhlcvData(date, open, high, low, close, volume));
            price = close; // Le prix de clôture devient la base du lendemain
        }

        System.out.println("--> Données MOCK générées pour " + symbol);
        return mocks;
    }
}