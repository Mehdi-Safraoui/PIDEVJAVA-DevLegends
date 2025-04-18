package Controllers.malek;

import Entities.malek.Pack;
import Services.malek.PackService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AjouterPackController {

    @FXML private TextField TFNomPack;
    @FXML private TextField TFDescription;
    @FXML private TextField TFPrix;
    @FXML private TextField TFDuree;
    @FXML private TextField TFPhoto;
    @FXML private TextField TFDiscountCode;
    @FXML private TextField TFIsUsed;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;
    @FXML private Button btnUploadPhoto;

    private final PackService packService = new PackService();

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            // Validation des champs
            if (!validateFields()) return;

            // Création et remplissage du Pack
            Pack pack = new Pack();
            pack.setNomPack(TFNomPack.getText());
            pack.setDescriptPack(TFDescription.getText());

            // Validation du prix : Il doit être un entier positif
            int prix = Integer.parseInt(TFPrix.getText());
            if (prix <= 0) {
                showAlert("Validation", "Le prix doit être un entier positif.", Alert.AlertType.WARNING);
                return;
            }
            pack.setPrixPack(prix);

            // Validation de la durée : Elle doit être un entier positif
            String dureeText = TFDuree.getText().trim();
            try {
                int duree = Integer.parseInt(dureeText);
                if (duree <= 0) {
                    showAlert("Validation", "La durée doit être un entier positif.", Alert.AlertType.WARNING);
                    return;
                }
                pack.setDureePack(dureeText); // Garder la durée en chaîne
            } catch (NumberFormatException e) {
                showAlert("Validation", "La durée doit être un entier valide.", Alert.AlertType.WARNING);
                return;
            }

            pack.setPhotoPack(TFPhoto.getText());
            pack.setDiscountCode(TFDiscountCode.getText());

            // Gestion du champ isUsed
            String isUsedText = TFIsUsed.getText().trim();
            boolean isUsed = isUsedText.equalsIgnoreCase("true");
            pack.setUsed(isUsed);

            // Enregistrement via service
            packService.add(pack);

            showAlert("Succès", "Pack ajouté avec succès !", Alert.AlertType.INFORMATION);
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix et la durée doivent être des entiers valides.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void handleUploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            TFPhoto.setText(file.getAbsolutePath());
        }
    }

    private boolean validateFields() {
        if (TFNomPack.getText().isEmpty()) {
            showAlert("Validation", "Le nom du pack est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }
        if (TFDescription.getText().isEmpty()) {
            showAlert("Validation", "La description est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }
        if (TFPrix.getText().isEmpty()) {
            showAlert("Validation", "Le prix est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }
        if (TFDuree.getText().isEmpty()) {
            showAlert("Validation", "La durée est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(TFPrix.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation", "Le prix doit être un entier.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(TFDuree.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation", "La durée doit être un entier.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
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
