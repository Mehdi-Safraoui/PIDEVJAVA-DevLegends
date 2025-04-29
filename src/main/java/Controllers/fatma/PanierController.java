package Controllers.fatma;

import Entities.fatma.PanierItem;
import Entities.fatma.Commande;
import Services.fatma.CommandeService;
import Entities.fatma.Produit;
import Services.fatma.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PanierController {

    @FXML
    private VBox panierListView;

    @FXML
    private Label totalLabel;

    private List<PanierItem> panierItems = new ArrayList<>();
    private ProduitService produitService = new ProduitService();

    public void setPanier(List<Produit> produits) {
        panierItems.clear();

        for (Produit p : produits) {
            boolean found = false;
            for (PanierItem item : panierItems) {
                if (item.getProduit().getId() == p.getId()) {
                    item.incrementer();
                    found = true;
                    break;
                }
            }
            if (!found) {
                panierItems.add(new PanierItem(p, 1));
            }
        }

        afficherPanier();
    }

    private void afficherPanier() {
        panierListView.getChildren().clear();

        for (PanierItem item : panierItems) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: #f9f9f9;");

            Label nom = new Label("Nom : " + item.getProduit().getNomProduit());
            Label prix = new Label("Prix unitaire : " + item.getProduit().getPrixProduit() );
            Label quantite = new Label("Quantité : " + item.getQuantite());
            double totalProduit = item.getTotalPrix();

            // Validation du prix total pour chaque produit
            if (totalProduit < 0) {
                totalProduit = 0;  // Gestion d'un prix négatif (si erreur dans les données)
            }

            Label totalProduitLabel = new Label("Total : " + totalProduit );

            Button plus = new Button("+");
            Button moins = new Button("-");

            // Incrémenter la quantité
            plus.setOnAction(e -> {
                int stockEnBase = produitService.findById(item.getProduit().getId()).getQuantite();
                int quantiteDansPanier = item.getQuantite();

                if (quantiteDansPanier < stockEnBase) {
                    item.incrementer();
                    afficherPanier();
                } else {
                    showAlert("Quantité indisponible ! Stock maximum : " + stockEnBase);
                }
            });

            // Décrémenter la quantité
            moins.setOnAction(e -> {
                item.decrementer();
                if (item.getQuantite() == 0) {
                    panierItems.remove(item);
                }
                afficherPanier();
            });

            HBox actions = new HBox(10, moins, plus);
            actions.setAlignment(Pos.CENTER);

            card.getChildren().addAll(nom, prix, quantite, totalProduitLabel, actions);
            panierListView.getChildren().add(card);
        }

        mettreAJourTotalPanier();
    }

    private void mettreAJourTotalPanier() {
        double total = 0;
        for (PanierItem item : panierItems) {
            total += item.getTotalPrix();
        }

        // Log du total calculé
        System.out.println("Total du panier calculé : " + total);

        totalLabel.setText(String.format("🧾 Total Panier : %.2f ", total)); // Affichage avec 2 décimales
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte de Stock");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) panierListView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleValiderCommande() {
        try {
            double total = 0;
            for (PanierItem item : panierItems) {
                total += item.getTotalPrix();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/AjouterCommande.fxml"));
            Parent root = loader.load();

            AjouterCommandeController controller = loader.getController();
            if (controller != null) {
                controller.setTotalCommande(total);  // Envoi direct
            }

            Stage stage = new Stage();
            stage.setTitle("Valider la Commande");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) panierListView.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
