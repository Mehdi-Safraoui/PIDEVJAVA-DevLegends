package Services.maya;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;



public class TherapistService {

    private static final String API_KEY = "pk.434f340f4366ca4f8e8c2c0c4bac43e4"; // Mets ici ta clé API LocationIQ
    private static final String BASE_URL = "https://eu1.locationiq.com/v1/search.php";

//    public String findNearbyTherapist(double latitude, double longitude) {
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            // Construire l'URL de l'API LocationIQ pour rechercher des lieux à proximité
//            String url = BASE_URL + "?key=" + API_KEY + "&q=psychologue&format=json&lat=" + latitude + "&lon=" + longitude + "&radius=5000";
//
//            // Faire l'appel HTTP
//            HttpGet request = new HttpGet(url);
//            try (CloseableHttpResponse response = client.execute(request)) {
//                String jsonResponse = EntityUtils.toString(response.getEntity());
//
//                // Parser la réponse JSON
//                JSONArray results = new JSONArray(jsonResponse);
//                if (results.length() > 0) {
//                    // Obtenir le premier thérapeute trouvé
//                    JSONObject firstResult = results.getJSONObject(0);
//                    String name = firstResult.optString("display_name");
//                    return "Thérapeute trouvé : " + name;
//                } else {
//                    return "Aucun thérapeute trouvé à proximité.";
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Erreur lors de la recherche d'un thérapeute.";
//        }
//    }
}
