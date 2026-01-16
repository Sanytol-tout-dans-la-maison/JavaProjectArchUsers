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


public class AlphaVantageClient {

    private static AlphaVantageClient instance;

    /**
     * URL de base pour l'API non-officielle de Yahoo Finance (Chart V8).
     */
    private static final String BASE_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";

    /**
     * Chemin de base pour le fichier de cache. Le symbole sera inséré avant l'extension .json.
     */
    private static final String CACHE_FILE_BASE = "src/main/resources/org/isep/javaprojectarchusers/market_data_cache.json";

    // Constructeur privé pour le Singleton
    private AlphaVantageClient() {}

    /**
     * Récupère l'instance unique du client (Singleton).
     * @return L'instance de {@code AlphaVantageClient}.
     */
    public static synchronized AlphaVantageClient getInstance() {
        if (instance == null) instance = new AlphaVantageClient();
        return instance;
    }

    /**
     * Récupère les données de marché (OHLCV) pour un actif donné.
     * La méthode vérifie d'abord si une copie locale (cache) existe pour éviter les appels réseaux inutiles.
     * Si {@code forceRefresh} est activé, le cache est ignoré et une nouvelle requête est envoyée.
     *
     * @param symbol Le symbole de l'actif (ex: "BTC", "AAPL").
     * @param isCrypto Indique s'il s'agit d'une crypto-monnaie (pour adapter le symbole Yahoo).
     * @param forceRefresh Si {@code true}, force le téléchargement et écrase le cache existant.
     * @return Une liste d'objets {@link OhlcvData} contenant l'historique des cours.
     */
    public ArrayList<OhlcvData> getMarketData(String symbol, boolean isCrypto, boolean forceRefresh) {
        String jsonResponse = "";

        // 1. Adapter le symbole pour Yahoo (Yahoo utilise "BTC-USD" pour le bitcoin, mais "AAPL" pour Apple)
        String yahooSymbol = symbol;
        if (isCrypto && !symbol.contains("-")) {
            yahooSymbol = symbol + "-USD";
        }

        // 2. Gestion du CACHE (Lecture)
        // Si on ne force pas le refresh, on essaie de lire le cache d'abord
        if (!forceRefresh) {
            String cachedData = loadCache(yahooSymbol);
            if (cachedData != null && !cachedData.isEmpty()) {
                System.out.println("[Backend] Chargement depuis le cache pour " + yahooSymbol);
                return parseYahooJson(cachedData);
            }
        }

        // 3. Téléchargement (Si forceRefresh est vrai OU si le cache est vide)
        try {
            System.out.println("[Backend] Tentative de téléchargement pour " + yahooSymbol + "...");
            jsonResponse = downloadFromYahoo(yahooSymbol);

            // On sauvegarde le nouveau résultat dans le cache pour la prochaine fois
            saveCache(yahooSymbol, jsonResponse);

            return parseYahooJson(jsonResponse);

        } catch (Exception e) {
            System.err.println("Erreur téléchargement Yahoo : " + e.getMessage());

            // 4. Fallback (Secours)
            // Si le téléchargement échoue (ex: pas internet), on essaie de charger le vieux cache même si on avait demandé un refresh.
            String oldCache = loadCache(yahooSymbol);
            if (oldCache != null) {
                System.out.println("[Backend] Récupération du cache existant suite à l'échec réseau.");
                return parseYahooJson(oldCache);
            }

            // Si vraiment rien ne marche, on génère de fausses données pour ne pas faire planter l'interface
            System.err.println("[Backend] Aucune donnée disponible. Génération de Mock.");
            return generateMock(symbol);
        }
    }

    // --- METHODES PRIVEES ---

    /**
     * Effectue la requête HTTP GET vers Yahoo Finance.
     * @param symbol Le symbole formaté pour Yahoo.
     * @return La réponse JSON brute sous forme de String.
     * @throws IOException Si la connexion échoue.
     */
    private String downloadFromYahoo(String symbol) throws IOException {
        // On demande 6 mois de données (range=6mo) avec intervalle 1 jour (interval=1d)
        String urlString = BASE_URL + symbol + "?range=6mo&interval=1d";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // Timeout 5 sec
        conn.setReadTimeout(5000);

        // Yahoo demande parfois un User-Agent pour ne pas bloquer les scripts Java
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HttpResponseCode: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    /**
     * Convertit le JSON brut de Yahoo en objets Java.
     * @param json La chaîne JSON.
     * @return Liste d'OhlcvData.
     */
    private ArrayList<OhlcvData> parseYahooJson(String json) {
        ArrayList<OhlcvData> list = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            // Navigation dans l'arbre JSON de Yahoo : chart -> result -> [0]
            if (!root.has("chart") || !root.path("chart").has("result") || root.path("chart").path("result").isEmpty()) {
                return list;
            }

            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode timestamps = result.path("timestamp");
            JsonNode quote = result.path("indicators").path("quote").get(0);

            if (timestamps.isMissingNode() || quote.isMissingNode()) return list;

            JsonNode opens = quote.path("open");
            JsonNode highs = quote.path("high");
            JsonNode lows = quote.path("low");
            JsonNode closes = quote.path("close");
            JsonNode volumes = quote.path("volume");

            for (int i = 0; i < timestamps.size(); i++) {
                // Conversion Timestamp Unix -> LocalDate
                long unixSeconds = timestamps.get(i).asLong();
                LocalDate date = Instant.ofEpochSecond(unixSeconds).atZone(ZoneId.of("UTC")).toLocalDate();

                // Ignorer les données incomplètes (jours fériés ou bugs API)
                if (opens.get(i).isNull() || closes.get(i).isNull()) continue;

                double open = opens.get(i).asDouble();
                double high = highs.get(i).asDouble();
                double low = lows.get(i).asDouble();
                double close = closes.get(i).asDouble();
                double volume = volumes.get(i).asDouble();

                list.add(new OhlcvData(date, open, high, low, close, volume));
            }

        } catch (Exception e) {
            System.err.println("Erreur parsing Yahoo JSON : " + e.getMessage());
        }
        return list;
    }

    // --- GESTION CACHE ---

    private void saveCache(String symbol, String data) {
        try {
            // Construit le nom de fichier : market_data_cache_BTC-USD.json
            String filename = CACHE_FILE_BASE.replace(".json", "_" + symbol + ".json");
            Files.createDirectories(Paths.get(filename).getParent());
            Files.write(Paths.get(filename), data.getBytes());
        } catch (IOException e) {
            System.err.println("Impossible de sauvegarder le cache : " + e.getMessage());
        }
    }

    private String loadCache(String symbol) {
        try {
            String filename = CACHE_FILE_BASE.replace(".json", "_" + symbol + ".json");
            if (Files.exists(Paths.get(filename))) {
                return new String(Files.readAllBytes(Paths.get(filename)));
            }
        } catch (IOException e) { return null; }
        return null;
    }

    // --- MOCK (DONNÉES FAUSSES DE SECOURS) ---
    private ArrayList<OhlcvData> generateMock(String symbol) {
        ArrayList<OhlcvData> mocks = new ArrayList<>();
        double price = 150.0; // Prix de départ arbitraire
        for (int i = 0; i < 30; i++) {
            // Génère une fluctuation aléatoire
            double fluctuation = (Math.random() * 10) - 5;
            double open = price;
            double close = price + fluctuation;
            double high = Math.max(open, close) + 2;
            double low = Math.min(open, close) - 2;

            mocks.add(new OhlcvData(
                    LocalDate.now().minusDays(30 - i),
                    open, high, low, close, 1000 + i * 10
            ));

            price = close; // Le prix de clôture devient le prix d'ouverture du lendemain
        }
        return mocks;
    }
}