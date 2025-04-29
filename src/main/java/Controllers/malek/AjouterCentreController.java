package Controllers.malek;

import Components.malek.PhoneNumberField;
import Entities.malek.Centre;
import Services.malek.CentreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.regex.Pattern;

public class AjouterCentreController {
    @FXML private TextField TFNom;
    @FXML private TextField TFAdresse;
    @FXML private PhoneNumberField TFTelephone;
    @FXML private TextField TFEmail;
    @FXML private TextField TFSpecialite;
    @FXML private TextField TFCapacite;
    @FXML private TextField TFPhoto;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    @FXML private Button btnUploadPhoto;

    private final CentreService centreService = new CentreService();

    @FXML
    public void initialize() {
        // Configuration initiale du champ téléphone
        TFTelephone.setDefaultCountry("TN"); // Tunisie par défaut

        // Validation du numéro de téléphone
        TFTelephone.getPhoneNumberField().textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null && !newText.matches("\\d*")) {
                TFTelephone.getPhoneNumberField().setText(newText.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (!validateFields()) return;

        try {
            Centre centre = new Centre(
                    TFNom.getText(),
                    TFAdresse.getText(),
                    TFTelephone.getPhoneNumber(), // Utilise la méthode spécifique de PhoneNumberField
                    TFEmail.getText(),
                    TFSpecialite.getText(),
                    Integer.parseInt(TFCapacite.getText()),
                    TFPhoto.getText()
            );

            centreService.add(centre);
            showAlert("Succès", "Centre ajouté avec succès", Alert.AlertType.INFORMATION);
            closeWindow();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (TFNom.getText().isEmpty() || TFNom.getText().length() < 3) {
            showError("Nom trop court (min 3 caractères)");
            return false;
        }

        if (TFAdresse.getText().isEmpty()) {
            showError("Adresse ne peut pas être vide");
            return false;
        }

        String phone = TFTelephone.getPhoneNumber();
        if (!isValidPhoneNumber(phone)) {
            showError("Numéro de téléphone invalide");
            return false;
        }

        if (TFEmail.getText().isEmpty() || !isValidEmail(TFEmail.getText())) {
            showError("Email invalide");
            return false;
        }

        if (TFSpecialite.getText().isEmpty()) {
            showError("Spécialité ne peut pas être vide");
            return false;
        }

        try {
            int capacite = Integer.parseInt(TFCapacite.getText());
            if (capacite <= 0) {
                showError("Capacité doit être positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Capacité invalide");
            return false;
        }

        return true;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        try {
            return TFTelephone.getPhoneNumberUtil().isValidNumber(
                    TFTelephone.getPhoneNumberUtil().parse(phoneNumber, null));
        } catch (Exception e) {
            return false;
        }
    }

    private void showError(String message) {
        showAlert("Erreur", message, Alert.AlertType.ERROR);
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void handleUploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            TFPhoto.setText(file.getAbsolutePath());
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}