package Services.maya;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YouTubeAPI {

    private static final String API_KEY = "AIzaSyDPVKolq6JG6GgswzVQe-vWsiFEYNhv_Fg"; // Remplacez par votre clé API

    public static String searchPlaylist(String etatMental) {
        String query = buildQuery(etatMental);
        String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + query + "&type=playlist&key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
            return extractPlaylistUrl(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String buildQuery(String etatMental) {
        // Mappez l'état mental à des mots-clés pertinents pour la recherche YouTube
        switch (etatMental) {
            case "Bonne santé mentale":
                return "positive energy meditation playlist";
            case "Légère fatigue émotionnelle":
                return "relaxing music playlist";
            case "Signes d'anxiété ou de stress":
                return "stress relief music playlist";
            case "État dépressif modéré":
                return "calm music for depression";
            case "État très préoccupant":
                return "mental health support playlist";
            default:
                return "mental health playlist";
        }
    }

    private static String extractPlaylistUrl(JsonObject jsonObject) {
        if (jsonObject.has("items") && jsonObject.getAsJsonArray("items").size() > 0) {
            JsonObject item = jsonObject.getAsJsonArray("items").get(0).getAsJsonObject();
            JsonObject idObject = item.getAsJsonObject("id");
            if (idObject != null && idObject.has("playlistId")) {
                String playlistId = idObject.get("playlistId").getAsString();
                return "https://www.youtube.com/playlist?list=" + playlistId;
            }
        }
        return null;
    }

}
