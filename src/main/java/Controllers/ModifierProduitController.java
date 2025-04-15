package Controllers;

import Entities.Categorie;
import Entities.Produit;
import Services.CategorieService;
import Services.ProduitService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ModifierProduitController implements Initializable {

    @FXML
    private TextField nomProduitField;
    @FXML
    private TextField prixProduitField;
    @FXML
    private TextField quantiteProduitField;
    @FXML
    private ComboBox<String> statutProduitCombo;
    @FXML
    private ComboBox<String> categorieProduitCombo;

    private Map<String, Categorie> categorieMap = new HashMap<>();
    private Produit produitAModifier;
    @FXML
    private Button retourButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.getClass().getResource("/style.css").toExternalForm();
        statutProduitCombo.setItems(FXCollections.observableArrayList("Disponible", "Indisponible"));

        CategorieService categorieService = new CategorieService();
        List<Categorie> categories = categorieService.find();

        ObservableList<String> noms = FXCollections.observableArrayList();
        for (Categorie c : categories) {
            noms.add(c.getNomCategorie());
            categorieMap.put(c.getNomCategorie(), c);
        }
        categorieProduitCombo.setItems(noms);
    }

    public void setProduit(Produit produit) {
        this.produitAModifier = produit;

        nomProduitField.setText(produit.getNomProduit());
        prixProduitField.setText(String.valueOf(produit.getPrixProduit()));
        quantiteProduitField.setText(String.valueOf(produit.getQuantite()));
        statutProduitCombo.setValue(produit.getStatutProduit());

        if (produit.getCategorie() != null) {
            categorieProduitCombo.setValue(produit.getCategorie().getNomCategorie());
        }
    }

    @FXML
    public void ModifierProduit(ActionEvent actionEvent) {
        try {
            String nom = nomProduitField.getText();
            int prix = Integer.parseInt(prixProduitField.getText());
            int quantite = Integer.parseInt(quantiteProduitField.getText());
            String statut = statutProduitCombo.getValue();
            String categorieNom = categorieProduitCombo.getValue();

            if (statut == null || categorieNom == null) {
                showAlert("Erreur", "Veuillez sélectionner un statut et une catégorie !");
                return;
            }

            Categorie categorie = categorieMap.get(categorieNom);

            produitAModifier.setNomProduit(nom);
            produitAModifier.setPrixProduit(prix);
            produitAModifier.setQuantite(quantite);
            produitAModifier.setStatutProduit(statut);
            produitAModifier.setCategorie(categorie);

            ProduitService service = new ProduitService();
            service.update(produitAModifier);

            // Afficher une alerte
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Produit modifié avec succès !");
            alert.showAndWait();

            // Fermer la fenêtre après validation
            ((Stage) nomProduitField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix et quantité doivent être des nombres !");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }


    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProduitAdmin.fxml"));
            Parent root = loader.load();
            retourButton.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
