package Controllers.malek;

import Entities.malek.Pack;
import Services.malek.PackService;
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

public class AfficherPackFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private ImageView logoImage;
    @FXML private Button retourAccueilBtn;

    private final PackService packService = new PackService();

    @FXML
    public void initialize() {
        // Logo circulaire
        Image img = new Image(getClass().getResource("/logo.png").toExternalForm());
        logoImage.setImage(img);
        logoImage.setClip(new Circle(25, 25, 25));

        // Chargement des packs
        loadPacks();
    }

    private void loadPacks() {
        cardsContainer.getChildren().clear();
        List<Pack> packs = packService.find();

        for (Pack pack : packs) {
            cardsContainer.getChildren().add(createPackCard(pack));
        }
    }

    private VBox createPackCard(Pack pack) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; " +
                "-fx-padding: 15;");
        card.setPrefSize(250, 280); // Taille fixe pour 3 par ligne
        card.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.rgb(0, 0, 0, 0.1)));

        // Image (optionnelle)
        ImageView imageView = new ImageView();
        try {
            if (pack.getPhotoPack() != null && !pack.getPhotoPack().isEmpty()) {
                imageView.setImage(new Image("file:" + pack.getPhotoPack()));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/default_pack.jpg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/default_pack.jpg")));
        }
        imageView.setFitWidth(220);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // Infos du pack
        Label nom = new Label(pack.getNomPack());
        nom.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label description = new Label(pack.getDescriptPack());
        description.setWrapText(true);
        description.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Label prix = new Label("Prix: " + pack.getPrixPack() + " DT");
        prix.setStyle("-fx-font-size: 14px; -fx-text-fill: #00796B;");

        card.getChildren().addAll(imageView, nom, description, prix);
        return card;
    }

    @FXML
    private void handleRetourAccueil() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AccueilFront.fxml"));
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
