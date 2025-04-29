package Controllers.malek;

import Components.malek.PhoneNumberField;
import Entities.malek.Centre;
import Services.malek.CentreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ModifierCentreController {

    @FXML private Label labelNomCentre;
    @FXML private TextField TFNom;
    @FXML private TextField TFAdresse;
    @FXML private PhoneNumberField TFTelephone;
    @FXML private TextField TFEmail;
    @FXML private TextField TFSpecialite;
    @FXML private TextField TFCapacite;
    @FXML private TextField TFPhoto;
    @FXML private Button btnUploadPhoto;

    private Centre centreAModifier;
    private final CentreService centreService = new CentreService();
    private String newPhotoPath;

    @FXML
    public void initialize() {
        TFTelephone.setDefaultCountry("TN");

        TFTelephone.getPhoneNumberField().textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null && !newText.matches("\\d*")) {
                TFTelephone.getPhoneNumberField().setText(newText.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setCentreAModifier(Centre centre) {
        this.centreAModifier = centre;
        updateUI();
    }

    private void updateUI() {
        labelNomCentre.setText(centreAModifier.getNomCentre());
        TFNom.setText(centreAModifier.getNomCentre());
        TFAdresse.setText(centreAModifier.getAdresseCentre());
        TFTelephone.setPhoneNumber(centreAModifier.getTelCentre());
        TFEmail.setText(centreAModifier.getEmailCentre());
        TFSpecialite.setText(centreAModifier.getSpecialiteCentre());
        TFCapacite.setText(String.valueOf(centreAModifier.getCapaciteCentre()));
        TFPhoto.setText(centreAModifier.getPhotoCentre());
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        if (!validateFields()) return;

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Enregistrer les modifications ?");
        confirm.setContentText("Êtes-vous sûr de vouloir modifier ce centre ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            updateCentreFromForm();
            if (newPhotoPath != null) {
                centreAModifier.setPhotoCentre(newPhotoPath);
            }
            centreService.update(centreAModifier);
            showAlert("Succès", "Centre modifié avec succès", AlertType.INFORMATION);
            closeWindow(event);
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Annuler les modifications ?");
        confirm.setContentText("Voulez-vous vraiment quitter sans enregistrer ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            closeWindow(event);
        }
    }

    @FXML
    private void handleUploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            newPhotoPath = file.getAbsolutePath();
            TFPhoto.setText(newPhotoPath);
        }
    }

    private void updateCentreFromForm() {
        centreAModifier.setNomCentre(TFNom.getText());
        centreAModifier.setAdresseCentre(TFAdresse.getText());
        centreAModifier.setTelCentre(TFTelephone.getPhoneNumber());
        centreAModifier.setEmailCentre(TFEmail.getText());
        centreAModifier.setSpecialiteCentre(TFSpecialite.getText());
        centreAModifier.setCapaciteCentre(Integer.parseInt(TFCapacite.getText()));
    }

    private boolean validateFields() {
        resetFieldStyles();
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;

        if (TFNom.getText().trim().length() < 6) {
            errors.append("- Le nom du centre doit contenir au moins 6 caractères\n");
            TFNom.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (TFAdresse.getText().trim().isEmpty()) {
            errors.append("- Adresse est obligatoire\n");
            TFAdresse.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        String phone = TFTelephone.getPhoneNumber();
        if (!isValidPhoneNumber(phone)) {
            errors.append("- Numéro de téléphone invalide\n");
            TFTelephone.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!TFEmail.getText().trim().isEmpty() && !isValidEmail(TFEmail.getText())) {
            errors.append("- Format email invalide\n");
            TFEmail.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (TFSpecialite.getText().trim().isEmpty()) {
            errors.append("- Spécialité est obligatoire\n");
            TFSpecialite.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (TFCapacite.getText().trim().isEmpty()) {
            errors.append("- Capacité est obligatoire\n");
            TFCapacite.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            try {
                int capacite = Integer.parseInt(TFCapacite.getText());
                if (capacite <= 0) {
                    errors.append("- Capacité doit être supérieure à 0\n");
                    TFCapacite.setStyle("-fx-border-color: red;");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                errors.append("- Capacité doit être un nombre valide\n");
                TFCapacite.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }

        if (!isValid) {
            showAlert("Erreurs de validation", errors.toString(), AlertType.ERROR);
        }

        return isValid;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        try {
            return TFTelephone.getPhoneNumberUtil().isValidNumber(
                    TFTelephone.getPhoneNumberUtil().parse(phoneNumber, null));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void resetFieldStyles() {
        TFNom.setStyle("");
        TFAdresse.setStyle("");
        TFTelephone.setStyle("");
        TFEmail.setStyle("");
        TFSpecialite.setStyle("");
        TFCapacite.setStyle("");
        TFPhoto.setStyle("");
    }

    private void closeWindow(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
