package Controllers.maya;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HuggingFaceAPI {

    private static final String API_URL = "https://api-inference.huggingface.co/models/distilbert-base-uncased";
    private static final String API_KEY = "Bearer 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"; // Remplacez par votre clé API

    public static void main(String[] args) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Corps de la requête
        String payload = "{\"inputs\":\"Bonjour\"}";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Lire la réponse
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response.toString());
        } else {
            System.out.println("Erreur API: " + responseCode);
        }
    }
}

