package Controllers;

import Entities.Categorie;
import Services.CategorieService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AjouterCategorieController {

    @FXML
    private TextField nomCategorieField;

    private CategorieService categorieService = new CategorieService();

    @FXML
    private void ajouterCategorie() {
        this.getClass().getResource("/style.css").toExternalForm();
        String nom = nomCategorieField.getText();

        if (nom == null || nom.trim().isEmpty()) {
            showAlert("Erreur", "Le nom de la catégorie ne peut pas être vide !");
            return;
        }

        Categorie categorie = new Categorie(nom);
        categorieService.add(categorie);

        showAlert("Succès", "Catégorie ajoutée avec succès !");
        nomCategorieField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

