package Controllers;

import Entities.Formation;
import Services.ServiceFormation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class AjoutFormation {

    @FXML
    private TextField titreField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField lieuField;
    @FXML
    private TextField statutField;

    private final ServiceFormation formationService = new ServiceFormation();

    @FXML
    private void ajouterFormation() {
        String titre = titreField.getText().trim();
        LocalDate date = datePicker.getValue();
        String lieu = lieuField.getText().trim();
        String statut = statutField.getText().trim();

        // Vérification champs vides
        if (titre.isEmpty() || date == null || lieu.isEmpty() || statut.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Contrôle de saisie : titre = lettres + espaces
        if (!titre.matches("[a-zA-ZÀ-ÿ\\s]{3,}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le titre doit contenir au moins 3 lettres et uniquement des lettres.");
            return;
        }

        // Contrôle : lieu = lettres + chiffres + espaces
        if (!lieu.matches("[\\wÀ-ÿ\\s-]{3,}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le lieu doit contenir au moins 3 caractères valides.");
            return;
        }

        // Contrôle : statut = seulement actif/inactif
        if (!statut.equalsIgnoreCase("Etudiant") && !statut.equalsIgnoreCase("Medecin")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'actif' ou 'inactif'.");
            return;
        }

        // Contrôle : date non passée
        if (date.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date ne peut pas être dans le passé.");
            return;
        }

        // Ajouter la formation si tout est OK
        Formation formation = new Formation(titre, Date.valueOf(date), lieu, statut);
        formationService.add(formation);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation ajoutée avec succès !");
        clearFields();
    }

    @FXML
    private void allerModifierFormationPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFormation.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Modifier une formation");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir la page de modification.");
        }
    }

    @FXML
    private void supprimerFormationPopup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Supprimer une formation");
        dialog.setHeaderText("Suppression d'une formation");
        dialog.setContentText("Entrez l'ID de la formation à supprimer :");

        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmation");
                confirm.setHeaderText("Êtes-vous sûr ?");
                confirm.setContentText("Voulez-vous vraiment supprimer la formation #" + id + " ?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        Formation formationToDelete = new Formation();
                        formationToDelete.setId(id); // Assumes Formation has a setId method
                        formationService.delete(formationToDelete);
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation supprimée !");
                    }
                });
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "ID invalide !");
            }
        });
    }


    private void clearFields() {
        titreField.clear();
        datePicker.setValue(null);
        lieuField.clear();
        statutField.clear();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
