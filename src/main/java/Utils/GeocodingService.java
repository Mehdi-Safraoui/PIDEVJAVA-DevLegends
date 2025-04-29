package Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeocodingService {
    private static final String GEOCODING_API = "https://nominatim.openstreetmap.org/search?format=json&q=";
    private static final String USER_AGENT = "Your Application Name";

    public static double[] getCoordinates(String address) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String encodedAddress = address.replace(" ", "+");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEOCODING_API + encodedAddress))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray jsonArray = new JSONArray(response.body());

        if (jsonArray.length() > 0) {
            JSONObject firstResult = jsonArray.getJSONObject(0);
            double lat = firstResult.getDouble("lat");
            double lon = firstResult.getDouble("lon");
            return new double[]{lat, lon};
        }
        return null;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}