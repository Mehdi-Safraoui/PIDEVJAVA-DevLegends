package Controllers.mehdi;

import Entities.mehdi.Avis;
import Services.mehdi.AvisService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;

public class AjouterAvisController {

    @FXML
    private TextField sujetAvisField;
    @FXML
    private TextArea contenuAvisField;
    @FXML
    private TextField emailAvisField;
    @FXML
    private Spinner<Integer> noteAvisField;

    private final AvisService avisService = new AvisService();

    @FXML
    private void handleAddAvis() {
        this.getClass().getResource("/AvisRecStyle.css").toExternalForm();

        String sujet = sujetAvisField.getText();
        String contenu = contenuAvisField.getText();
        String email = emailAvisField.getText();
        int note = noteAvisField.getValue();

        if (sujet.isEmpty() || contenu.isEmpty() || email.isEmpty()) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        if (contenu.length() < 10) {
            showAlert("Le contenu de l'avis doit contenir au moins 10 caractères.");
            return;
        }


        // Vérification de l'email
        if (!isValidEmail(email)) {
            showAlert("Veuillez entrer une adresse email valide.");
            return;
        }

        // Création de l'avis
        Avis avis = new Avis(0, sujet, contenu, note, new Date(), email, "Non traité");
        avisService.add(avis);
        showSuccess("Avis ajouté avec succès !");
        clearFields();
    }

    @FXML
    private void clearFields() {
        sujetAvisField.clear();
        contenuAvisField.clear();
        emailAvisField.clear();
        noteAvisField.getValueFactory().setValue(1); // Remet la note par défaut à 1
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
