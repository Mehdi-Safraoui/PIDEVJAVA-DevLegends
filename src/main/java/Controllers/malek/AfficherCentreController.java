package Controllers.malek;

import Entities.malek.Centre;
import Services.malek.CentreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class AfficherCentreController {

    @FXML private FlowPane cardsContainer;
    @FXML private ImageView logoImage;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private Button btnPrevPage;
    @FXML private Button btnNextPage;
    @FXML private Label pageLabel;

    private final CentreService centreService = new CentreService();
    private final int ITEMS_PER_PAGE = 3;
    private int currentPage = 0;
    private List<Centre> allCentres;
    private List<Centre> filteredCentres;

    @FXML
    public void initialize() {
        setupLogo();
        setupComboBox();
        setupSearchField();
        loadData();
        setupEventHandlers();
    }
    @FXML
    private void openAjouterCentreWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/AjouterCentre.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau centre");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Rafraîchir la liste après l'ajout
            refreshData();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyBzBZ_pFBvC_qCOp4AaLPJXIwGoze0wyNo";

    private void showMapForCentre(Centre centre) {
        try {
            Stage mapStage = new Stage();
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            // Construction de l'URL Google Maps Embed
            String encodedAddress = java.net.URLEncoder.encode(centre.getAdresseCentre(), "UTF-8");
            String mapUrl = String.format(
                    "https://www.google.com/maps/embed/v1/place?key=%s&q=%s",
                    GOOGLE_MAPS_API_KEY,
                    encodedAddress
            );

            // HTML intégrant la carte
            String htmlContent = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { margin: 0; padding: 0; }
                        #map { height: 100%%; width: 100%%; }
                    </style>
                </head>
                <body>
                    <iframe
                        width="100%%"
                        height="100%%"
                        frameborder="0"
                        style="border:0"
                        src="%s"
                        allowfullscreen>
                    </iframe>
                </body>
                </html>
                """, mapUrl);

            webEngine.loadContent(htmlContent);

            StackPane root = new StackPane(webView);
            Scene scene = new Scene(root, 800, 600);

            mapStage.setTitle("Localisation: " + centre.getNomCentre());
            mapStage.setScene(scene);
            mapStage.show();

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'afficher la carte: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupLogo() {
        try {
            Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
            logoImage.setImage(img);
            logoImage.setClip(new javafx.scene.shape.Circle(25, 25, 25));
        } catch (Exception e) {
            System.err.println("Erreur de chargement du logo: " + e.getMessage());
        }
    }

    private void setupComboBox() {
        sortComboBox.getItems().addAll("Nom (A-Z)", "Nom (Z-A)");
        sortComboBox.getSelectionModel().select(0);
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCentres(newValue));
    }

    private void loadData() {
        allCentres = centreService.find();
        filteredCentres = allCentres;
        loadCentresPage(currentPage);
        updatePaginationButtons();
    }

    private void setupEventHandlers() {
        sortComboBox.setOnAction(event -> handleSort());
        btnNextPage.setOnAction(event -> handleNextPage());
        btnPrevPage.setOnAction(event -> handlePreviousPage());
    }

    private void filterCentres(String query) {
        filteredCentres = allCentres.stream()
                .filter(centre -> centre.getNomCentre().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        currentPage = 0;
        loadCentresPage(currentPage);
        updatePaginationButtons();
    }

    private void handleSort() {
        String selectedSort = sortComboBox.getValue();
        if ("Nom (A-Z)".equals(selectedSort)) {
            filteredCentres.sort((c1, c2) -> c1.getNomCentre().compareTo(c2.getNomCentre()));
        } else {
            filteredCentres.sort((c1, c2) -> c2.getNomCentre().compareTo(c1.getNomCentre()));
        }
        loadCentresPage(currentPage);
    }

    private void loadCentresPage(int pageIndex) {
        cardsContainer.getChildren().clear();

        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, filteredCentres.size());
        List<Centre> pageItems = filteredCentres.subList(fromIndex, toIndex);

        pageItems.forEach(centre -> cardsContainer.getChildren().add(createCentreCard(centre)));

        updatePageLabel();
    }

    private void updatePageLabel() {
        int totalPages = (int) Math.ceil((double) filteredCentres.size() / ITEMS_PER_PAGE);
        pageLabel.setText(String.format("Page %d / %d", currentPage + 1, totalPages));
    }

    private void updatePaginationButtons() {
        btnPrevPage.setDisable(currentPage == 0);
        btnNextPage.setDisable((currentPage + 1) * ITEMS_PER_PAGE >= filteredCentres.size());
    }

    private void handleNextPage() {
        if ((currentPage + 1) * ITEMS_PER_PAGE < filteredCentres.size()) {
            currentPage++;
            loadCentresPage(currentPage);
            updatePaginationButtons();
        }
    }

    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadCentresPage(currentPage);
            updatePaginationButtons();
        }
    }



    private VBox createCentreCard(Centre centre) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-padding: 15;");
        card.setMaxWidth(250);
        card.setPrefWidth(250);
        card.setPrefHeight(350);

        // Effet d'ombre
        card.setEffect(new DropShadow(10, javafx.scene.paint.Color.gray(0.5)));

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
        imageView.setSmooth(true);

        // Titre
        Label title = new Label(centre.getNomCentre());
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Détails
        Label adresse = new Label("Adresse: " + centre.getAdresseCentre());
        Label telephone = new Label("Téléphone: " + centre.getTelCentre());
        Label email = new Label("Email: " + centre.getEmailCentre());
        Label specialite = new Label("Spécialité: " + centre.getSpecialiteCentre());
        Label capacite = new Label("Capacité: " + centre.getCapaciteCentre());

        // Style des labels
        String labelStyle = "-fx-text-fill: #34495e; -fx-font-size: 12px;";
        adresse.setStyle(labelStyle);
        telephone.setStyle(labelStyle);
        email.setStyle(labelStyle);
        specialite.setStyle(labelStyle);
        capacite.setStyle(labelStyle);

        // Boutons
        HBox buttons = new HBox(10); // DÉCLARATION DE LA VARIABLE buttons
        buttons.setAlignment(Pos.CENTER);

        // Bouton "Voir sur la carte"
        Button mapButton = new Button("Voir sur la carte");
        mapButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        mapButton.setOnAction(e -> showMapForCentre(centre));

        // Boutons existants
        Button modifier = new Button("Modifier");
        Button supprimer = new Button("Supprimer");

        modifier.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        supprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        modifier.setOnAction(e -> openModificationWindow(centre));
        supprimer.setOnAction(e -> deleteCentre(centre));

        // Ajouter tous les boutons à la HBox
        buttons.getChildren().addAll(mapButton, modifier, supprimer);

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


    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private HBox createActionButtons(Centre centre) {
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button modifier = new Button("Modifier");
        modifier.getStyleClass().add("btn-modifier");
        modifier.setOnAction(e -> openModificationWindow(centre));

        Button supprimer = new Button("Supprimer");
        supprimer.getStyleClass().add("btn-supprimer");
        supprimer.setOnAction(e -> deleteCentre(centre));

        buttons.getChildren().addAll(modifier, supprimer);
        return buttons;
    }

    private void openModificationWindow(Centre centre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/ModifierCentre.fxml"));
            Parent root = loader.load();

            ModifierCentreController controller = loader.getController();
            controller.setCentreAModifier(centre);

            Stage stage = new Stage();
            stage.setTitle("Modifier Centre - " + centre.getNomCentre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshData();
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
                refreshData();
                showAlert("Succès", "Centre supprimé avec succès", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void refreshData() {
        allCentres = centreService.find();
        filteredCentres = allCentres;
        if (currentPage > 0 && currentPage * ITEMS_PER_PAGE >= filteredCentres.size()) {
            currentPage--;
        }
        loadCentresPage(currentPage);
        updatePaginationButtons();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}