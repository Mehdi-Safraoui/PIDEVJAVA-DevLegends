package Controllers;

import Services.ServiceFormation;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SupprimerFormation {

    @FXML
    private TextField idField;

    private final ServiceFormation formationService = new ServiceFormation();

    @FXML
    private void supprimerFormation() {
        // Contrôle de saisie pour l'ID
        try {
            int id = Integer.parseInt(idField.getText());
            Entities.Formation formation = new Entities.Formation();
            formation.setId(id);
            formationService.delete(formation);
            boolean success = true; // or set success conditionally if needed based on other logic
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Formation supprimée !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Formation introuvable !");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "ID invalide !");
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
