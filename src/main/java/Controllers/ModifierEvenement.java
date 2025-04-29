package Controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

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

    private final ServiceEvenement evenementService = new ServiceEvenement();

    @FXML
    private void modifierEvenement() {
        try {
            int id = Integer.parseInt(idField.getText());
            String titre = titreField.getText();
            LocalDate date = datePicker.getValue();
            String lieu = lieuField.getText();
            String statut = statutField.getText();

            if (titre.isEmpty() || date == null || lieu.isEmpty() || statut.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            Evenement updatedEvenement = new Evenement(titre, Date.valueOf(date), lieu, statut, id);
            evenementService.update(updatedEvenement);  // Assure-toi que cette méthode existe
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement modifié !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setEvenement(Evenement evenement) {
        idField.setText(String.valueOf(evenement.getId()));
        titreField.setText(evenement.getTitreEvent());

        LocalDate localDate = evenement.getDateEvent().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        datePicker.setValue(localDate);

        lieuField.setText(evenement.getLieuEvent());
        statutField.setText(evenement.getStatutEvent());
    }
}
