package Controllers.malek;

import Entities.malek.Contrat;
import Entities.malek.Centre;
import Services.malek.CentreService;
import Services.malek.ContratService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AfficherContratFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private ImageView logoImage;
    @FXML private Button retourAccueilBtn;

    private final ContratService contratService = new ContratService();
    private final CentreService centreService = new CentreService();
    private final Map<Integer, Image> imageCache = new HashMap<>();

    @FXML
    public void initialize() {
        // Configuration du logo
        Image img = new Image(getClass().getResource("/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle(25, 25, 25);
        logoImage.setClip(clip);

        // Chargement des contrats
        loadContrats();
    }

    private void loadContrats() {
        cardsContainer.getChildren().clear();
        List<Contrat> contrats = contratService.find();

        if (contrats.isEmpty()) {
            Label noDataLabel = new Label("Aucun contrat disponible");
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            cardsContainer.getChildren().add(noDataLabel);
            return;
        }

        for (Contrat contrat : contrats) {
            cardsContainer.getChildren().add(createContratCard(contrat));
        }
    }

    private VBox createContratCard(Contrat contrat) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; " +
                "-fx-padding: 15;");

        // Définir une largeur pour 3 cartes par ligne
        card.setPrefWidth(270);  // Chaque carte prend 270px de largeur
        card.setPrefHeight(350); // Hauteur des cartes

        // Effet d'ombre
        card.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.rgb(0, 0, 0, 0.1)));

        // Récupérer le centre associé
        Centre centre = centreService.findById(contrat.getCentreId());

        // Image du centre
        ImageView centreImageView = new ImageView();
        centreImageView.setFitWidth(220);
        centreImageView.setFitHeight(120);
        centreImageView.setPreserveRatio(true);
        centreImageView.setSmooth(true);
        centreImageView.getStyleClass().add("centre-image");

        if (centre != null) {
            Image centreImage = imageCache.computeIfAbsent(centre.getId(), id -> {
                try {
                    if (centre.getPhotoCentre() != null && !centre.getPhotoCentre().isEmpty()) {
                        return new Image("file:" + centre.getPhotoCentre());
                    }
                } catch (Exception e) {
                    System.err.println("Erreur de chargement de l'image: " + e.getMessage());
                }
                return new Image(getClass().getResourceAsStream("/default_centre.png"));
            });
            centreImageView.setImage(centreImage);
        } else {
            centreImageView.setImage(new Image(getClass().getResourceAsStream("/default_centre.png")));
        }

        // Titre
        Label title = new Label("Contrat #" + contrat.getId());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        // Centre associé
        String nomCentre = centre != null ? centre.getNomCentre() : "Centre inconnu";
        Label centreLabel = new Label(nomCentre);
        centreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Dates
        Label dateDebut = new Label("Début: " + formatDate(contrat.getDatdebCont()));
        dateDebut.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label dateFin = new Label("Fin: " + formatDate(contrat.getDatfinCont()));
        dateFin.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Mode de paiement
        Label paiementLabel = new Label("Paiement: " + contrat.getModpaimentCont());
        paiementLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Renouvellement automatique
        Label renouvLabel = new Label("Renouvellement: " + (contrat.isRenouvAutoCont() ? "Auto" : "Manuel"));
        renouvLabel.setStyle(contrat.isRenouvAutoCont() ?
                "-fx-font-size: 14px; -fx-text-fill: #388E3C;" :
                "-fx-font-size: 14px; -fx-text-fill: #D32F2F;");

        // Ajout des éléments à la carte
        card.getChildren().addAll(
                centreImageView,
                title,
                new Separator(),
                centreLabel,
                dateDebut,
                dateFin,
                paiementLabel,
                renouvLabel
        );

        return card;
    }

    private String formatDate(Date date) {
        if (date == null) return "Non spécifié";
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    @FXML
    private void handleRetourAccueil() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AccueilFront.fxml"));
            Stage stage = (Stage) retourAccueilBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
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
