package Controllers.fatma;

import Entities.fatma.Commande;
import Entities.salsabil.User; // 👈 Important : importer User
import Utils.NotificationManager;
import Utils.Session;          // 👈 Importer ton Session
import Services.fatma.CommandeService;
import Services.fatma.MollieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URI;
import java.sql.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AjouterCommandeController {

    @FXML private TextField nomClientField;
    @FXML private TextField emailClientField;
    @FXML private DatePicker dateCommandeField;
    @FXML private ComboBox<String> adresseComboBox;
    @FXML private Label totalComLabel;
    @FXML private Label paysLabel;
    @FXML private TextField numTelephoneField;

    private Commande commande;
    private double totalCommande;

    @FXML
    public void initialize() {
        ObservableList<String> gouvernorats = FXCollections.observableArrayList(
                "Tunis", "Ariana", "Ben Arous", "Manouba", "Nabeul", "Zaghouan",
                "Bizerte", "Béja", "Jendouba", "Kef", "Siliana", "Sousse",
                "Monastir", "Mahdia", "Kairouan", "Kasserine", "Sidi Bouzid",
                "Gabès", "Medenine", "Tataouine", "Gafsa", "Tozeur", "Kebili", "Sfax"
        );
        adresseComboBox.setItems(gouvernorats);
        adresseComboBox.getSelectionModel().selectFirst();

        dateCommandeField.setValue(java.time.LocalDate.now());
        numTelephoneField.setText("+216");

        commande = new Commande();



        // ✅ Auto-remplir les champs depuis Session
        User user = Session.getCurrentUser();
        if (user != null) {
            nomClientField.setText(user.getFirst_name());
            emailClientField.setText(user.getUser_email() );
        }
    }

    @FXML
    public void AjouterCommande(ActionEvent actionEvent) {
        try {
            String nomClient = nomClientField.getText().trim();
            String emailClient = emailClientField.getText().trim();
            String adresse = adresseComboBox.getSelectionModel().getSelectedItem();
            String pays = paysLabel.getText().trim();

            if (adresse == null || adresse.isEmpty() || nomClient.isEmpty() || emailClient.isEmpty() || pays.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires.");
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

            String numTelText = numTelephoneField.getText().trim();
            if (!numTelText.startsWith("+216") || numTelText.length() != 12) {
                showAlert(Alert.AlertType.ERROR, "Format de téléphone invalide.");
                return;
            }

            String numberPart = numTelText.substring(4);
            if (!numberPart.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Le numéro doit contenir 8 chiffres après +216.");
                return;
            }

            int numTelephone = Integer.parseInt(numberPart);

            // Vérifie que le total est valide
            if (totalCommande < 0.01) {
                showAlert(Alert.AlertType.ERROR, "Le montant total doit être supérieur à 0.01€.");
                return;
            }

            commande.setNomClient(nomClient);
            commande.setAdresseEmail(emailClient);
            commande.setDateCommande(dateCommande);
            commande.setAdresse(adresse);
            commande.setPays(pays);
            commande.setNumTelephone(numTelephone);
            commande.setTotalCom(totalCommande);

            // ✅ Récupérer l'utilisateur connecté via Session
            User user = Session.getCurrentUser();
            if (user != null) {
                commande.setUserId(user.getId()); // 👈 Associer userId
            } else {
                showAlert(Alert.AlertType.ERROR, "Utilisateur non connecté.");
                return;
            }

            // Convertir le montant pour Mollie
            String amountFormatted = String.format(Locale.US, "%.2f", totalCommande);

            // Créer paiement Mollie
            String paymentUrl = MollieService.createMolliePayment(amountFormatted);

            if (paymentUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Échec de création du paiement Mollie.");
                return;
            }

            commande.setPaymentId(paymentUrl);
            new CommandeService().add(commande);
            // ✅ Ajouter la notification ici seulement si tout a bien été ajouté
            NotificationManager.addNotification("Commande ajoutée avec succès pour " + commande.getNomClient());
            NotificationManager.openNotificationWindow();
            // ✅ Affichage de la facture
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/facture.fxml"));
            Parent root = loader.load();
            Controllers.fatma.FactureController controller = loader.getController();
            controller.setCommande(commande);

            Stage stage = new Stage();
            stage.setTitle("Facture");
            stage.setScene(new Scene(root));
            stage.show();

            showAlert(Alert.AlertType.INFORMATION, "Commande enregistrée, redirection vers paiement...");
            java.awt.Desktop.getDesktop().browse(new URI(paymentUrl));

            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(email).matches();
    }

    public void setTotalCommande(double total) {
        this.totalCommande = total;
        totalComLabel.setText(String.format("%.2f", total));
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Succès" : "Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        adresseComboBox.getSelectionModel().selectFirst();
        numTelephoneField.setText("+216");
        totalComLabel.setText("0.00");
        totalCommande = 0;
        dateCommandeField.setValue(java.time.LocalDate.now());
    }
}
