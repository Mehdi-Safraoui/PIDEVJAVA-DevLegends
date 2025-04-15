package controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import Services.ServiceEvenement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;

public class ModifierEvenement {

    @FXML
    private TextField titreEventField;

    @FXML
    private DatePicker dateEventPicker;

    @FXML
    private TextField lieuEventField;

    @FXML
    private TextField statutEventField;

    @FXML
    private TextField formationIdField;

    private Evenement evenementExistant;

    private final ServiceEvenement evenementService = new ServiceEvenement();

    public void setEvenement(Evenement evenement) {
        this.evenementExistant = evenement;

        // Remplir les champs avec les données existantes
        titreEventField.setText(evenement.getTitreEvent());
        dateEventPicker.setValue(new java.sql.Date(evenement.getDateEvent().getTime()).toLocalDate());
        lieuEventField.setText(evenement.getLieuEvent());
        statutEventField.setText(evenement.getStatutEvent());
        formationIdField.setText(evenement.getFormationId() != null ? String.valueOf(evenement.getFormationId()) : "");
    }

    @FXML
    private void modifierEvenement() {
        try {
            String titre = titreEventField.getText();
            LocalDate date = dateEventPicker.getValue();
            String lieu = lieuEventField.getText();
            String statut = statutEventField.getText();
            String formationIdText = formationIdField.getText();

            if (titre.isEmpty() || date == null || lieu.isEmpty() || statut.isEmpty()) {
                showAlert("Erreur", "Tous les champs sauf l'ID Formation sont obligatoires.");
                return;
            }

            evenementExistant.setTitreEvent(titre);
            evenementExistant.setDateEvent(Date.valueOf(date));
            evenementExistant.setLieuEvent(lieu);
            evenementExistant.setStatutEvent(statut);

            if (!formationIdText.isEmpty()) {
                evenementExistant.setFormationId(Integer.parseInt(formationIdText));
            } else {
                evenementExistant.setFormationId(null);
            }

            // Appel au service update
            evenementService.update(evenementExistant);

            showAlert("Succès", "Événement modifié avec succès !");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID de formation doit être un nombre entier.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
