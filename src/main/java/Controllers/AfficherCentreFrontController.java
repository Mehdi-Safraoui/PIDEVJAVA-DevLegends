package Controllers;

import Entities.Centre;
import Services.CentreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherCentreFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private ImageView logoImage;
    @FXML private Button retourAccueilBtn;

    private final CentreService centreService = new CentreService();

    @FXML
    public void initialize() {
        // Logo circulaire
        Image img = new Image(getClass().getResource("/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle(25, 25, 25);
        logoImage.setClip(clip);

        // Chargement des centres
        loadCentres();
    }

    private void loadCentres() {
        cardsContainer.getChildren().clear();
        List<Centre> centres = centreService.find();

        for (Centre centre : centres) {
            cardsContainer.getChildren().add(createCentreCard(centre));
        }
    }

    private VBox createCentreCard(Centre centre) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; " +
                "-fx-padding: 15;");
        card.setPrefWidth(250);  // largeur spécifique pour avoir 3 cartes par ligne
        card.setPrefHeight(300); // hauteur fixe

        card.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.rgb(0, 0, 0, 0.1)));

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
        imageView.setFitWidth(220);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // Infos
        Label title = new Label(centre.getNomCentre());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label adresse = new Label(centre.getAdresseCentre());
        adresse.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Label telephone = new Label("Tél: " + centre.getTelCentre());
        telephone.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Label specialite = new Label("Spécialité: " + centre.getSpecialiteCentre());
        specialite.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Separator separator = new Separator();

        card.getChildren().addAll(imageView, title, separator, adresse, telephone, specialite);
        return card;
    }

    @FXML
    private void handleRetourAccueil() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AccueilFront.fxml"));
            Stage stage = (Stage) retourAccueilBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 640));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page d'accueil", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
