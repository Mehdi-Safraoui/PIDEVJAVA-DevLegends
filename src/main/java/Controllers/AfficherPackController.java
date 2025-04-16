package Controllers;

import Entities.Pack;
import Services.PackService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class AfficherPackController {

    @FXML private FlowPane cardsContainer;
    private final PackService packService = new PackService();
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle();
        clip.setRadius(25); // half of 120 width/height
        clip.setCenterX(25);
        clip.setCenterY(25);
        logoImage.setClip(clip);
        loadPacks();
    }

    private void loadPacks() {
        cardsContainer.getChildren().clear();

        List<Pack> packs = packService.find();
        for (Pack pack : packs) {
            cardsContainer.getChildren().add(createPackCard(pack));
        }
    }
    @FXML
    private void handleVoirContrats(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherContrat.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setTitle("Gestion des Contrats");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la scène des contrats", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVoirCentres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCentre.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setTitle("Gestion des Centres");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la scène des centres", Alert.AlertType.ERROR);
        }
    }



    // Méthode pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createPackCard(Pack pack) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 15;");
        card.setMaxWidth(220);  // Assurez-vous que chaque carte ait la même largeur
        card.setPrefWidth(220); // Fixe la largeur de chaque carte
        card.setPrefHeight(300); // Fixe la hauteur de chaque carte

        // Ajouter un effet 3D avec une ombre sans incliner la carte
        card.setEffect(new javafx.scene.effect.DropShadow(20, 10, 10, javafx.scene.paint.Color.GRAY));

        // Image
        ImageView imageView = new ImageView();
        try {
            if (pack.getPhotoPack() != null && !pack.getPhotoPack().isEmpty()) {
                imageView.setImage(new Image("file:" + pack.getPhotoPack()));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/default_pack.jpeg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/default_pack.jpeg")));
        }
        imageView.setFitWidth(220); // S'assurer que l'image a la largeur de la carte
        imageView.setFitHeight(120); // Ajuster la hauteur de l'image
        imageView.setPreserveRatio(true);

        // Titre
        Label title = new Label(pack.getNomPack());
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Détails
        Label description = new Label("Description: " + pack.getDescriptPack());
        Label prix = new Label("Prix: " + pack.getPrixPack() + "TND");
        Label duree = new Label("Durée: " + pack.getDureePack() + " Jours");
        Label discountCode = new Label("Code Promo: " + (pack.getDiscountCode() != null ? pack.getDiscountCode() : "N/A"));
        Label isUsed = new Label("Utilisé: " + (pack.isUsed() ? "Oui" : "Non"));

        // Boutons
        HBox buttons = new HBox(10);
        Button modifier = new Button("Modifier");
        Button supprimer = new Button("Supprimer");

        modifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        supprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        modifier.setOnAction(e -> openModificationWindow(pack));
        supprimer.setOnAction(e -> deletePack(pack));

        buttons.getChildren().addAll(modifier, supprimer);

        card.getChildren().addAll(
                imageView,
                title,
                new Separator(),
                description,
                prix,
                duree,
                discountCode,
                isUsed,
                buttons
        );

        return card;
    }

    private void openModificationWindow(Pack pack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPack.fxml"));
            Parent root = loader.load();

            ModifierPackController controller = loader.getController();
            controller.setPackAModifier(pack);

            Stage stage = new Stage();
            stage.setTitle("Modifier Pack - " + pack.getNomPack());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadPacks(); // Rafraîchir après modification
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", Alert.AlertType.ERROR);
        }
    }

    private void deletePack(Pack pack) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer ce pack ?");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer le pack " + pack.getNomPack() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                packService.delete(pack);
                loadPacks();
                showAlert("Succès", "Pack supprimé avec succès", Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void handleAjouterPack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPack.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau pack");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadPacks(); // Rafraîchir après ajout
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout", Alert.AlertType.ERROR);
        }
    }
}
