package Services.maya;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class TranslationService {

    private static final String API_URL = "https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s";

    public static String translate(String text, String sourceLang, String targetLang) {
        try {
            String urlStr = String.format(API_URL,
                    java.net.URLEncoder.encode(text, "UTF-8"),
                    sourceLang,
                    targetLang);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject.getJSONObject("responseData").getString("translatedText");

        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur de traduction";
        }
    }
}
