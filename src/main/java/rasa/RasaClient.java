package rasa;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RasaClient {

    public static String sendMessageToRasa(String userMessage) {
        try {
            URL url = new URL("http://localhost:5005/webhooks/rest/webhook");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Construire le JSON comme Rasa l'attend
            String jsonInputString = "{\"sender\": \"user\", \"message\": \"" + userMessage + "\"}";

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Lire la réponse
            java.io.InputStream is = conn.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"text\":\"Erreur serveur (" + e.getMessage() + ")\"}";
        }
    }
}
