package Controllers;

import Entities.Formation;
import Services.ServiceFormation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.time.LocalDate;

public class ModifierFormation {

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

    private final ServiceFormation formationService = new ServiceFormation();

    @FXML
    private void modifierFormation() {
        // Validation des champs
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

            Formation updatedFormation = new Formation(id, titre, Date.valueOf(date), lieu, statut);
            formationService.update(updatedFormation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation modifiée !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vérifiez les champs !");
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
