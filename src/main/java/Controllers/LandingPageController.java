package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {

    @FXML
    private void goToArticles() {
        loadScene("/AjouterArticle.fxml", "Articles de Conseil");
    }

    @FXML
    private void goToConsultations() {
        loadScene("/AfficherConsultation.fxml", "Consultations");
    }

    @FXML
    private void goToQuestion() {
        loadScene("/AjouterQuestion.fxml", "Questions");
    }

    @FXML
    private void goToReponse() {
        loadScene("/AjouterReponse.fxml", "Réponses");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

            // Fermer la page actuelle si souhaité :
            // ((Stage) titreButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListArticles() {
        try {
            // Charger le fichier FXML de la liste des articles
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListArticle.fxml"));
            AnchorPane root = loader.load();

            // Créer une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (Stage) pour afficher la scène
            Stage stage = new Stage();
            stage.setTitle("Liste des Articles");
            stage.setScene(scene);

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
