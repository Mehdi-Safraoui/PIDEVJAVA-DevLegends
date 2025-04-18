package Controllers.malek;

import Services.malek.CentreService;
import Entities.malek.Centre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterCentreController {

    @FXML private TextField TFNom;
    @FXML private TextField TFAdresse;
    @FXML private TextField TFTelephone;
    @FXML private TextField TFEmail;
    @FXML private TextField TFSpecialite;
    @FXML private TextField TFCapacite;
    @FXML private TextField TFPhoto;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    @FXML private Button btnUploadPhoto; // Ajout d'un bouton pour uploader la photo


    private final CentreService centreService = new CentreService();

    @FXML
    public void initialize() {
        // Ajouter un écouteur de texte sur le champ téléphone pour valider en temps réel
        TFTelephone.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.startsWith("+216")) {
                TFTelephone.setText("+216");
            } else if (newText.length() > 12) { // +216 + 8 chiffres = 12 caractères max
                TFTelephone.setText(oldText);
            } else if (!newText.matches("\\+216\\d*")) {
                TFTelephone.setText(oldText); // Annule toute entrée non-numérique après +216
            }
        });
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            // Validation des champs
            if (!validateFields()) {
                return;
            }

            // Création du nouveau centre
            Centre centre = new Centre();
            centre.setNomCentre(TFNom.getText());
            centre.setAdresseCentre(TFAdresse.getText());
            centre.setTelCentre(TFTelephone.getText());
            centre.setEmailCentre(TFEmail.getText());
            centre.setSpecialiteCentre(TFSpecialite.getText());
            centre.setCapaciteCentre(Integer.parseInt(TFCapacite.getText()));
            centre.setPhotoCentre(TFPhoto.getText()); // L'URL du fichier de l'image

            // Ajout dans la base de données
            centreService.add(centre);

            // Message de succès
            showAlert("Succès", "Centre ajouté avec succès!", Alert.AlertType.INFORMATION);

            // Fermer la fenêtre
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre valide", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void handleUploadPhoto(ActionEvent event) {
        // Ouvrir le FileChooser pour sélectionner un fichier image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Afficher le chemin du fichier sélectionné dans le champ TextField
            TFPhoto.setText(file.getAbsolutePath());
        }
    }

    private boolean validateFields() {
        // Validation du nom (au moins 6 caractères)
        if (TFNom.getText().trim().length() < 6) {
            showAlert("Erreur", "Le nom du centre doit contenir au moins 6 caractères.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation de l'adresse (au moins 1 caractère)
        if (TFAdresse.getText().trim().isEmpty()) {
            showAlert("Erreur", "L'adresse ne peut pas être vide.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation du numéro de téléphone (doit commencer par +216 et avoir 8 chiffres)
        String phone = TFTelephone.getText();
        if (!phone.matches("^\\+216\\d{8}$")) {
            showAlert("Erreur", "Le numéro doit commencer par +216 et contenir 8 chiffres.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation de l'email (format valide)
        if (!isValidEmail(TFEmail.getText())) {
            showAlert("Erreur", "L'email n'est pas valide.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation de la spécialité (au moins 1 caractère)
        if (TFSpecialite.getText().trim().isEmpty()) {
            showAlert("Erreur", "La spécialité ne peut pas être vide.", Alert.AlertType.ERROR);
            return false;
        }

        // Validation de la capacité (doit être un nombre positif)
        try {
            int capacite = Integer.parseInt(TFCapacite.getText());
            if (capacite <= 0) {
                showAlert("Erreur", "La capacité doit être un nombre positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre valide.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }


    private boolean isValidEmail(String email) {
        // Utilisation d'une expression régulière pour valider le format de l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
