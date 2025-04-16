package Controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import Entities.Consultation;
import Services.ConsultationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.stage.Stage;

public class AjouterConsultationController {

    @FXML
    private TextField searchInput;

    @FXML
    private Label LabelAge, LabelDateCons, LabelNom, LabelPrenom, LabelRaison, LabelProfession;

    @FXML
    private TextField TFAge, TFNom, TFPrenom, TFProfession;

    @FXML
    private TextArea TFRaison;

    @FXML
    private DatePicker DPDateCons;

    private final ConsultationService consultationService = new ConsultationService();

    @FXML private Button btnAjouter;

    @FXML
    void AjouterConsultationFXML(ActionEvent event) {
        String nom = TFNom.getText().trim();
        String prenom = TFPrenom.getText().trim();
        String ageStr = TFAge.getText().trim();
        String profession = TFProfession.getText().trim();
        String raison = TFRaison.getText().trim();
        LocalDate localDate = DPDateCons.getValue();

        // Vérification des champs vides
        if (nom.length() < 3) {
            showError("Le nom doit contenir au moins 3 caractères.");
            return;
        }
        if (prenom.length() < 3) {
            showError("Le prénom doit contenir au moins 3 caractères.");
            return;
        }
        if (profession.length() < 3) {
            showError("La profession doit contenir au moins 3 caractères.");
            return;
        }
        if (raison.length() < 3) {
            showError("La raison doit contenir au moins 3 caractères.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age > 100) {
                showError("L'âge doit être inférieur ou égal à 100 ans.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("L'âge doit être un nombre valide.");
            return;
        }

        if (localDate == null) {
            showError("Veuillez sélectionner une date.");
            return;
        }

        Date dateCons = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (!dateCons.after(new Date())) {
            showError("La date de consultation doit être strictement supérieure à aujourd'hui.");
            return;
        }

        // Création de l’objet Consultation
        Consultation c = new Consultation(
                dateCons,
                profession,
                raison,
                nom,
                prenom,
                age
        );

        consultationService.add(c);

        // Message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout réussi");
        alert.setHeaderText("Consultation ajoutée avec succès !");
        alert.setContentText("La consultation a été ajoutée à la liste.");
        alert.showAndWait();

        // Réinitialisation du formulaire
        TFNom.clear();
        TFPrenom.clear();
        TFAge.clear();
        TFProfession.clear();
        TFRaison.clear();
        DPDateCons.setValue(null);
    }



    @FXML
    void AfficherConsultationFXML(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherConsultation.fxml"));
            TFNom.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur de chargement de la page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert LabelAge != null : "fx:id=\"LabelAge\" not injected.";
        assert LabelDateCons != null : "fx:id=\"LabelDateCons\" not injected.";
        assert LabelNom != null : "fx:id=\"LabelNom\" not injected.";
        assert LabelPrenom != null : "fx:id=\"LabelPrenom\" not injected.";
        assert LabelRaison != null : "fx:id=\"LabelRaison\" not injected.";
        assert LabelProfession != null : "fx:id=\"LabelProfession\" not injected.";
        assert TFAge != null : "fx:id=\"TFAge\" not injected.";
        assert TFNom != null : "fx:id=\"TFNom\" not injected.";
        assert TFPrenom != null : "fx:id=\"TFPrenom\" not injected.";
        assert TFRaison != null : "fx:id=\"TFRaison\" not injected.";
        assert TFProfession != null : "fx:id=\"TFProfession\" not injected.";
        assert DPDateCons != null : "fx:id=\"DPDateCons\" not injected.";
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(msg);
        alert.show();
    }

    @FXML
    private void goToLandingPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LandingPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) searchInput.getScene().getWindow();  // Cela récupère la fenêtre actuelle
            stage.setScene(new Scene(root));  // Définit la nouvelle scène
            stage.setTitle("Landing Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
