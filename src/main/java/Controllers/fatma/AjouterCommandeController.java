package Controllers.fatma;

import Entities.fatma.Commande;
import Services.fatma.CommandeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.regex.Pattern;

public class AjouterCommandeController {

    @FXML private TextField nomClientField;
    @FXML private TextField emailClientField;
    @FXML private DatePicker dateCommandeField;
    @FXML private TextField adresseField;
    @FXML private Label totalComLabel;
    @FXML private TextField paysField;
    @FXML private TextField numTelephoneField;

    @FXML
    public void initialize() {
        this.getClass().getResource("/styleProduit.css").toExternalForm();
        dateCommandeField.setValue(java.time.LocalDate.now());
        paysField.setText("Tunisie");

        // Valeur d'exemple pour total, fixe ou calculée
        totalComLabel.setText("0.0 €");
    }

    @FXML
    public void AjouterCommande(ActionEvent actionEvent) {
        try {
            String nomClient = nomClientField.getText().trim();
            String emailClient = emailClientField.getText().trim();
            String adresse = adresseField.getText().trim();
            String pays = paysField.getText().trim();

            if (nomClient.isEmpty() || emailClient.isEmpty() || adresse.isEmpty() || pays.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires : Nom, Email, Adresse, Pays.");
                return;
            }

            if (!isValidEmail(emailClient)) {
                showAlert(Alert.AlertType.ERROR, "L'adresse email est invalide.");
                return;
            }

            if (dateCommandeField.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Veuillez sélectionner une date de commande.");
                return;
            }
            Date dateCommande = Date.valueOf(dateCommandeField.getValue());

            // Lecture depuis Label, sans modification
           double totalCom = Double.parseDouble(totalComLabel.getText().replace("€", "").trim());

            String numTelText = numTelephoneField.getText().trim();
            if (!numTelText.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Le numéro de téléphone doit contenir exactement 8 chiffres.");
                return;
            }
            int numTelephone = Integer.parseInt(numTelText);

            Commande nouvelleCommande = new Commande(nomClient, emailClient, dateCommande, adresse, totalCom, pays, numTelephone);
            CommandeService commandeService = new CommandeService();
            commandeService.add(nouvelleCommande);

            showAlert(Alert.AlertType.INFORMATION, "Commande ajoutée avec succès !");
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Une erreur est survenue : " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Succès" : "Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomClientField.clear();
        emailClientField.clear();
        adresseField.clear();
        numTelephoneField.clear();
        dateCommandeField.setValue(java.time.LocalDate.now());
        paysField.setText("Tunisie");
        totalComLabel.setText("0.0 €"); // Remettre la valeur initiale
    }
}
