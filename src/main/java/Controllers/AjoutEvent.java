package Controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class AjoutEvent {

    @FXML
    private TextField titreField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField lieuField;

    @FXML
    private TextField statutField;

    @FXML
    private TextField formationIdField;

    private final ServiceEvenement evenementService = new ServiceEvenement();

    @FXML
    private void ajouterEvenement() {
        this.getClass().getResource("/style.css").toExternalForm();

        String titre = titreField.getText().trim();
        LocalDate date = datePicker.getValue();
        String lieu = lieuField.getText().trim();
        String statut = statutField.getText().trim().toLowerCase();
        String formationIdText = formationIdField.getText().trim();

        // Vérifications
        if (titre.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le titre de l'événement ne peut pas être vide !");
            return;
        }

        if (date == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de l'événement doit être sélectionnée !");
            return;
        }

        if (lieu.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le lieu de l'événement ne peut pas être vide !");
            return;
        }

        if (!lieu.matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le lieu ne doit contenir que des lettres et des espaces !");
            return;
        }

        if (!statut.equals("accepté") && !statut.equals("en attente")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'accepté' ou 'en attente'.");
            return;
        }

        if (formationIdText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de la formation ne peut pas être vide !");
            return;
        }

        int formationId;

        try {
            formationId = Integer.parseInt(formationIdText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de la formation doit être un entier !");
            return;
        }

        Evenement evenement = new Evenement(
                titre,
                Date.valueOf(date),
                lieu,
                statut,
                formationId
        );

        // Ajout
        evenementService.add(evenement);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
        clearForm();

        // Redirection vers la liste
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = (Stage) titreField.getScene().getWindow(); // récupérer la fenêtre actuelle
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de liste des événements.");
        }
    }


    private void clearForm() {
        titreField.clear();
        datePicker.setValue(null);
        lieuField.clear();
        statutField.clear();
        formationIdField.clear();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void redirigerVersListeEvenements(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Événements");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle (optionnel)
            Stage currentStage = (Stage) titreField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
