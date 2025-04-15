package Controllers;

import Entities.Pack;
import Services.PackService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.File;

public class ModifierPackController {

    @FXML private Label labelNomPack;
    @FXML private TextField TFNom;
    @FXML private TextField TFDescription;
    @FXML private TextField TFPrix;
    @FXML private TextField TFDuree;
    @FXML private TextField TFDiscountCode;
    @FXML private TextField TFPhoto;
    @FXML private Button btnParcourir;

    private Pack packAModifier;
    private final PackService packService = new PackService();

    public void setPackAModifier(Pack pack) {
        this.packAModifier = pack;
        updateUI();
    }

    private void updateUI() {
        labelNomPack.setText(packAModifier.getNomPack());
        TFNom.setText(packAModifier.getNomPack());
        TFDescription.setText(packAModifier.getDescriptPack());
        TFPrix.setText(String.valueOf(packAModifier.getPrixPack()));
        TFDuree.setText(packAModifier.getDureePack());
        TFDiscountCode.setText(packAModifier.getDiscountCode());
        TFPhoto.setText(packAModifier.getPhotoPack());
    }

    @FXML
    private void handleParcourirPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            TFPhoto.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        if (!validateFields()) return;

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Enregistrer les modifications ?");
        confirm.setContentText("Êtes-vous sûr de vouloir modifier ce pack ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            updatePackFromForm();
            packService.update(packAModifier);
            showAlert("Succès", "Pack modifié avec succès", AlertType.INFORMATION);
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

    private void updatePackFromForm() {
        packAModifier.setNomPack(TFNom.getText());
        packAModifier.setDescriptPack(TFDescription.getText());
        packAModifier.setPrixPack(Double.parseDouble(TFPrix.getText()));
        packAModifier.setDureePack(TFDuree.getText()); // Garder la durée en chaîne
        packAModifier.setDiscountCode(TFDiscountCode.getText());
        packAModifier.setPhotoPack(TFPhoto.getText());
    }

    private boolean validateFields() {
        resetFieldStyles();
        StringBuilder errors = new StringBuilder();

        if (TFNom.getText().isEmpty()) {
            errors.append("- Nom requis\n");
            TFNom.setStyle("-fx-border-color: red;");
        }

        if (TFDescription.getText().isEmpty()) {
            errors.append("- Description requise\n");
            TFDescription.setStyle("-fx-border-color: red;");
        }

        try {
            Double.parseDouble(TFPrix.getText());
        } catch (NumberFormatException e) {
            errors.append("- Prix doit être un nombre valide\n");
            TFPrix.setStyle("-fx-border-color: red;");
        }

        // Validation de la durée (doit être un nombre entier positif)
        String dureeText = TFDuree.getText().trim();
        try {
            int duree = Integer.parseInt(dureeText);
            if (duree <= 0) {
                errors.append("- La durée doit être un entier positif\n");
                TFDuree.setStyle("-fx-border-color: red;");
            }
        } catch (NumberFormatException e) {
            errors.append("- La durée doit être un entier valide\n");
            TFDuree.setStyle("-fx-border-color: red;");
        }

        if (!TFDiscountCode.getText().isEmpty() && !TFDiscountCode.getText().matches("^[A-Za-z0-9]+$")) {
            errors.append("- Code promo invalide\n");
            TFDiscountCode.setStyle("-fx-border-color: red;");
        }

        if (errors.length() > 0) {
            showAlert("Erreurs de validation", errors.toString(), AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void resetFieldStyles() {
        TFNom.setStyle("");
        TFDescription.setStyle("");
        TFPrix.setStyle("");
        TFDuree.setStyle("");
        TFDiscountCode.setStyle("");
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
