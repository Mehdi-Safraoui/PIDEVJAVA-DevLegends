package Controllers.ali;

import Entities.ali.Evenement;
import Services.ali.ServiceEvenement;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.time.LocalDate;

public class ModifierEvenement {

    @FXML
    private TextField idField;

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

    private final ServiceEvenement service = new ServiceEvenement();

    @FXML
    private void modifierEvenement() {
        this.getClass().getResource("/style.css").toExternalForm();
        String idText = idField.getText().trim();
        String titre = titreField.getText().trim();
        LocalDate date = datePicker.getValue();
        String lieu = lieuField.getText().trim();
        String statut = statutField.getText().trim().toLowerCase();
        String formationIdText = formationIdField.getText().trim();

        // Contrôles de saisie
        if (idText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de l'événement ne peut pas être vide !");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

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

            // Vérification du lieu pour qu'il contienne seulement des lettres et des espaces
            if (!lieu.matches("[a-zA-Z\\s]+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le lieu ne doit contenir que des lettres et des espaces !");
                return;
            }

            // Vérification que le statut est "accepté" ou "en attente"
            if (!statut.equals("accepté") && !statut.equals("en attente")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'accepté' ou 'en attente'.");
                return;
            }

            // Validation du champ formationId
            if (formationIdText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de la formation ne peut pas être vide !");
                return;
            }

            int formationId = Integer.parseInt(formationIdText);

            // Création de l'objet Evenement et mise à jour dans la base de données
            Evenement updated = new Evenement(id, titre, Date.valueOf(date), lieu, statut, formationId);
            service.update(updated);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement modifié avec succès !");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID de l'événement et l'ID de la formation doivent être des entiers !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue, vérifiez vos saisies !");
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
