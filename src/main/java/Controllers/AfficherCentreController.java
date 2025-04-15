package Controllers;

import Entities.Centre;
import Services.CentreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.List;

public class AfficherCentreController {


    @FXML private FlowPane cardsContainer;
    private final CentreService centreService = new CentreService();
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResource("/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle();
        clip.setRadius(25); // half of 120 width/height
        clip.setCenterX(25);
        clip.setCenterY(25);
        logoImage.setClip(clip);
        loadCentres();

    }

    private void loadCentres() {
        cardsContainer.getChildren().clear();

        List<Centre> centres = centreService.find();
        for (Centre centre : centres) {
            cardsContainer.getChildren().add(createCentreCard(centre));
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
    private void handleVoirPacks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPack.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setTitle("Gestion des Packs");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la scène des packs", Alert.AlertType.ERROR);
        }
    }



    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createCentreCard(Centre centre) {
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
            if (centre.getPhotoCentre() != null && !centre.getPhotoCentre().isEmpty()) {
                imageView.setImage(new Image("file:" + centre.getPhotoCentre()));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/default_centre.jpeg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/default_centre.jpeg")));
        }
        imageView.setFitWidth(220); // S'assurer que l'image a la largeur de la carte
        imageView.setFitHeight(120); // Ajuster la hauteur de l'image
        imageView.setPreserveRatio(true);

        // Titre
        Label title = new Label(centre.getNomCentre());
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Détails
        Label adresse = new Label("Adresse: " + centre.getAdresseCentre());
        Label telephone = new Label("Téléphone: " + centre.getTelCentre());
        Label email = new Label("Email: " + centre.getEmailCentre());
        Label specialite = new Label("Spécialité: " + centre.getSpecialiteCentre());
        Label capacite = new Label("Capacité: " + centre.getCapaciteCentre());

        // Boutons
        HBox buttons = new HBox(10);
        Button modifier = new Button("Modifier");
        Button supprimer = new Button("Supprimer");

        modifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        supprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        modifier.setOnAction(e -> openModificationWindow(centre));
        supprimer.setOnAction(e -> deleteCentre(centre));

        buttons.getChildren().addAll(modifier, supprimer);

        card.getChildren().addAll(
                imageView,
                title,
                new Separator(),
                adresse,
                telephone,
                email,
                specialite,
                capacite,
                buttons
        );

        return card;
    }

    private void openModificationWindow(Centre centre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCentre.fxml"));
            Parent root = loader.load();

            ModifierCentreController controller = loader.getController();
            controller.setCentreAModifier(centre);

            Stage stage = new Stage();
            stage.setTitle("Modifier Centre - " + centre.getNomCentre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCentres(); // Rafraîchir après modification
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", Alert.AlertType.ERROR);
        }
    }

    private void deleteCentre(Centre centre) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer ce centre ?");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer le centre " + centre.getNomCentre() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                centreService.delete(centre);
                loadCentres();
                showAlert("Succès", "Centre supprimé avec succès", Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void handleAjouterCentre(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCentre.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau centre");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCentres(); // Rafraîchir après ajout
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout", Alert.AlertType.ERROR);
        }
    }


}
