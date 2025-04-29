package Controllers.maya;

import Entities.maya.Question;
import Entities.maya.Reponse;
import Entities.maya.Quiz;
import Services.maya.ApiQuizGenerator;
import Services.maya.QuizService;
import Services.maya.ReponseService;
import Services.maya.UnsplashAPI;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class QuizController {

    @FXML
    private VBox quizContainer;

    @FXML
    private Label resultLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label timerLabel;

    private final Map<Question, ToggleGroup> toggleGroupMap = new HashMap<>();
    private int timeSeconds = 60;
    private int score = 0;
    private Timeline timerTimeline;

    private QuizService quizService = new QuizService();

    @FXML
    public void initialize() {
        // Initialisation du timer et de la barre de progression
        setupTimer();
        applyFadeInAnimation(quizContainer);

        // Récupérer les questions avec leurs réponses
        ReponseService rs = new ReponseService();
        List<Question> quiz = rs.getQuestionsWithReponses(); // Récupère 10 questions avec réponses

        // Ajouter les questions et leurs réponses à l'interface
        for (Question q : quiz) {
            Label questionLabel = new Label(q.getQuestionText());
            questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            ToggleGroup group = new ToggleGroup();

            VBox questionBox = new VBox(5);
            questionBox.getChildren().add(questionLabel);

            for (Reponse r : q.getReponses()) {
                RadioButton rb = new RadioButton(r.getAnswerText());
                rb.setUserData(r.getScore());
                rb.setToggleGroup(group);
                questionBox.getChildren().add(rb);
            }

            toggleGroupMap.put(q, group);
            quizContainer.getChildren().add(questionBox);
        }
    }

    private void applyFadeInAnimation(VBox node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // Fonction pour démarrer le timer
    private void setupTimer() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeSeconds--;
            timerLabel.setText("Temps restant : " + timeSeconds + "s");
            progressBar.setProgress(timeSeconds / 60.0);

            if (timeSeconds <= 0) {
                timerTimeline.stop();
                // Utilisation de Platform.runLater() pour afficher la popup après la fin de l'animation
                Platform.runLater(this::showTimeUpPopup);
            }
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();
    }

    @FXML
    private void handleSubmit() {
        for (Map.Entry<Question, ToggleGroup> entry : toggleGroupMap.entrySet()) {
            if (entry.getValue().getSelectedToggle() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez répondre à toutes les questions !");
                alert.showAndWait();
                return;
            }
        }

        List<Reponse> reponsesChoisies = new ArrayList<>();
        for (Map.Entry<Question, ToggleGroup> entry : toggleGroupMap.entrySet()) {
            Question question = entry.getKey();
            Toggle selectedToggle = entry.getValue().getSelectedToggle();
            int score = (int) selectedToggle.getUserData();

            Reponse reponse = new Reponse();
            reponse.setQuestionId(question.getId());
            reponse.setScore(score);
            reponsesChoisies.add(reponse);
        }

        // Utiliser le service pour générer le quiz et obtenir l'état mental
        Quiz result = quizService.generateQuiz(reponsesChoisies);
        quizService.saveQuizResult(result);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ResultatQuiz.fxml"));
            Parent root = loader.load();

            // Passer le résultat à la vue ResultatQuizController
            ResultatQuizController controller = loader.getController();
            controller.setResult(result.getEtatMental(), result.getScore());

            Stage stage = (Stage) quizContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    // Afficher une popup lorsque le temps est écoulé
//    private void showTimeUpPopup() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Temps écoulé !");
//        alert.setHeaderText("Le temps est terminé !");
//        alert.setContentText("Que voulez-vous faire ?");
//
//        ButtonType quitter = new ButtonType("Quitter");
//        ButtonType recommencer = new ButtonType("Recommencer");
//        alert.getButtonTypes().setAll(quitter, recommencer);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent()) {
//            if (result.get() == quitter) {
//                ((Stage) quizContainer.getScene().getWindow()).close(); // Ferme la fenêtre
//            } else if (result.get() == recommencer) {
//                resetQuiz();
//            }
//        }
//    }

    private void showTimeUpPopup() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Temps écoulé");
            alert.setHeaderText(null);
            alert.setContentText("Le temps est écoulé !");

            ButtonType quitter = new ButtonType("Quitter");
            ButtonType recommencer = new ButtonType("Recommencer");

            alert.getButtonTypes().setAll(quitter, recommencer);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == quitter) {
                // Juste fermer l'alert, PAS besoin de fermer le stage manuellement
            } else if (result.isPresent() && result.get() == recommencer) {
                resetQuiz(); // Recharge ton quiz
            }
        });
    }

    // Réinitialiser le quiz
    private void resetQuiz() {
        quizContainer.getChildren().clear();
        timeSeconds = 60;
        score = 0;
        setupTimer(); // Redémarrer le timer
        resultLabel.setText("");
        initialize(); // Recharger les questions
    }

    //Partie AI Quiz

    @FXML
    private TextArea resultArea;  // Zone de texte pour afficher l'état mental

    @FXML
    private Button startQuizButton;  // Bouton pour commencer le quiz

    @FXML
    private Button submitAnswersButton;  // Bouton pour soumettre les réponses

    @FXML
    private void startQuiz() {
        try {
            // Générez un quiz de 5 questions via l'API
            String quiz = ApiQuizGenerator.generateQuiz();
            resultArea.setText(quiz);  // Affiche le quiz dans l'interface
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la génération du quiz.");
        }
    }

    @FXML
    private void submitAnswers() {
        try {
            // Récupère les réponses de l'utilisateur depuis l'interface (simulé ici par un texte exemple)
            String userResponses = "Réponse 1 : a, Réponse 2 : b, Réponse 3 : c, Réponse 4 : d, Réponse 5 : a";

            // Analyse les réponses via l'API
            String mentalState = ApiQuizGenerator.analyzeMentalState(userResponses);
            resultArea.setText("État mental: " + mentalState);  // Affiche l'état mental dans l'interface
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de l'analyse des réponses.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
