package Controllers.maya;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class QuizAPIHuggingFaceController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label timerLabel;

    @FXML
    private VBox quizContainer;

    @FXML
    private Button submitButton;

    @FXML
    private Label resultLabel;

    private List<String> questionsFromApi = new ArrayList<>();
    private List<ToggleGroup> toggleGroups = new ArrayList<>();
    private Timer timer;
    private int timeSeconds = 300; // 5 minutes = 300 secondes

    private static final String API_URL = "https://api-inference.huggingface.co/models/MayaM25/innerbloom"; // Remplace ici
    private static final String API_KEY = "Bearer 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"; // Remplace ici

    //POUR TESTER
//    private static final String API_KEY = "Bearer hf_XXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
//    private static final String API_URL = "https://api-inference.huggingface.co/models/bigscience/bloomz-560m";

    @FXML
    public void initialize() {
        fetchQuestionsFromApi();
    }

    private void fetchQuestionsFromApi() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", API_KEY);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Payload pour demander un quiz
                String payload = "{\"inputs\":\"Génère un quiz de santé mentale avec 5 questions.\"}";
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    parseQuestions(response.toString());
                } else {
                    showError("Erreur API", "Code erreur: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur de connexion", "Impossible de contacter l'API.");
            }
        }).start();
    }

//    //POUR TESTER
//private void fetchQuestionsFromApi() {
//    new Thread(() -> {
//        try {
//            URL url = new URL(API_URL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Authorization", API_KEY);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            String payload = "{\"inputs\": \"Génère 5 questions simples sur la santé mentale.\"}";
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = payload.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//                StringBuilder response = new StringBuilder();
//                String responseLine;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                parseQuestions(response.toString());
//            } else {
//                showError("Erreur API", "Code erreur: " + responseCode);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showError("Erreur de connexion", "Impossible de contacter l'API.");
//        }
//    }).start();
//}


    private void parseQuestions(String jsonResponse) {
        Platform.runLater(() -> {
            try {
                JSONArray array = new JSONArray(jsonResponse);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (obj.has("generated_text")) {
                        String generated = obj.getString("generated_text");
                        String[] questions = generated.split("\n");
                        for (String q : questions) {
                            if (!q.trim().isEmpty()) {
                                questionsFromApi.add(q.trim());
                            }
                        }
                    }
                }

                displayQuestions();
                startTimer();

            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur parsing", "Format inattendu de l'API.");
            }
        });
    }

    private void displayQuestions() {
        for (String questionText : questionsFromApi) {
            VBox questionBox = new VBox(5);
            Label questionLabel = new Label(questionText);
            questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            ToggleGroup toggleGroup = new ToggleGroup();

            RadioButton option1 = new RadioButton("Oui");
            option1.setToggleGroup(toggleGroup);

            RadioButton option2 = new RadioButton("Parfois");
            option2.setToggleGroup(toggleGroup);

            RadioButton option3 = new RadioButton("Non");
            option3.setToggleGroup(toggleGroup);

            questionBox.getChildren().addAll(questionLabel, option1, option2, option3);
            quizContainer.getChildren().add(questionBox);

            toggleGroups.add(toggleGroup);
        }
    }

    private void startTimer() {
        timer = new Timer();
        int totalTime = 300; // 300 secondes = 5 minutes
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timeSeconds--;
                    timerLabel.setText("Temps restant : " + (timeSeconds / 60) + "m " + (timeSeconds % 60) + "s");
                    progressBar.setProgress((totalTime - timeSeconds) / (double) totalTime);

                    if (timeSeconds <= 0) {
                        timer.cancel();
                        showTimesUpAlert();
                        submitButton.setDisable(true);
                    }
                });
            }
        }, 1000, 1000);
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        int score = 0;

        // Calcul du score basé sur les réponses
        for (ToggleGroup group : toggleGroups) {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected != null) {
                String answer = selected.getText();
                if (answer.equals("Oui")) {
                    score += 0;
                } else if (answer.equals("Parfois")) {
                    score += 3;
                } else if (answer.equals("Non")) {
                    score += 5;
                }
            }
        }

        // Appel de la méthode pour afficher le résultat avec emoji
        showResultPopup(score);
    }


    private String calculateMentalState(int score) {
        if (score <= 7) {
            return "Heureux";
        } else if (score <= 15) {
            return "Neutre";
        } else {
            return "Triste";
        }
    }

    private void showTimesUpAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Temps écoulé");
        alert.setHeaderText("Temps écoulé !");
        alert.setContentText("Vous pouvez soumettre vos réponses.");
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

    private void showResultPopup(int score) {
        String mentalState = calculateMentalState(score);
        String emoji = "";
        String message = "";

        // Choisir l'emoji et le message basé sur l'état mental
        switch (mentalState) {
            case "Heureux":
                emoji = "😊";
                message = "Votre état mental est excellent ! Continuez comme ça!";
                break;
            case "Neutre":
                emoji = "😐";
                message = "Votre état mental est neutre. Il est bon de prendre soin de vous.";
                break;
            case "Triste":
                emoji = "😢";
                message = "Votre état mental est triste. N'oubliez pas qu'il est toujours possible de s'améliorer.";
                break;
            default:
                emoji = "🤔";
                message = "État mental non défini, mais vous êtes unique!";
                break;
        }

        // Créer l'alert avec un emoji et un message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultat du Quiz");
        alert.setHeaderText("Votre résultat : " + emoji);
        alert.setContentText(message);

        // Afficher l'alert
        alert.showAndWait();
    }

}
