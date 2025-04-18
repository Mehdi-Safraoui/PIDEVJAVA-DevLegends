package Controllers.maya;

import Entities.maya.Question;
import Services.maya.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterQuestionController {

    @FXML
    private TextField questionTextField;

    @FXML
    private ComboBox<String> answerTypeComboBox;

    @FXML
    private TextField pointsTextField;

    @FXML
    private Label statusLabel;

    private final QuestionService questionService = new QuestionService();

    public void initialize() {
        // Options disponibles pour le type de réponse
        answerTypeComboBox.getItems().addAll("Oui/Non", "Parfois/Jamais", "Échelle 1-5");

        // Saisie uniquement numérique dans le champ points
        pointsTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                pointsTextField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void handleAddQuestion(ActionEvent event) {
        String questionText = questionTextField.getText().trim();
        String answerType = answerTypeComboBox.getValue();
        String pointsText = pointsTextField.getText().trim();

        // Validation des champs
        if (questionText.isEmpty() || answerType == null || pointsText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs requis", "Tous les champs doivent être remplis.");
            return;
        }

        if (questionText.length() > 5) {
            showAlert(Alert.AlertType.WARNING, "Texte trop court", "La question doit contenir au moins 5 caractères.");
            return;
        }

        if (answerType.length() < 5) {
            showAlert(Alert.AlertType.WARNING, "Type de réponse invalide", "Le type de réponse doit contenir au moins 5 caractères.");
            return;
        }

        int points;
        try {
            points = Integer.parseInt(pointsText);
            if (points < 0) {
                showAlert(Alert.AlertType.WARNING, "Valeur invalide", "Les points doivent être un entier positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Le champ Points doit contenir un nombre entier.");
            return;
        }

        // Création et enregistrement de la question
        Question question = new Question(questionText, answerType, points);
        questionService.add(question);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Question ajoutée avec succès !");

        // Réinitialisation des champs
        questionTextField.clear();
        answerTypeComboBox.setValue(null);
        pointsTextField.clear();
    }

    @FXML
    private void handleAfficherListeQuestions(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ListQuestion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Liste des Questions");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affichage d'alerte générique
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
