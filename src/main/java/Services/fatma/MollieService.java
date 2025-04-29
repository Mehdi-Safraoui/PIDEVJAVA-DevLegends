package Services.fatma;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class MollieService {

    private static final String API_KEY = "test_KMh68gwm3MchDwV4MU2wgk74xmHscV";

    public static String createMolliePayment(String amount) {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://api.mollie.com/v2/payments")
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .body("{"
                            + "\"amount\": {\"currency\": \"EUR\", \"value\": \"" + amount + "\"},"
                            + "\"description\": \"Commande complément alimentaire\","
                            + "\"redirectUrl\": \"https://example.org/merci\""
                            + "}")
                    .asJson();

            if (response.getStatus() == 201) {
                JSONObject json = response.getBody().getObject();
                String checkoutUrl = json.getJSONObject("_links")
                        .getJSONObject("checkout")
                        .getString("href");
                return checkoutUrl;
            } else {
                System.out.println("Erreur Mollie : " + response.getStatus() + " - " + response.getBody());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception lors de la création du paiement Mollie : " + e.getMessage());
            return null;
        }
    }
}
