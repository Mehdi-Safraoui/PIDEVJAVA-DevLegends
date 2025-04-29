package Services.maya;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class ApiQuizGenerator {
    private static final String API_URL = "https://api-inference.huggingface.co/models/MayaM25/innerbloom";  // Remplacer par le modèle de ton choix
    private static final String API_KEY = "Bearer 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"; // Remplacer par ta clé API Hugging Face

    //POUR TESTER
//    private static final String API_KEY = "Bearer hf_XXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
//    private static final String API_URL = "https://api-inference.huggingface.co/models/bigscience/bloomz-560m";

    public static void main(String[] args) {
        try {
            // 1. Demander à l'API de générer 5 questions sur la santé mentale
            String generatedQuiz = generateQuiz();
            System.out.println("Quiz généré : " + generatedQuiz);

            // 2. Envoie les réponses de l'utilisateur à l'API pour obtenir un état de santé mentale
            String userResponses = "Réponse 1 : a, Réponse 2 : b, Réponse 3 : c, Réponse 4 : d, Réponse 5 : a";
            String mentalState = analyzeMentalState(userResponses);
            System.out.println("État mental de l'utilisateur : " + mentalState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateQuiz() throws Exception {
        String prompt = "Génère un quiz de 5 questions sur la santé mentale avec plusieurs réponses possibles pour chaque question.";

        // Envoie la requête à l'API Hugging Face pour générer le quiz
        String response = sendPostRequest(API_URL, prompt);
        return response; // L'API retourne les 5 questions
    }

    public static String analyzeMentalState(String responses) throws Exception {
        String prompt = "Voici les réponses d'un utilisateur au quiz de santé mentale : " + responses + "\nAnalyse l'état mental de l'utilisateur et retourne un diagnostic.";

        // Envoie la requête à l'API pour analyser les réponses et obtenir un diagnostic
        String response = sendPostRequest(API_URL, prompt);
        return response; // L'API retourne le diagnostic basé sur les réponses
    }

    private static String sendPostRequest(String urlString, String prompt) throws Exception {
        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setDoOutput(true);

        // Données envoyées dans la requête
        String jsonInputString = "{\"inputs\": \"" + prompt + "\"}";

        // Envoi de la requête
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Récupérer la réponse de l'API
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}

