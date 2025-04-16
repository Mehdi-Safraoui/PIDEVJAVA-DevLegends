package Controllers;

import Entities.Question;
import Entities.Reponse;
import Services.QuestionService;
import Services.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AjouterReponseController {

    @FXML
    private TextField answerTextField;

    @FXML
    private ComboBox<Question> questionComboBox;

    @FXML
    private ComboBox<String> answerComboBox;

    @FXML
    private TextField scoreField;

    @FXML
    private Label statusLabel;

    private final QuestionService questionService = new QuestionService();
    private final ReponseService reponseService = new ReponseService();

//    @FXML
//    public void initialize() {
//        // Initialiser les options de réponse
//        ObservableList<String> answerOptions = FXCollections.observableArrayList("Oui", "Non", "Parfois");
//        answerComboBox.setItems(answerOptions);
//
//        // Initialiser les questions dans le ComboBox
//        List<Question> questions = questionService.getAll();
//        questionComboBox.setItems(FXCollections.observableArrayList(questions));
//    }

//    @FXML
//    private void handleAjouterReponse() {
//        String answerText = answerComboBox.getValue();  // Récupère la valeur sélectionnée dans le ComboBox
//        String scoreText = scoreField.getText();
//        Question selectedQuestion = questionComboBox.getValue();
//
//        // Validation
//        if (answerText == null || answerText.trim().length() < 5) {
//            statusLabel.setText("La réponse doit contenir au moins 5 caractères.");
//            return;
//        }
//
//        if (selectedQuestion == null) {
//            statusLabel.setText("Veuillez sélectionner une question.");
//            return;
//        }
//
//        int score;
//        try {
//            score = Integer.parseInt(scoreText);
//            if (score < 0 || score > 10) {
//                statusLabel.setText("Le score doit être entre 0 et 10.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            statusLabel.setText("Le score doit être un nombre valide.");
//            return;
//        }
//
//        // Création et enregistrement
//        Reponse reponse = new Reponse(answerText, selectedQuestion.getId(), score);
//        reponseService.add(reponse);
//
//        statusLabel.setText("Réponse ajoutée avec succès !");
//        clearFields();
//    }



        @FXML
        public void initialize() {
            // Initialiser les options de réponse
            ObservableList<String> answerOptions = FXCollections.observableArrayList("Oui", "Non", "Parfois");
            answerComboBox.setItems(answerOptions);

            // Initialiser les questions dans le ComboBox
            List<Question> questions = questionService.getAll();
            questionComboBox.setItems(FXCollections.observableArrayList(questions));

            // Utilisation d'un CellFactory pour afficher uniquement le texte de la question
            questionComboBox.setCellFactory(param -> new ListCell<Question>() {
                @Override
                protected void updateItem(Question item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getQuestionText()); // Affiche uniquement le texte de la question
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
                        setText(item.getQuestionText()); // Affiche uniquement le texte de la question dans le bouton du ComboBox
                    }
                }
            });
        }

        @FXML
        public void handleAjouterReponse() {
            String answerText = answerComboBox.getValue();  // Récupère la valeur sélectionnée dans le ComboBox
            String scoreText = scoreField.getText();
            Question selectedQuestion = questionComboBox.getValue();

            // Validation
            if (answerText == null || answerText.trim().isEmpty()) {
                statusLabel.setText("Veuillez sélectionner une réponse.");
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                return;
            }

            if (selectedQuestion == null) {
                statusLabel.setText("Veuillez choisir une question.");
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

            // Création de la nouvelle réponse
            Reponse newReponse = new Reponse();
            newReponse.setAnswerText(answerText);
            newReponse.setScore(score);
            newReponse.setQuestionId(selectedQuestion.getId());

            // Tentative d'ajout de la réponse
            try {
                reponseService.add(newReponse);
                statusLabel.setText("Réponse ajoutée avec succès !");
                statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);

                // Réinitialiser les champs après ajout
                answerComboBox.getSelectionModel().clearSelection();
                scoreField.clear();
                questionComboBox.getSelectionModel().clearSelection();
            } catch (Exception e) {
                statusLabel.setText("Une erreur est survenue lors de l'ajout de la réponse.");
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                e.printStackTrace(); // Optionnel: Pour afficher l'exception dans la console
            }
        }


    private void clearFields() {
        answerTextField.clear();
        scoreField.clear();
        questionComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    private void handleAfficherReponses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListReponse.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Liste des Réponses");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleAnnuler() {
        ((Stage) statusLabel.getScene().getWindow()).close();
    }
}
