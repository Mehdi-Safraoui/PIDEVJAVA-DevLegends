package Controllers.mehdi;

import Entities.mehdi.Reclamation;
import Services.mehdi.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;

public class AjouterReclamationController {

    @FXML
    private TextField tfSujet;
    @FXML
    private TextArea taContenu;
    @FXML
    private TextField tfEmail;

    private final ReclamationService service = new ReclamationService();

    @FXML
    private void ajouterReclamation() {
        this.getClass().getResource("/AvisRecStyle.css").toExternalForm();

        String sujet = tfSujet.getText();
        String contenu = taContenu.getText();
        String email = tfEmail.getText();

        if (sujet.isEmpty() || contenu.isEmpty() || email.isEmpty() ) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        // Vérification email
        if (!isValidEmail(email)) {
            showAlert("Veuillez entrer une adresse email valide.");
            return;
        }

        Reclamation r = new Reclamation(0, sujet, contenu, new Date(), email, "Pas traitée");
        service.add(r);
        showSuccess("Réclamation ajoutée avec succès !");
        clearFields();
    }

    @FXML
    private void clearFields() {
        tfSujet.clear();
        taContenu.clear();
        tfEmail.clear();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}