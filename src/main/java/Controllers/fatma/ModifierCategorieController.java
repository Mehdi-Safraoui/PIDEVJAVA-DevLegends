package Controllers.fatma;

import Entities.fatma.Categorie;
import Services.fatma.CategorieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifierCategorieController {

    @FXML
    private TextField nomCategorieField;

    private Categorie categorie;

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        nomCategorieField.setText(categorie.getNomCategorie());
    }
    @FXML
    private Button retourButton;

    @FXML
    private void modifierCategorie() {
        if (nomCategorieField.getText().trim().isEmpty()) {
            showAlert("Le nom de la catégorie ne peut pas être vide.");
            return;
        }

        categorie.setNomCategorie(nomCategorieField.getText().trim());
        CategorieService service = new CategorieService();
        service.update(categorie);

        showAlert("Catégorie modifiée avec succès !");
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomCategorieField.getScene().getWindow();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de Fermeture");
        confirm.setHeaderText("Voulez-vous fermer cette fenêtre ?");
        confirm.setContentText("Les modifications sont enregistrées.");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                stage.close();
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/AfficherCategorie.fxml"));
            Parent root = loader.load();
            retourButton.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
