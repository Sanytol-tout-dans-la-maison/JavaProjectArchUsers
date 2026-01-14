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

    private static final String API_KEY = "LKAC3T7BRU8XTY5X";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";

    public static ArrayList<OhlcvData> getMarketData(String symbol, boolean isCrypto) {
        ArrayList<OhlcvData> dataList = new ArrayList<>();
        String function = isCrypto ? "DIGITAL_CURRENCY_DAILY" : "TIME_SERIES_DAILY";
        String symbolParam = isCrypto ? "&symbol=" + symbol + "&market=USD" : "&symbol=" + symbol;
        String urlStr = BASE_URL + "function=" + function + symbolParam + "&apikey=" + API_KEY;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());

            String timeSeriesKey = isCrypto ? "Time Series (Digital Currency Daily)" : "Time Series (Daily)";
            JsonNode timeSeries = root.get(timeSeriesKey);

            if (timeSeries == null) {
                System.err.println("Erreur API : Pas de données trouvées pour " + symbol);
                return dataList;
            }

            Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String dateStr = entry.getKey();
                JsonNode stats = entry.getValue();

                double open, high, low, close, volume;

                if (isCrypto) {
                    open = stats.get("1a. open (USD)").asDouble();
                    high = stats.get("2a. high (USD)").asDouble();
                    low = stats.get("3a. low (USD)").asDouble();
                    close = stats.get("4a. close (USD)").asDouble();
                    volume = stats.get("5. volume").asDouble();
                } else {
                    open = stats.get("1. open").asDouble();
                    high = stats.get("2. high").asDouble();
                    low = stats.get("3. low").asDouble();
                    close = stats.get("4. close").asDouble();
                    volume = stats.get("5. volume").asDouble();
                }

                dataList.add(new OhlcvData(LocalDate.parse(dateStr), open, high, low, close, volume));
            }

            Collections.reverse(dataList);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}