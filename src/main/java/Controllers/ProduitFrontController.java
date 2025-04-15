package Controllers;

import Entities.Produit;
import Services.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProduitFrontController implements Initializable {


    @FXML
    private FlowPane productContainer;

    // Conteneur pour afficher les cartes produits

    private final ProduitService produitService = new ProduitService();  // Service pour récupérer les produits
    private final List<Produit> panier = new ArrayList<>();  // Liste du panier d'achat

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProducts();  // Charge les produits au démarrage
    }

    private void loadProducts() {
        // Vider le conteneur avant de charger les nouveaux produits
        productContainer.getChildren().clear();

        // Récupérer la liste des produits
        List<Produit> produits = produitService.find();

        // Parcours des produits pour les afficher
        for (Produit p : produits) {
            // Création d'une carte pour chaque produit
            VBox card = createProductCard(p);
            productContainer.getChildren().add(card);  // Ajoute la carte au conteneur
        }
    }

    private VBox createProductCard(Produit p) {
        // Création de la carte avec un espacement de 8 pixels
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setBackground(new Background(new BackgroundFill(Color.web("#f9f9f9"), new CornerRadii(12), Insets.EMPTY)));
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 12; -fx-background-radius: 12;");

        // Création des labels pour afficher les informations du produit
        Label nom = new Label("Nom : " + p.getNomProduit());
        Label prix = new Label("Prix : " + p.getPrixProduit() + " €");
        Label quantite = new Label("Quantité : " + p.getQuantite());
        Label statut = new Label("Statut : " + p.getStatutProduit());
        Label categorie = new Label("Catégorie : " + p.getCategorie().getNomCategorie());

        // Création du bouton "Ajouter au panier"
        Button addToCartButton = new Button("🛒 Ajouter au panier");
        addToCartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addToCartButton.setOnAction(e -> {
            panier.add(p);  // Ajoute le produit au panier
            showAlert("Produit ajouté au panier : " + p.getNomProduit());  // Affiche une alerte
        });

        // Ajout des éléments dans la carte
        card.getChildren().addAll(nom, prix, quantite, statut, categorie, addToCartButton);
        return card;  // Retourne la carte pour l'ajouter au conteneur
    }

    // Méthode pour afficher une alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
