package Controllers.maya;

import Entities.maya.Question;
import Services.maya.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class ModifierQuestionController {

    @FXML
    private Label idLabel;

    @FXML
    private TextField questionTextField;

    @FXML
    private TextField answerTypeField;

    @FXML
    private TextField pointsField;

    private final QuestionService questionService = new QuestionService();

    private Question questionToEdit;

    private ListQuestionController listQuestionController;

    public void initialize() {
        // Filtrage : saisir uniquement des chiffres pour les points
        pointsField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                pointsField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setQuestion(Question question) {
        this.questionToEdit = question;
        idLabel.setText(String.valueOf(question.getId()));
        questionTextField.setText(question.getQuestionText());
        answerTypeField.setText(question.getAnswerType());
        pointsField.setText(String.valueOf(question.getPoints()));
    }

    @FXML
    private void handleSaveQuestion() {
        String questionText = questionTextField.getText().trim();
        String answerType = answerTypeField.getText().trim();
        String pointsText = pointsField.getText().trim();

        if (questionText.isEmpty() || answerType.isEmpty() || pointsText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs requis", "Tous les champs doivent être remplis.");
            return;
        }

        if (questionText.length() < 5) {
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

        // Mise à jour de l'objet Question
        questionToEdit.setQuestionText(questionText);
        questionToEdit.setAnswerType(answerType);
        questionToEdit.setPoints(points);

        // Mise à jour dans la base
        questionService.update(questionToEdit);

        // Message de succès
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Question modifiée avec succès !");

        // Rafraîchir la table si possible
        if (listQuestionController != null) {
            listQuestionController.refreshTable();
        }

        // Fermer la fenêtre
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }

    public void setListQuestionController(ListQuestionController listQuestionController) {
        this.listQuestionController = listQuestionController;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Annuler la modification");
        alert.setContentText("Voulez-vous vraiment annuler les modifications ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    // Méthode utilitaire pour les alertes
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
