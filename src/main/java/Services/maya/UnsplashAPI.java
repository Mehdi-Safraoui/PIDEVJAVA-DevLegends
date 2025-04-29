package Services.maya;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UnsplashAPI {
    private static final String ACCESS_KEY = "RorUls5l7h1UULYyVfz1Ly2y6IuJhd7ddLdS4eqjKXE";
    private static final String BASE_URL = "https://api.unsplash.com/photos/random?client_id=%s&query=%s";

    public static String getImageUrlForEtatMental(String etatMental) throws Exception {
        String query = etatMental.toLowerCase().replace(" ", "-");
        String apiUrl = String.format(BASE_URL, ACCESS_KEY, query);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);

        return node.get("urls").get("regular").asText(); // Attention : pas .get(0) car l'API renvoie un seul objet
    }
}
