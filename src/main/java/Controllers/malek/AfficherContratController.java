package Controllers.malek;

import Entities.malek.Contrat;
import Entities.malek.Centre;
import Services.malek.CentreService;
import Services.malek.ContratService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AfficherContratController {

    @FXML private FlowPane cardsContainer;
    private final ContratService contratService = new ContratService();
    private final CentreService centreService = new CentreService();
    @FXML private ImageView logoImage;
    private final Map<Integer, Image> imageCache = new HashMap<>();

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle();
        clip.setRadius(25);
        clip.setCenterX(25);
        clip.setCenterY(25);
        logoImage.setClip(clip);
        loadContrats();
    }

    private void loadContrats() {
        cardsContainer.getChildren().clear();
        List<Contrat> contrats = contratService.find();

        for (Contrat contrat : contrats) {
            cardsContainer.getChildren().add(createContratCard(contrat));
        }
    }

    private VBox createContratCard(Contrat contrat) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 15;");
        card.setMaxWidth(250);
        card.setPrefWidth(250);
        card.setPrefHeight(350); // Augmenté pour accommoder l'image plus grande

        // Effet d'ombre pour le style carte
        card.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.GRAY));

        // Récupérer le centre associé
        Centre centre = centreService.findById(contrat.getCentreId());

        // Image du centre
        ImageView centreImageView = new ImageView();
        centreImageView.setFitWidth(200);
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
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        // Centre associé
        String nomCentre = centre != null ? centre.getNomCentre() : "Centre inconnu";
        Label centreLabel = new Label(nomCentre);
        centreLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        // Dates
        Label dateDebut = new Label("Début: " + formatDate(contrat.getDatdebCont()));
        Label dateFin = new Label("Fin: " + formatDate(contrat.getDatfinCont()));

        // Mode de paiement
        Label paiementLabel = new Label("Paiement: " + contrat.getModpaimentCont());

        // Renouvellement automatique
        Label renouvLabel = new Label("Renouvellement: " + (contrat.isRenouvAutoCont() ? "Auto" : "Manuel"));
        renouvLabel.setStyle(contrat.isRenouvAutoCont() ? "-fx-text-fill: #388E3C;" : "-fx-text-fill: #D32F2F;");

        // Boutons d'action
        HBox buttonsBox = new HBox(10);
        Button editBtn = new Button("Modifier");
        Button deleteBtn = new Button("Supprimer");

        editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

        editBtn.setOnAction(e -> openModificationWindow(contrat));
        deleteBtn.setOnAction(e -> deleteContrat(contrat));

        buttonsBox.getChildren().addAll(editBtn, deleteBtn);

        // Ajout des éléments à la carte
        card.getChildren().addAll(
                centreImageView,
                title,
                new Separator(),
                centreLabel,
                dateDebut,
                dateFin,
                paiementLabel,
                renouvLabel,
                buttonsBox
        );

        return card;
    }

    // ... (les autres méthodes restent inchangées)
    private String formatDate(Date date) {
        if (date == null) return "Non spécifié";
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private void openModificationWindow(Contrat contrat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/ModifierContrat.fxml"));
            Parent root = loader.load();

            ModifierContratController controller = loader.getController();
            controller.setContratToEdit(contrat);

            Stage stage = new Stage();
            stage.setTitle("Modifier Contrat #" + contrat.getId());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadContrats(); // Rafraîchir l'affichage
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'éditeur", Alert.AlertType.ERROR);
        }
    }

    private void deleteContrat(Contrat contrat) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer ce contrat ?");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer le contrat #" + contrat.getId() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                contratService.delete(contrat);
                loadContrats();
                showAlert("Succès", "Contrat supprimé", Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void handleAjouterContrat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/AjouterContrat.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nouveau Contrat");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadContrats();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVoirCentres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/AfficherCentre.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setTitle("Gestion des Centres");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger les centres", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleVoirPacks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/AfficherPack.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setTitle("Gestion des Packs");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger les packs", Alert.AlertType.ERROR);
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