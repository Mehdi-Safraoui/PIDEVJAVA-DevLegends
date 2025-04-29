package Controllers.malek;

import Entities.malek.Contrat;
import Entities.malek.Centre;
import Utils.GeocodingService;
import Entities.salsabil.User;
import javafx.application.Platform;
import Utils.Session;
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
import javafx.collections.FXCollections;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class AfficherContratFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private ImageView logoImage;
    @FXML private Button btnSuivant;
    @FXML private Button btnPrecedent;
    @FXML private Label pageLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;

    private final ContratService contratService = new ContratService();
    private final CentreService centreService = new CentreService();
    private final Map<Integer, Image> imageCache = new HashMap<>();

    private final int contratsParPage = 3;
    private int pageActuelle = 0;
    private List<Contrat> tousLesContrats;
    private List<Contrat> contratsFiltres;

    @FXML
    public void initialize() {
        setupLogo();
        setupComboBox();
        loadInitialData();
        setupListeners();
    }

    private void setupLogo() {
        try {
            Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
            logoImage.setImage(img);
            logoImage.setClip(new Circle(25, 25, 25));
        } catch (Exception e) {
            System.err.println("Erreur de chargement du logo: " + e.getMessage());
        }
    }

    private void setupComboBox() {
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Date début (récent)",
                "Date début (ancien)",
                "Date fin (récent)",
                "Date fin (ancien)"
        ));
    }

    private void loadInitialData() {
        int userId = Session.getCurrentUser().getId(); // Utilisateur connecté
        tousLesContrats = contratService.findByUserId(userId); // Contrats de l'utilisateur
        contratsFiltres = new ArrayList<>(tousLesContrats); // Prépare les contrats filtrés
        afficherContratsParPage(pageActuelle); // Affiche page actuelle
    }



    private void setupListeners() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filtrerEtTrier());
        dateDebutPicker.valueProperty().addListener((obs, oldVal, newVal) -> filtrerEtTrier());
        dateFinPicker.valueProperty().addListener((obs, oldVal, newVal) -> filtrerEtTrier());
        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> filtrerEtTrier());
    }

    private void filtrerEtTrier() {
        filtrerContrats();
        trierContrats();
        pageActuelle = 0;
        afficherContratsParPage(pageActuelle);
    }

    private void filtrerContrats() {
        String recherche = searchField.getText().toLowerCase();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        contratsFiltres = tousLesContrats.stream()
                .filter(contrat -> {
                    // Filtre par nom de centre
                    Centre centre = centreService.findById(contrat.getCentreId());
                    String nomCentre = centre != null ? centre.getNomCentre().toLowerCase() : "";
                    boolean matchRecherche = recherche.isEmpty() || nomCentre.contains(recherche);

                    // Filtre par date
                    boolean matchDateDebut = dateDebut == null ||
                            (contrat.getDatdebCont() != null &&
                                    convertToLocalDate(contrat.getDatdebCont()).isAfter(dateDebut.minusDays(1)));

                    boolean matchDateFin = dateFin == null ||
                            (contrat.getDatfinCont() != null &&
                                    convertToLocalDate(contrat.getDatfinCont()).isBefore(dateFin.plusDays(1)));

                    return matchRecherche && matchDateDebut && matchDateFin;
                })
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) return null;
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void trierContrats() {
        String critereTri = sortComboBox.getValue();
        if (critereTri == null || contratsFiltres == null) return;

        switch (critereTri) {
            case "Date début (récent)":
                contratsFiltres.sort((c1, c2) -> c2.getDatdebCont().compareTo(c1.getDatdebCont()));
                break;
            case "Date début (ancien)":
                contratsFiltres.sort((c1, c2) -> c1.getDatdebCont().compareTo(c2.getDatdebCont()));
                break;
            case "Date fin (récent)":
                contratsFiltres.sort((c1, c2) -> c2.getDatfinCont().compareTo(c1.getDatfinCont()));
                break;
            case "Date fin (ancien)":
                contratsFiltres.sort((c1, c2) -> c1.getDatfinCont().compareTo(c2.getDatfinCont()));
                break;
        }
    }

    private void afficherContratsParPage(int page) {
        cardsContainer.getChildren().clear();

        int start = page * contratsParPage;
        int end = Math.min(start + contratsParPage, contratsFiltres.size());

        for (int i = start; i < end; i++) {
            cardsContainer.getChildren().add(createContratCard(contratsFiltres.get(i)));
        }

        updatePaginationUI();
    }


    private void updatePaginationUI() {
        int totalPages = Math.max(1, (int) Math.ceil((double) contratsFiltres.size() / contratsParPage));
        pageLabel.setText("Page " + (pageActuelle + 1) + " / " + totalPages);
        btnPrecedent.setDisable(pageActuelle == 0);
        btnSuivant.setDisable((pageActuelle + 1) * contratsParPage >= contratsFiltres.size());
    }

    @FXML
    private void pageSuivante() {
        if ((pageActuelle + 1) * contratsParPage < contratsFiltres.size()) {
            pageActuelle++;
            afficherContratsParPage(pageActuelle);
        }
    }

    @FXML
    private void pagePrecedente() {
        if (pageActuelle > 0) {
            pageActuelle--;
            afficherContratsParPage(pageActuelle);
        }
    }

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        sortComboBox.getSelectionModel().clearSelection();
        filtrerEtTrier();
    }

    private VBox createContratCard(Contrat contrat) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 15;");
        card.setMaxWidth(250);
        card.setPrefWidth(250);
        card.setPrefHeight(380); // Augmenté pour la distance

        Centre centre = centreService.findById(contrat.getCentreId());
        User user = Session.getCurrentUser();

        // Image du centre
        ImageView centreImageView = new ImageView();
        centreImageView.setFitWidth(200);
        centreImageView.setFitHeight(120);
        centreImageView.setPreserveRatio(true);
        centreImageView.setSmooth(true);

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

        // Titre et informations
        Label title = new Label("Contrat #" + contrat.getId());
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        String nomCentre = centre != null ? centre.getNomCentre() : "Centre inconnu";
        Label centreLabel = new Label(nomCentre);
        centreLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label dateDebut = new Label("Début: " + formatDate(contrat.getDatdebCont()));
        Label dateFin = new Label("Fin: " + formatDate(contrat.getDatfinCont()));
        Label paiementLabel = new Label("Paiement: " + contrat.getModpaimentCont());

        Label renouvLabel = new Label("Renouvellement: " + (contrat.isRenouvAutoCont() ? "Auto" : "Manuel"));
        renouvLabel.setStyle(contrat.isRenouvAutoCont() ? "-fx-text-fill: #388E3C;" : "-fx-text-fill: #D32F2F;");

        // Label de distance
        Label distanceLabel = new Label("Calcul en cours...");
        distanceLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

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
                distanceLabel
        );

        // Calcul asynchrone de la distance
        calculateAndDisplayDistance(user, centre, distanceLabel);

        return card;
    }


    private void calculateAndDisplayDistance(User user, Centre centre, Label distanceLabel) {
        new Thread(() -> {
            try {
                if (user == null || centre == null || user.getAddress() == null || centre.getAdresseCentre() == null) {
                    Platform.runLater(() -> distanceLabel.setText("Distance: N/A"));
                    return;
                }

                double[] userCoords = GeocodingService.getCoordinates(user.getAddress());
                double[] centreCoords = GeocodingService.getCoordinates(centre.getAdresseCentre());

                if (userCoords != null && centreCoords != null) {
                    double distance = GeocodingService.calculateDistance(
                            userCoords[0], userCoords[1],
                            centreCoords[0], centreCoords[1]
                    );

                    Platform.runLater(() ->
                            distanceLabel.setText(String.format("Distance: %.2f km", distance))
                    );
                } else {
                    Platform.runLater(() -> distanceLabel.setText("Distance: N/A"));
                }
            } catch (Exception e) {
                Platform.runLater(() -> distanceLabel.setText("Distance: Erreur"));
                System.err.println("Erreur calcul distance: " + e.getMessage());
            }
        }).start();
    }

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

            reloadData();
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
                reloadData();
                showAlert("Succès", "Contrat supprimé", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void reloadData() {
        tousLesContrats = contratService.find();
        filtrerEtTrier();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}