package Controllers.fatma;

import Entities.fatma.Produit;
import Services.fatma.ProduitService;
import Utils.NotificationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.text.Text;

public class ProduitFrontController implements Initializable {

    @FXML
    private FlowPane productContainer;

    @FXML
    private ComboBox<String> categorieComboBox;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    @FXML
    private Button notificationButton;

    private final ProduitService produitService = new ProduitService();
    private final List<Produit> panier = new ArrayList<>();
    private final int pageSize = 3;
    private int currentPage = 0;
    private List<Produit> allProducts = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCategories();
        setupSortComboBox();
        loadProducts();
    }

    @FXML
    private void handleNotificationClick() {
        try {
            // Charger le FXML de la fenêtre des notifications
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/NotificationPage.fxml"));

            VBox notificationPage = loader.load();

            // Récupérer le contrôleur de la fenêtre des notifications
            NotificationsPageController notificationsController = loader.getController();

            // Définir le contrôleur des notifications dans NotificationManager
            NotificationManager.setNotificationsPageController(notificationsController);

            // Créer une nouvelle scène avec la page des notifications
            Stage notificationStage = new Stage();
            notificationStage.setTitle("Notifications");
            notificationStage.setScene(new Scene(notificationPage));
            notificationStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadCategories() {
        List<String> categories = produitService.findAllCategories();
        categorieComboBox.getItems().clear();
        categorieComboBox.getItems().add("Tous");
        categorieComboBox.getItems().addAll(categories);
        categorieComboBox.getSelectionModel().selectFirst();
    }

    private void setupSortComboBox() {
        sortComboBox.getItems().addAll(
                "Nom croissant",
                "Prix croissant",
                "Prix décroissant",
                "Quantité croissante",
                "Quantité décroissante"
        );
    }

    private void loadProducts() {
        allProducts = produitService.find();
        currentPage = 0;
        showPage(currentPage);
        updatePaginationButtons();
    }

    private void showPage(int pageIndex) {
        productContainer.getChildren().clear();
        int fromIndex = pageIndex * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allProducts.size());

        if (fromIndex >= allProducts.size()) return;

        List<Produit> pageProducts = allProducts.subList(fromIndex, toIndex);
        for (Produit p : pageProducts) {
            VBox card = createProductCard(p);
            productContainer.getChildren().add(card);
        }

        prevPageButton.setDisable(pageIndex == 0);
        nextPageButton.setDisable(toIndex >= allProducts.size());
    }

    private void updatePaginationButtons() {
        prevPageButton.setDisable(currentPage == 0);
        nextPageButton.setDisable((currentPage + 1) * pageSize >= allProducts.size());
    }

    @FXML
    private void nextPage() {
        if ((currentPage + 1) * pageSize < allProducts.size()) {
            currentPage++;
            showPage(currentPage);
            updatePaginationButtons();
        }
    }

    @FXML
    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            showPage(currentPage);
            updatePaginationButtons();
        }
    }

    private VBox createProductCard(Produit p) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setBackground(new Background(new BackgroundFill(Color.web("#f9f9f9"), new CornerRadii(12), Insets.EMPTY)));
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 12; -fx-background-radius: 12;");

        Label nom = new Label("Nom : " + p.getNomProduit());
        Label prix = new Label("Prix : " + p.getPrixProduit() + " €");
        Label quantite = new Label("Quantité : " + p.getQuantite());
        Label statut = new Label("Statut : " + p.getStatutProduit());

        Button addToCartButton = new Button("🛒 Ajouter au panier");
        addToCartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        if (p.getQuantite() == 0) {
            statut.setTextFill(Color.RED);
            addToCartButton.setDisable(true);
            addToCartButton.setStyle("-fx-background-color: #cccccc; -fx-text-fill: white;");
            Tooltip tooltip = new Tooltip("Produit indisponible");
            Tooltip.install(addToCartButton, tooltip);
        } else {
            statut.setTextFill(Color.GREEN);
            addToCartButton.setOnAction(e -> {
                panier.add(p);

                // Ajouter notification sans ouvrir la fenêtre
                NotificationManager.addNotification("Produit ajouté au panier : " + p.getNomProduit());
                NotificationManager.openNotificationWindow();


                if (p.getQuantite() == 1) {
                    p.setQuantite(0);
                    p.setStatutProduit("Indisponible");
                    produitService.update(p);
                    NotificationManager.addNotification("Produit en rupture de stock : " + p.getNomProduit());
                    NotificationManager.openNotificationWindow();

                } else {
                    p.setQuantite(p.getQuantite() - 1);
                    produitService.update(p);
                }

                loadProducts(); // rafraîchir l'affichage
            });
        }

        card.getChildren().addAll(nom, prix, quantite, statut, addToCartButton);
        return card;
    }


    @FXML
    private void filterByCategorie() {
        String selectedCategorie = categorieComboBox.getValue();
        if ("Tous".equals(selectedCategorie)) {
            allProducts = produitService.find();
        } else {
            allProducts = produitService.findByCategorie(selectedCategorie);
        }
        currentPage = 0;
        showPage(currentPage);
        updatePaginationButtons();
    }

    @FXML
    private void sortProducts() {
        String selectedSort = sortComboBox.getValue();
        allProducts = produitService.find();

        switch (selectedSort) {
            case "Nom croissant":
                allProducts.sort((p1, p2) -> p1.getNomProduit().compareToIgnoreCase(p2.getNomProduit()));
                break;
            case "Prix croissant":
                allProducts.sort((p1, p2) -> Integer.compare(p1.getPrixProduit(), p2.getPrixProduit()));
                break;
            case "Prix décroissant":
                allProducts.sort((p1, p2) -> Integer.compare(p2.getPrixProduit(), p1.getPrixProduit()));
                break;
            case "Quantité croissante":
                allProducts.sort((p1, p2) -> Integer.compare(p1.getQuantite(), p2.getQuantite()));
                break;
            case "Quantité décroissante":
                allProducts.sort((p1, p2) -> Integer.compare(p2.getQuantite(), p1.getQuantite()));
                break;
            default:
                break;
        }

        currentPage = 0;
        showPage(currentPage);
        updatePaginationButtons();
    }

    @FXML
    private void openPanier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/panier.fxml"));
            Parent root = loader.load();

            PanierController controller = loader.getController();
            controller.setPanier(panier);

            Stage stage = new Stage();
            stage.setTitle("Mon Panier");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
