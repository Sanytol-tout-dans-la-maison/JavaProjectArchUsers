package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Client API pour AlphaVantage (Singleton).
 * Gère la récupération, la mise en cache et la simulation des données boursières.
 */

public class AlphaVantageClient {

    // --- PARTIE 1 : SINGLETON (Architecture) ---
    private static AlphaVantageClient instance;

    // Constructeur privé pour empêcher "new AlphaVantageClient()"
    private AlphaVantageClient() {}

    /**
     * Point d'accès unique à l'instance du client (Pattern Singleton).
     * @return L'instance unique de AlphaVantageClient.
     */
    public static synchronized AlphaVantageClient getInstance() {
        if (instance == null) {
            instance = new AlphaVantageClient();
        }
        return instance;
    }

    // --- CONSTANTES ---
    private static final String API_KEY = "LWQOIBMC5YRMRFDT";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    private static final String CACHE_FILE = "market_data_cache.json"; // Notre "Base de données" locale

    public static ArrayList<OhlcvData> getMarketData(String symbol, boolean isCrypto) {
        String jsonResponse = "";

        /**
         * Récupère les données historiques du marché.
         * <p>
         * Stratégie de résilience :
         * 1. Tente l'API AlphaVantage (Online).
         * 2. Si échec, tente le Cache local (Offline).
         * 3. Si échec, génère des données Mock (Secours).
         * </p>
         *
         * @param symbol   Le symbole boursier (ex: "BTC", "IBM").
         * @param isCrypto True si c'est une crypto-monnaie.
         * @return Une liste d'objets OhlcvData prête à l'emploi.
         */

        // ÉTAPE 1 : On essaie de télécharger les nouvelles données
        try {
            System.out.println("[Backend] Tentative de connexion API...");
            jsonResponse = downloadDataFromApi(symbol, isCrypto);

            // Si l'API répond une erreur (quota), on lance une exception pour passer au cache
            if (jsonResponse.contains("Error Message") || jsonResponse.contains("Information")) {
                throw new RuntimeException("Quota API dépassé ou Erreur.");
            }

            // Si succès, on sauvegarde dans le fichier (Mise à jour du cache)
            saveCache(jsonResponse);
            System.out.println("[Backend] Données fraîches sauvegardées dans le cache.");

        } catch (Exception e) {
            System.err.println("[Backend] ⚠️ Impossible de joindre l'API (" + e.getMessage() + ")");
            // ÉTAPE 2 : Si échec, on essaie de lire le fichier local
            jsonResponse = loadCache();
        }

        // ÉTAPE 3 : On transforme le JSON (qu'il vienne du Web ou du Fichier) en Objets
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            return parseJsonData(jsonResponse, isCrypto);
        } else {
            // ÉTAPE 4 : Si même le fichier n'existe pas, on génère du faux (Dernier recours)
            System.err.println("[Backend] Cache vide. Génération de Mock.");
            return getMockData(symbol);
        }
    }

    // --- MÉTHODES UTILITAIRES ---

    // Téléchargement Web
    private static String downloadDataFromApi(String symbol, boolean isCrypto) throws IOException {
        String function = isCrypto ? "DIGITAL_CURRENCY_DAILY" : "TIME_SERIES_DAILY";
        String symbolParam = isCrypto ? "&symbol=" + symbol + "&market=USD" : "&symbol=" + symbol;
        String urlStr = BASE_URL + "function=" + function + symbolParam + "&outputsize=full&apikey=" + API_KEY;
        // Note: j'ai mis "full" pour avoir tout l'historique, remets "compact" si c'est trop lourd

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) response.append(line);
        reader.close();
        return response.toString();
    }

    // Sauvegarde dans un fichier
    private static void saveCache(String data) {
        try {
            Files.write(Paths.get(CACHE_FILE), data.getBytes());
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde cache : " + e.getMessage());
        }
    }

    // Lecture du fichier
    private static String loadCache() {
        try {
            if (Files.exists(Paths.get(CACHE_FILE))) {
                System.out.println("[Backend] 📂 Chargement depuis la base de données locale (Cache).");
                return new String(Files.readAllBytes(Paths.get(CACHE_FILE)));
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture cache.");
        }
        return null;
    }

    // Parsing JSON (Transformation Texte -> Objets Java)
    private static ArrayList<OhlcvData> parseJsonData(String json, boolean isCrypto) {
        ArrayList<OhlcvData> list = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            // Vérification si le JSON contient une erreur (même dans le cache)
            if (root.has("Note") || root.has("Information")) return new ArrayList<>();

            String timeSeriesKey = isCrypto ? "Time Series (Digital Currency Daily)" : "Time Series (Daily)";
            JsonNode timeSeries = root.get(timeSeriesKey);

            if (timeSeries == null) return list;

            Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String dateStr = entry.getKey();
                JsonNode stats = entry.getValue();

                double close = 0;
                // Logique souple pour trouver le prix de fermeture
                if (isCrypto) {
                    if(stats.has("4a. close (USD)")) close = stats.get("4a. close (USD)").asDouble();
                    else if(stats.has("4. close")) close = stats.get("4. close").asDouble();
                } else {
                    if(stats.has("4. close")) close = stats.get("4. close").asDouble();
                }

                // On simplifie pour l'exemple (tu peux tout parser si tu veux)
                if (close != 0) {
                    list.add(new OhlcvData(LocalDate.parse(dateStr), close, close, close, close, 0));
                }
            }
            Collections.reverse(list);
            System.out.println("[Backend] " + list.size() + " valeurs chargées.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Données de secours (Mathématiques)
    private static ArrayList<OhlcvData> getMockData(String symbol) {
        ArrayList<OhlcvData> mocks = new ArrayList<>();
        double price = 20000.0;
        for (int i = 0; i < 100; i++) {
            LocalDate date = LocalDate.now().minusDays(100 - i);
            double close = price * (1 + (Math.random() - 0.5) * 0.05);
            mocks.add(new OhlcvData(date, close, close, close, close, 5000));
            price = close;
        }
        return mocks;
    }
}