package Controllers;

import Entities.Commande;
import Services.CommandeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterCommandeController {

    @FXML
    private TextField nomClientField;
    @FXML
    private TextField emailClientField;
    @FXML
    private DatePicker dateCommandeField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField totalComField;
    @FXML
    private TextField paysField;
    @FXML
    private TextField numTelephoneField;

    @FXML
    public void initialize() {
        this.getClass().getResource("/style.css").toExternalForm();
        // Mettre la date d'aujourd'hui par défaut
        dateCommandeField.setValue(java.time.LocalDate.now());

        // Mettre "Tunisie" par défaut dans le champ pays
        paysField.setText("Tunisie");
    }

    @FXML
    public void AjouterCommande(ActionEvent actionEvent) {
        try {
            String nomClient = nomClientField.getText().trim();
            String emailClient = emailClientField.getText().trim();
            String adresse = adresseField.getText().trim();
            String pays = paysField.getText().trim();

            // Vérifier si certains champs sont vides
            if (nomClient.isEmpty() || emailClient.isEmpty() || adresse.isEmpty() || pays.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires : Nom, Email, Adresse, Pays.");
                return;
            }

            // Validation de l'email
            if (!isValidEmail(emailClient)) {
                showAlert(Alert.AlertType.ERROR, "L'adresse email est invalide.");
                return;
            }

            if (dateCommandeField.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Veuillez sélectionner une date de commande.");
                return;
            }
            Date dateCommande = Date.valueOf(dateCommandeField.getValue());

            double totalCom;
            try {
                totalCom = Double.parseDouble(totalComField.getText());
                if (totalCom <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Le total de la commande doit être supérieur à 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Le total de la commande doit être un nombre valide.");
                return;
            }

            int numTelephone;
            String numTelText = numTelephoneField.getText().trim();
            if (!numTelText.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Le numéro de téléphone doit contenir exactement 8 chiffres.");
                return;
            }
            numTelephone = Integer.parseInt(numTelText);
            if (numTelephone <= 0) {
                showAlert(Alert.AlertType.ERROR, "Le numéro de téléphone doit être strictement positif.");
                return;
            }

            Commande nouvelleCommande = new Commande(nomClient, emailClient, dateCommande, adresse, totalCom, pays, numTelephone);
            CommandeService commandeService = new CommandeService();
            commandeService.add(nouvelleCommande);

            showAlert(Alert.AlertType.INFORMATION, "Commande ajoutée avec succès !");
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Une erreur est survenue : " + e.getMessage());
        }
    }

    // Méthode pour valider le format de l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
        totalComField.clear();
        numTelephoneField.clear();
        // Remettre la date et pays par défaut
        dateCommandeField.setValue(java.time.LocalDate.now());
        paysField.setText("Tunisie");
    }
}
