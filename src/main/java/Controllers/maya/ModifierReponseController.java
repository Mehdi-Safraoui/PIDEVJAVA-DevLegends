package Controllers.maya;

import Entities.maya.Question;
import Entities.maya.Reponse;
import Services.maya.QuestionService;
import Services.maya.ReponseService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class ModifierReponseController {

    @FXML private TextField idField;
    @FXML private ComboBox<String> answerComboBox;  // Le ComboBox pour les réponses "Oui", "Non", "Parfois"
    @FXML private ComboBox<Question> questionComboBox;  // Le ComboBox pour les questions
    @FXML private TextField scoreField;
    @FXML private Label statusLabel;

    private final QuestionService questionService = new QuestionService();
    private final ReponseService reponseService = new ReponseService();
    private Reponse reponseToEdit;

    public void setReponse(Reponse reponse) {
        this.reponseToEdit = reponse;

        // Afficher l'ID dans un champ non modifiable
        idField.setText(String.valueOf(reponse.getId()));
        idField.setEditable(false);  // Rendre l'ID non modifiable

        // Prendre la réponse précédente et la sélectionner dans le ComboBox
        answerComboBox.setValue(reponse.getAnswerText());  // Utilisation de la réponse dans le ComboBox
        scoreField.setText(String.valueOf(reponse.getScore()));

        // Récupérer toutes les questions
        List<Question> questions = questionService.getAll();

        // Créer une liste contenant uniquement les textes des questions
        List<String> questionTexts = questions.stream()
                .map(Question::getQuestionText)  // Récupérer le texte de chaque question
                .collect(java.util.stream.Collectors.toList());

        // Mettre à jour le ComboBox avec les questions (affichage des textes des questions)
        questionComboBox.setItems(FXCollections.observableArrayList(questions));

        // Utiliser un CellFactory pour afficher uniquement le texte des questions dans le ComboBox
        questionComboBox.setCellFactory(lv -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getQuestionText());  // Afficher uniquement le texte de la question
                }
            }
        });

        questionComboBox.setButtonCell(new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getQuestionText());  // Afficher uniquement le texte de la question
                }
            }
        });

        // Sélectionner la question associée à la réponse
        for (Question q : questions) {
            if (q.getId() == reponse.getQuestionId()) {
                questionComboBox.setValue(q);
                break;
            }
        }
    }

    @FXML
    public void handleModifierReponse() {
        String answerText = answerComboBox.getValue();
        String scoreText = scoreField.getText();
        Question selectedQuestion = questionComboBox.getValue();  // Récupérer l'objet Question sélectionné

        // Validation des entrées
        if (answerText == null || answerText.trim().isEmpty()) {
            statusLabel.setText("Veuillez sélectionner une réponse.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreText);
            if (score < 0 || score > 10) {
                statusLabel.setText("Le score doit être un nombre entier entre 0 et 10.");
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Le score doit être un nombre entier entre 0 et 10.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        if (selectedQuestion == null) {
            statusLabel.setText("Veuillez choisir une question.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        // Mise à jour de la réponse
        reponseToEdit.setAnswerText(answerText);
        reponseToEdit.setScore(score);
        reponseToEdit.setQuestionId(selectedQuestion.getId());  // Récupérer l'ID de la question sélectionnée

        try {
            reponseService.update(reponseToEdit);
            statusLabel.setText("Réponse mise à jour avec succès !");
            statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);

            // Rafraîchir la liste des réponses si le contrôleur est défini
            if (listReponseController != null) {
                listReponseController.loadReponses();
            }

            // Fermer la fenêtre après mise à jour
            ((Stage) statusLabel.getScene().getWindow()).close();

        } catch (Exception e) {
            statusLabel.setText("Une erreur est survenue lors de la mise à jour de la réponse.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAnnuler() {
        // Fermer la fenêtre sans appliquer de modifications
        ((Stage) statusLabel.getScene().getWindow()).close();
    }

    private ListReponseController listReponseController;

    public void setListReponseController(ListReponseController controller) {
        this.listReponseController = controller;
    }
}
