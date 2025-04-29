//AVEC API OPENAI!!!

package Services.maya;

import Entities.maya.Question;
import Entities.maya.Reponse;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import java.net.http.*;
import java.net.URI;
import java.util.*;

public class QuizGenerator {
    // Configuration API
    private static final boolean USE_API = true; // Mettre à true pour activer OpenAI
    private static final String API_KEY = "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";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Question> generateMentalHealthQuiz(int amount) {
        if (USE_API) {
            try {
                List<Question> apiQuestions = generateFromOpenAI(amount);
                if (!apiQuestions.isEmpty()) {
                    return apiQuestions;
                }
            } catch (Exception e) {
                System.err.println("Erreur API OpenAI: " + e.getMessage());
            }
        }
        return generateLocalQuiz(amount);
    }

    private List<Question> generateFromOpenAI(int amount) throws Exception {
        // 1. Préparer le prompt
        String prompt = String.format("""
        Génère un quiz JSON de %d questions sur la santé mentale avec:
        - Une question par item
        - 4 réponses possibles
        - Un score de 1 (positif) à 4 (négatif)
        Format: {"questions":[{"question":"...","answers":[{"text":"...","score":1},...]}]
        """, amount);

        // 2. Construire le payload
        ObjectNode payload = mapper.createObjectNode();
        payload.put("model", "gpt-3.5-turbo");
        payload.put("temperature", 0.7);

        ArrayNode messages = mapper.createArrayNode();
        ObjectNode message = mapper.createObjectNode();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        payload.set("messages", messages);

        // 3. Envoyer la requête
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());  // Affiche la réponse brute pour vérifier son format

        // 4. Parser la réponse
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode choicesNode = rootNode.path("choices");
        if (choicesNode.isArray() && choicesNode.size() > 0) {
            JsonNode messageNode = choicesNode.get(0).path("message");
            String content = messageNode.path("content").asText();
            if (content != null && !content.isEmpty()) {
                JsonNode quizNode = mapper.readTree(content);
                JsonNode questionsNode = quizNode.path("questions");

                List<Question> questions = new ArrayList<>();
                for (JsonNode qNode : questionsNode) {
                    Question question = new Question(qNode.path("question").asText());

                    List<Reponse> reponses = new ArrayList<>();
                    for (JsonNode aNode : qNode.path("answers")) {
                        reponses.add(new Reponse(
                                aNode.path("text").asText(),
                                aNode.path("score").asInt()
                        ));
                    }

                    question.setReponses(reponses);
                    questions.add(question);
                }

                return questions;
            } else {
                System.err.println("Le contenu du message est vide.");
            }
        } else {
            System.err.println("Aucun choix valide dans la réponse.");
        }

        // Si l'API échoue, retourner un quiz local par défaut
        return generateLocalQuiz(amount);
    }


    public List<Question> generateLocalQuiz(int amount) {
        List<Question> quiz = new ArrayList<>();

        // Question 1
        Question q1 = new Question("Comment vous sentez-vous ces derniers jours ?");
        q1.setReponses(Arrays.asList(
                new Reponse("Très bien", 1),
                new Reponse("Bien", 2),
                new Reponse("Moyen", 3),
                new Reponse("Pas bien", 4)
        ));

        // Question 2
        Question q2 = new Question("Comment évaluez-vous votre niveau de stress ?");
        q2.setReponses(Arrays.asList(
                new Reponse("Très faible", 1),
                new Reponse("Faible", 2),
                new Reponse("Élevé", 3),
                new Reponse("Très élevé", 4)
        ));

        // Question 3
        Question q3 = new Question("Comment dormez-vous récemment ?");
        q3.setReponses(Arrays.asList(
                new Reponse("Très bien", 1),
                new Reponse("Bien", 2),
                new Reponse("Difficilement", 3),
                new Reponse("Très mal", 4)
        ));

        // Question 4
        Question q4 = new Question("À quelle fréquence vous sentez-vous anxieux ?");
        q4.setReponses(Arrays.asList(
                new Reponse("Jamais", 1),
                new Reponse("Rarement", 2),
                new Reponse("Souvent", 3),
                new Reponse("Toujours", 4)
        ));

        // Question 5
        Question q5 = new Question("Comment gérez-vous les problèmes ?");
        q5.setReponses(Arrays.asList(
                new Reponse("Très bien", 1),
                new Reponse("Bien", 2),
                new Reponse("Difficilement", 3),
                new Reponse("Pas du tout", 4)
        ));

        Collections.addAll(quiz, q1, q2, q3, q4, q5);
        return quiz.subList(0, Math.min(amount, quiz.size()));
    }
}