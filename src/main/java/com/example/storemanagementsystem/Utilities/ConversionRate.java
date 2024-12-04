package com.example.storemanagementsystem.Utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ConversionRate {
    TreeMap<String, String> rates;
    String lastUpdateUtc;
    String nextUpdateUtc;

    public TreeMap<String, String> getRates() {
        return rates;
    }

    public String getLastUpdateUtc() {
        return lastUpdateUtc;
    }

    public String getNextUpdateUtc() {
        return nextUpdateUtc;
    }

    public void parseJson(String response, String base){
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        lastUpdateUtc = jsonObject.get("time_last_update_utc").getAsString();
        nextUpdateUtc = jsonObject.get("time_next_update_utc").getAsString();
        JsonObject conversionRate = jsonObject.getAsJsonObject("conversion_rates");
        rates = new TreeMap<>();
        for (Map.Entry<String, JsonElement> entry : conversionRate.entrySet()) {
            String currency = entry.getKey();
            String rate = entry.getValue().getAsString();
            if(currency.equals(base)) continue;
            rates.put(currency,rate);
        }
    }

    public ConversionRate(String base) {
        String url = "https://v6.exchangerate-api.com/v6/33164f7275ef751bede51ffc/latest/" + base;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            parseJson(response.body(),base);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
