//UTILISE OPENAI DONT LES QUOTA SONT EPUISEE!!
package Controllers.maya;

import Entities.maya.Question;
import Entities.maya.Reponse;
import Services.maya.QuizGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import java.util.*;

public class ApiQuizController {
    @FXML
    private VBox quizContainer;

    private final Map<Question, ToggleGroup> toggleGroupMap = new HashMap<>();
    private final QuizGenerator quizGenerator = new QuizGenerator();
    private List<Question> questions = new ArrayList<>();

    @FXML
    private Label modeLabel;

//    @FXML
//    public void initialize() {
//        showLoadingIndicator();
//
//        // Créez une variable finale pour le QuizGenerator
//        final QuizGenerator generator = this.quizGenerator
//        new Thread(() -> {
//            try {
//                List<Question> generatedQuestions = quizGenerator.generateMentalHealthQuiz(5);
//
//                Platform.runLater(() -> {
//                    quizContainer.getChildren().clear();
//
//                    if (generatedQuestions == null || generatedQuestions.isEmpty()) {
//                        showWarning("Mode local activé - Impossible de charger depuis l'API");
//                        generatedQuestions = quizGenerator.generateLocalQuiz(5);
//                    }
//
//                    this.questions = generatedQuestions;
//
//                    if (questions.isEmpty()) {
//                        showError("Aucune question disponible");
//                        return;
//                    }
//
//                    for (Question question : questions) {
//                        addQuestionToUI(question);
//                    }
//                });
//
//            } catch (Exception e) {
//                Platform.runLater(() -> {
//                    try {
//                        quizContainer.getChildren().clear();
//                        showWarning("Mode local activé suite à une erreur: " + e.getMessage());
//                        List<Question> localQuestions = quizGenerator.generateLocalQuiz(5);
//                        this.questions = localQuestions;
//
//                        for (Question question : localQuestions) {
//                            addQuestionToUI(question);
//                        }
//                    } catch (Exception ex) {
//                        showError("Erreur critique: " + ex.getMessage());
//                    }
//                });
//            }
//        }).start();
//    }


//    @FXML
//    public void initialize() {
//        showLoadingIndicator();
//
//        // Solution 1: Utiliser directement le champ final (recommandé)
//        new Thread(() -> {
//            try {
//                final List<Question>[] generatedQuestions = new List[]{quizGenerator.generateMentalHealthQuiz(5)};
//
//                Platform.runLater(() -> {
//                    quizContainer.getChildren().clear();
//
//                    if (generatedQuestions[0] == null || generatedQuestions[0].isEmpty()) {
//                        showWarning("Mode local activé - Impossible de charger depuis l'API");
//                        generatedQuestions[0] = quizGenerator.generateLocalQuiz(5);
//                    }
//
//                    this.questions = generatedQuestions[0];
//
//                    if (questions.isEmpty()) {
//                        showError("Aucune question disponible");
//                        return;
//                    }
//
//                    for (Question question : questions) {
//                        addQuestionToUI(question);
//                    }
//                });
//
//            } catch (Exception e) {
//                Platform.runLater(() -> {
//                    try {
//                        quizContainer.getChildren().clear();
//                        showWarning("Mode local activé suite à une erreur: " + e.getMessage());
//                        List<Question> localQuestions = quizGenerator.generateLocalQuiz(5);
//                        this.questions = localQuestions;
//
//                        for (Question question : localQuestions) {
//                            addQuestionToUI(question);
//                        }
//                    } catch (Exception ex) {
//                        showError("Erreur critique: " + ex.getMessage());
//                    }
//                });
//            }
//        }).start();
//    }

    @FXML
    public void initialize() {
        showLoadingIndicator();

        new Thread(() -> {
            try {
                final List<Question>[] generatedQuestions = new List[]{quizGenerator.generateMentalHealthQuiz(5)};

                Platform.runLater(() -> {
                    quizContainer.getChildren().clear();

                    if (generatedQuestions[0] == null || generatedQuestions[0].isEmpty()) {
                        showLocalFallbackAlert();  // Nouvelle méthode pour l'alerte spécifique
                        generatedQuestions[0] = quizGenerator.generateLocalQuiz(5);
                        modeLabel.setText("Mode de génération : Local");  // Mise à jour du label
                    } else {
                        modeLabel.setText("Mode de génération : API");  // Mise à jour du label
                    }

                    this.questions = generatedQuestions[0];

                    if (questions.isEmpty()) {
                        showError("Aucune question disponible");
                        return;
                    }

                    for (Question question : questions) {
                        addQuestionToUI(question);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    try {
                        quizContainer.getChildren().clear();
                        showLocalFallbackAlert();  // Aussi utilisé pour les erreurs
                        List<Question> localQuestions = quizGenerator.generateLocalQuiz(5);
                        this.questions = localQuestions;
                        modeLabel.setText("Mode de génération : Local");  // Mise à jour du label

                        for (Question question : localQuestions) {
                            addQuestionToUI(question);
                        }
                    } catch (Exception ex) {
                        showError("Erreur critique: " + ex.getMessage());
                    }
                });
            }
        }).start();
    }

    // Nouvelle méthode pour l'alerte de fallback
    private void showLocalFallbackAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mode local activé");
        alert.setHeaderText("Le quiz sera généré localement");
        alert.setContentText("L'API n'est pas disponible pour le moment.\n" +
                "Vous utilisez la version locale du quiz.");
        alert.showAndWait();
    }

    private void showLoadingIndicator() {
        quizContainer.getChildren().clear();
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);
        Label loadingLabel = new Label("Chargement du quiz...");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        VBox loadingBox = new VBox(10, progress, loadingLabel);
        loadingBox.setAlignment(javafx.geometry.Pos.CENTER);
        quizContainer.getChildren().add(loadingBox);
    }

    private void addQuestionToUI(Question question) {
        VBox questionBox = new VBox(10);
        questionBox.setStyle("-fx-padding: 15px; -fx-background-color: #f8f8f8; -fx-border-radius: 5px;");

        Label questionLabel = new Label(question.getQuestionText());
        questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-wrap-text: true;");

        ToggleGroup toggleGroup = new ToggleGroup();
        VBox answersBox = new VBox(5);

        for (Reponse reponse : question.getReponses()) {
            RadioButton radioButton = new RadioButton(reponse.getAnswerText());
            radioButton.setUserData(reponse.getScore());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setStyle("-fx-font-size: 13px; -fx-wrap-text: true;");
            radioButton.setMaxWidth(Double.MAX_VALUE);
            answersBox.getChildren().add(radioButton);
        }

        questionBox.getChildren().addAll(questionLabel, answersBox);
        toggleGroupMap.put(question, toggleGroup);
        quizContainer.getChildren().add(questionBox);
    }

    @FXML
    private void handleSubmit() {
        // Vérification des réponses
        for (Map.Entry<Question, ToggleGroup> entry : toggleGroupMap.entrySet()) {
            if (entry.getValue().getSelectedToggle() == null) {
                showError("Veuillez répondre à la question: " + entry.getKey().getQuestionText());
                return;
            }
        }

        // Calcul du score
        int totalScore = calculateTotalScore();
        showResult(totalScore);
    }

    private int calculateTotalScore() {
        int score = 0;
        for (ToggleGroup group : toggleGroupMap.values()) {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            score += (int) selected.getUserData();
        }
        return score;
    }

    private void showResult(int totalScore) {
        int maxPossibleScore = questions.size() * 4;
        String evaluation = evaluateMentalHealth(totalScore, maxPossibleScore);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultats");
        alert.setHeaderText("Votre score: " + totalScore + "/" + maxPossibleScore);
        alert.setContentText(evaluation);
        alert.getDialogPane().setPrefSize(400, 250);
        alert.showAndWait();
    }

    private String evaluateMentalHealth(int score, int maxScore) {
        double percentage = (double) score / maxScore * 100;

        if (percentage <= 30) {
            return "Excellent état mental!\nContinuez vos bonnes habitudes.";
        } else if (percentage <= 60) {
            return "Bon état mental général.\nQuelques points à améliorer.";
        } else if (percentage <= 80) {
            return "État moyen.\nEnvisagez d'en parler à un professionnel.";
        } else {
            return "État préoccupant.\nNous vous recommandons de consulter un spécialiste.";
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}