package Controllers.mehdi;

import Entities.mehdi.Avis;
import Services.mehdi.AvisService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterAvisController implements Initializable {

    @FXML
    private TextField sujetAvisField;
    @FXML
    private TextArea contenuAvisField;
    @FXML
    private TextField emailAvisField;
    @FXML
    private Spinner<Integer> noteAvisField;

    private final AvisService avisService = new AvisService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser le Spinner avec une plage de 1 à 10 et valeur par défaut 1
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        noteAvisField.setValueFactory(valueFactory);
    }

    @FXML
    private void handleAddAvis() {
        String sujet = sujetAvisField.getText();
        String contenu = contenuAvisField.getText();
        String email = emailAvisField.getText();
        Integer note = noteAvisField.getValue();

        if (sujet.isEmpty() || contenu.isEmpty() || email.isEmpty()) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        if (contenu.length() < 10) {
            showAlert("Le contenu de l'avis doit contenir au moins 10 caractères.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Veuillez entrer une adresse email valide.");
            return;
        }

        // Création et ajout de l'avis
        Avis avis = new Avis(0, sujet, contenu, note, new Date(), email, "Non traité");
        avisService.add(avis);

        showSuccess("✅ Avis ajouté avec succès !");

        // 👉 Redirection automatique vers AfficherAvisFrontend.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mehdi/AfficherAvisFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Récupérer la scène actuelle depuis le bouton (ou n'importe quel composant de la vue)
            Stage stage = (Stage) sujetAvisField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Liste des Avis");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void clearFields() {
        sujetAvisField.clear();
        contenuAvisField.clear();
        emailAvisField.clear();
        noteAvisField.getValueFactory().setValue(1); // Réinitialiser la note à 1
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
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
