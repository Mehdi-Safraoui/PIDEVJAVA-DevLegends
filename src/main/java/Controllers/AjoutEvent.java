package Controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
        String formationIdText = formationIdField.getText().trim(); // Le champ formationId n'est pas validé

        // Vérification des champs vides
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

        // Vérifier que le lieu contient uniquement des lettres et espaces
        if (!lieu.matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le lieu ne doit contenir que des lettres et des espaces !");
            return;
        }

        // Vérifier que le statut est "accepté" ou "en attente"
        if (!statut.equals("accepté") && !statut.equals("en attente")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'accepté' ou 'en attente'.");
            return;
        }

        // Le champ formationId n'est pas validé ici, on suppose que l'utilisateur entre un entier valide

        // Création et ajout
        Evenement evenement = new Evenement(
                titre,
                Date.valueOf(date),
                lieu,
                statut,
                Integer.parseInt(formationIdText)  // on convertit directement l'ID Formation en entier
        );

        evenementService.add(evenement);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
        clearForm();
    }

    @FXML
    private void allerModifierPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvent.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Modifier un événement");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir la page de modification.");
        }
    }

    @FXML
    private void supprimerEvenementPopup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Supprimer un événement");
        dialog.setHeaderText("Suppression d'un événement");
        dialog.setContentText("Entrez l'ID de l'événement à supprimer :");

        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Êtes-vous sûr ?");
                confirm.setContentText("Voulez-vous vraiment supprimer l'événement #" + id + " ?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean success = evenementService.delete(id);
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé !");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Erreur", "Événement introuvable !");
                        }
                    }
                });
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "ID invalide !");
            }
        });
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
}
