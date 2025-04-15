package Controllers;

import Entities.Categorie;
import Entities.Produit;
import Services.CategorieService;
import Services.ProduitService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjouterProduitController {

    @FXML
    private TextField nomProduitField;
    @FXML
    private TextField prixProduitField;
    @FXML
    private TextField quantiteProduitField;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private ComboBox<String> categorieComboBox;

    private Map<String, Categorie> categorieMap = new HashMap<>();

    @FXML
    public void initialize() {
        this.getClass().getResource("/style.css").toExternalForm();
        // Remplir statut (désactivé car automatique)
        statutComboBox.setItems(FXCollections.observableArrayList("Disponible", "Indisponible"));
        statutComboBox.setDisable(true);

        // Remplir ComboBox catégorie avec Nom -> Objet
        CategorieService categorieService = new CategorieService();
        List<Categorie> categories = categorieService.find();

        for (Categorie cat : categories) {
            categorieComboBox.getItems().add(cat.getNomCategorie());
            categorieMap.put(cat.getNomCategorie(), cat);
        }
    }

    @FXML
    public void AjouterProduit(ActionEvent actionEvent) {
        try {
            String nom = nomProduitField.getText();
            String prixText = prixProduitField.getText();
            String quantiteText = quantiteProduitField.getText();
            String categorieNom = categorieComboBox.getValue();

            if (nom == null || nom.trim().isEmpty() ||
                    prixText == null || prixText.trim().isEmpty() ||
                    quantiteText == null || quantiteText.trim().isEmpty() ||
                    categorieNom == null) {

                showAlert(Alert.AlertType.ERROR, "Tous les champs sont obligatoires !");
                return;
            }

            int prix = Integer.parseInt(prixText);
            int quantite = Integer.parseInt(quantiteText);

            if (prix <= 0) {
                showAlert(Alert.AlertType.ERROR, "Le prix doit être supérieur à zéro !");
                return;
            }

            if (quantite < 0) {
                showAlert(Alert.AlertType.ERROR, "La quantité ne peut pas être négative !");
                return;
            }

            String statut = (quantite == 0) ? "Indisponible" : "Disponible";

            // Ici récupération de l'objet Categorie, pas l'id
            Categorie categorie = categorieMap.get(categorieNom);

            // Création de l'objet Produit avec un objet Categorie
            Produit produit = new Produit(nom, prix, quantite, statut, categorie);

            ProduitService service = new ProduitService();
            service.add(produit);

            showAlert(Alert.AlertType.INFORMATION, "Produit ajouté avec succès !");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Prix et quantité doivent être des nombres valides !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Succès" : "Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomProduitField.clear();
        prixProduitField.clear();
        quantiteProduitField.clear();
        statutComboBox.getSelectionModel().clearSelection();
        categorieComboBox.getSelectionModel().clearSelection();
    }
}
