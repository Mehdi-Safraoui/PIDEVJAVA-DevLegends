package Controllers.malek;

import Entities.malek.Centre;
import Services.malek.CentreService;
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
import java.util.stream.Collectors;

public class AfficherCentreFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private ImageView logoImage;
    @FXML private Button retourAccueilBtn;
    @FXML private Button btnNext;
    @FXML private Button btnPrev;
    @FXML private Label pageLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;

    private final CentreService centreService = new CentreService();
    private int currentPage = 0;
    private final int itemsPerPage = 3;
    private List<Centre> allCentres;
    private List<Centre> filteredCentres;

    @FXML
    public void initialize() {
        // Logo circulaire
        setupLogo();

        // Initialiser les composants de recherche et tri
        setupSearchAndSort();

        // Charger les données
        loadInitialData();
    }

    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyBzBZ_pFBvC_qCOp4AaLPJXIwGoze0wyNo"; // ta clé API

    private void showMapForCentre(Centre centre) {
        try {
            Stage mapStage = new Stage();
            javafx.scene.web.WebView webView = new javafx.scene.web.WebView();
            javafx.scene.web.WebEngine webEngine = webView.getEngine();

            String encodedAddress = java.net.URLEncoder.encode(centre.getAdresseCentre(), "UTF-8");
            String mapUrl = String.format(
                    "https://www.google.com/maps/embed/v1/place?key=%s&q=%s",
                    GOOGLE_MAPS_API_KEY,
                    encodedAddress
            );

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

            javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane(webView);
            Scene scene = new Scene(root, 800, 600);

            mapStage.setTitle("Localisation: " + centre.getNomCentre());
            mapStage.setScene(scene);
            mapStage.show();

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'afficher la carte: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void setupLogo() {
        Image img = new Image(getClass().getResource("/logo.png").toExternalForm());
        logoImage.setImage(img);
        logoImage.setClip(new Circle(25, 25, 25));
    }

    private void setupSearchAndSort() {
        // Initialiser le ComboBox de tri
        sortComboBox.getItems().addAll("Nom (A-Z)", "Nom (Z-A)");
        sortComboBox.getSelectionModel().select(0);

        // Configurer le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCentres(newValue);
        });

        // Configurer le tri
        sortComboBox.setOnAction(event -> handleSort());
    }

    private void loadInitialData() {
        allCentres = centreService.find();
        filteredCentres = allCentres;
        loadPage(currentPage);
        updatePaginationButtons();
    }

    private void filterCentres(String query) {
        filteredCentres = allCentres.stream()
                .filter(centre -> centre.getNomCentre().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        currentPage = 0;
        loadPage(currentPage);
        updatePaginationButtons();
    }

    private void handleSort() {
        String selectedSort = sortComboBox.getValue();
        if ("Nom (A-Z)".equals(selectedSort)) {
            filteredCentres.sort((c1, c2) -> c1.getNomCentre().compareTo(c2.getNomCentre()));
        } else {
            filteredCentres.sort((c1, c2) -> c2.getNomCentre().compareTo(c1.getNomCentre()));
        }
        loadPage(currentPage);
    }

    private void loadPage(int pageIndex) {
        cardsContainer.getChildren().clear();

        int start = pageIndex * itemsPerPage;
        int end = Math.min(start + itemsPerPage, filteredCentres.size());

        for (int i = start; i < end; i++) {
            cardsContainer.getChildren().add(createCentreCard(filteredCentres.get(i)));
        }

        updatePaginationButtons();
    }

    private VBox createCentreCard(Centre centre) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-padding: 15;");
        card.setPrefWidth(250);
        card.setPrefHeight(350); // J'augmente légèrement pour laisser de la place au bouton
        card.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.rgb(0, 0, 0, 0.1)));

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

        Label title = new Label(centre.getNomCentre());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label adresse = new Label(centre.getAdresseCentre());
        adresse.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Label telephone = new Label("Tél: " + centre.getTelCentre());
        telephone.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Label specialite = new Label("Spécialité: " + centre.getSpecialiteCentre());
        specialite.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        Separator separator = new Separator();

        // Nouveau bouton "Voir sur la carte"
        Button mapButton = new Button("Voir sur la carte");
        mapButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        mapButton.setOnAction(e -> showMapForCentre(centre));

        card.getChildren().addAll(imageView, title, separator, adresse, telephone, specialite, mapButton);
        return card;
    }


    @FXML
    private void goToNextPage() {
        int totalPages = (int) Math.ceil((double) filteredCentres.size() / itemsPerPage);
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadPage(currentPage);
        }
    }

    @FXML
    private void goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadPage(currentPage);
        }
    }

    private void updatePaginationButtons() {
        int totalPages = (int) Math.ceil((double) filteredCentres.size() / itemsPerPage);
        btnPrev.setDisable(currentPage == 0);
        btnNext.setDisable(currentPage >= totalPages - 1);
        pageLabel.setText("Page " + (currentPage + 1) + " / " + totalPages);
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