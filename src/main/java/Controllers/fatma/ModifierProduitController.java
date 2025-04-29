package Controllers.fatma;

import Entities.fatma.Categorie;
import Entities.fatma.Produit;
import Services.fatma.CategorieService;
import Services.fatma.ProduitService;
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
    @FXML
    private Button retourButton;

    private Map<String, Categorie> categorieMap = new HashMap<>();
    private Produit produitAModifier;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.getClass().getResource("/fatma/styleProduit.css").toExternalForm();
        statutProduitCombo.setItems(FXCollections.observableArrayList("Disponible", "Indisponible"));

        // Chargement des catégories
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
            String nom = nomProduitField.getText().trim();
            String prixText = prixProduitField.getText().trim();
            String quantiteText = quantiteProduitField.getText().trim();
            String statut = statutProduitCombo.getValue();
            String categorieNom = categorieProduitCombo.getValue();

            if (nom.isEmpty() || prixText.isEmpty() || quantiteText.isEmpty() || statut == null || categorieNom == null) {
                showAlert("Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            int prix = Integer.parseInt(prixText);
            int quantite = Integer.parseInt(quantiteText);

            // Mise à jour automatique du statut en fonction de la quantité
            if (quantite == 0) {
                statut = "Indisponible";
            } else {
                statut = "Disponible";
            }

            Categorie categorie = categorieMap.get(categorieNom);

            // Mise à jour du produit
            produitAModifier.setNomProduit(nom);
            produitAModifier.setPrixProduit(prix);
            produitAModifier.setQuantite(quantite);
            produitAModifier.setStatutProduit(statut);
            produitAModifier.setCategorie(categorie);

            // Mise à jour dans la base de données
            ProduitService service = new ProduitService();
            service.update(produitAModifier);

            showAlert("Succès", "Produit modifié avec succès !");

            // Fermeture de la fenêtre après validation
            ((Stage) nomProduitField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix et la quantité doivent être des nombres !");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/ProduitAdmin.fxml"));
            Parent root = loader.load();
            retourButton.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
