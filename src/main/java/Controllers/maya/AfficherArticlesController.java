package Controllers.maya;

import Entities.maya.ArticleConseil;
import Services.maya.ArticleConseilService;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class AfficherArticlesController {

    @FXML
    private GridPane container;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> comboTri;
    @FXML
    private CheckBox checkDesc;
    @FXML
    private Button btnPrecedent;
    @FXML
    private Button btnSuivant;
    @FXML
    private Label labelPage;


    private final ArticleConseilService service = new ArticleConseilService();
    private ObservableList<ArticleConseil> allArticles;
    private SortedList<ArticleConseil> currentSortedList;

    private int currentPage = 0;
    private final int itemsPerPage = 3;

    @FXML
    public void initialize() {
        afficherArticles();
        comboTri.setItems(FXCollections.observableArrayList("ID", "Titre", "Catégorie"));
        comboTri.getSelectionModel().select("ID");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAndSortArticles(newValue);
        });

        comboTri.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterAndSortArticles(searchField.getText());
        });

        checkDesc.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filterAndSortArticles(searchField.getText());
        });
    }

    private void afficherArticles() {
        allArticles = FXCollections.observableArrayList(service.find());
        filterAndSortArticles(null);
    }

    private void filterAndSortArticles(String searchText) {
        FilteredList<ArticleConseil> filteredData = new FilteredList<>(allArticles, article -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lowerCaseSearch = searchText.toLowerCase();
            return article.getTitreArticle().toLowerCase().contains(lowerCaseSearch) ||
                    article.getCategorieMentalArticle().toLowerCase().contains(lowerCaseSearch) ||
                    String.valueOf(article.getId()).contains(lowerCaseSearch);
        });

        String selectedSortCriterion = comboTri.getValue();
        if (selectedSortCriterion == null) {
            selectedSortCriterion = "ID";
        }
        final boolean descending = checkDesc.isSelected();

        SortedList<ArticleConseil> sortedData = new SortedList<>(filteredData);
        String finalSelectedSortCriterion = selectedSortCriterion;
        sortedData.setComparator((article1, article2) -> {
            int result = 0;
            switch (finalSelectedSortCriterion) {
                case "ID":
                    result = Integer.compare(article1.getId(), article2.getId());
                    break;
                case "Titre":
                    result = article1.getTitreArticle().compareToIgnoreCase(article2.getTitreArticle());
                    break;
                case "Catégorie":
                    result = article1.getCategorieMentalArticle().compareToIgnoreCase(article2.getCategorieMentalArticle());
                    break;
            }
            return descending ? -result : result;
        });

        currentSortedList = sortedData;
        currentPage = 0;
        updatePagination();
    }

    private void updatePagination() {
        container.getChildren().clear();
        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, currentSortedList.size());

        int row = 0, col = 0;
        for (int i = start; i < end; i++) {
            ArticleConseil article = currentSortedList.get(i);
            VBox card = createArticleCard(article);
            container.add(card, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        // Calculer le nombre total de pages
        int totalPages = (int) Math.ceil((double) currentSortedList.size() / itemsPerPage);

        // Mettre à jour le label de la page actuelle
        labelPage.setText("Page " + (currentPage + 1) + "/" + totalPages);

        // Désactiver les boutons Précédent/Suivant selon la page actuelle
        btnPrecedent.setDisable(currentPage == 0);
        btnSuivant.setDisable(end >= currentSortedList.size());
    }



//    private void updatePagination() {
//        container.getChildren().clear();
//        int start = currentPage * itemsPerPage;
//        int end = Math.min(start + itemsPerPage, currentSortedList.size());
//
//        int row = 0, col = 0;
//        for (int i = start; i < end; i++) {
//            ArticleConseil article = currentSortedList.get(i);
//            VBox card = createArticleCard(article);
//            container.add(card, col, row);
//            col++;
//            if (col > 2) {
//                col = 0;
//                row++;
//            }
//        }
//
//        btnPrecedent.setDisable(currentPage == 0);
//        btnSuivant.setDisable(end >= currentSortedList.size());
//    }

    @FXML
    private void handleSuivant() {
        if ((currentPage + 1) * itemsPerPage < currentSortedList.size()) {
            currentPage++;
            updatePagination();
        }
    }

    @FXML
    private void handlePrecedent() {
        if (currentPage > 0) {
            currentPage--;
            updatePagination();
        }
    }

    private VBox createArticleCard(ArticleConseil article) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");

        ImageView imageView = new ImageView();
        String imagePath = article.getImage();
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imagePath = "resources/images/default.png";
        }

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            imageView.setImage(new Image("file:" + imgFile.getAbsolutePath()));
        } else {
            imageView.setImage(new Image("file:resources/images/default.png"));
        }
        imageView.setFitHeight(120);
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);

        Rectangle overlay = new Rectangle(180, 120);
        overlay.setFill(Color.rgb(0, 0, 0, 0.3));
        overlay.setVisible(false);
        overlay.setArcWidth(10);
        overlay.setArcHeight(10);

        ImageView eyeIcon = new ImageView(new Image("file:resources/images/eye.png"));
        eyeIcon.setFitWidth(24);
        eyeIcon.setFitHeight(24);

        Button btnVisualiser = new Button();
        btnVisualiser.setGraphic(eyeIcon);
        btnVisualiser.setStyle("-fx-background-color: transparent;");
        btnVisualiser.setVisible(false);
        btnVisualiser.setOpacity(0);
        btnVisualiser.setScaleX(0.8);
        btnVisualiser.setScaleY(0.8);

        btnVisualiser.setOnAction(e -> afficherDetailsArticle(article));

        StackPane imageContainer = new StackPane(imageView, overlay, btnVisualiser);
        StackPane.setAlignment(btnVisualiser, Pos.CENTER);

        FadeTransition fadeInBtn = new FadeTransition(Duration.millis(200), btnVisualiser);
        fadeInBtn.setFromValue(0);
        fadeInBtn.setToValue(1);

        FadeTransition fadeOutBtn = new FadeTransition(Duration.millis(200), btnVisualiser);
        fadeOutBtn.setFromValue(1);
        fadeOutBtn.setToValue(0);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), btnVisualiser);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), btnVisualiser);
        scaleOut.setFromX(1.0);
        scaleOut.setFromY(1.0);
        scaleOut.setToX(0.8);
        scaleOut.setToY(0.8);

        imageContainer.setOnMouseEntered(e -> {
            overlay.setVisible(true);
            btnVisualiser.setVisible(true);
            fadeInBtn.playFromStart();
            scaleIn.playFromStart();
        });

        imageContainer.setOnMouseExited(e -> {
            overlay.setVisible(false);
            fadeOutBtn.setOnFinished(ev -> btnVisualiser.setVisible(false));
            fadeOutBtn.playFromStart();
            scaleOut.playFromStart();
        });

        Label titre = new Label("📝 " + article.getTitreArticle());
        Label categorie = new Label("📁 Catégorie : " + article.getCategorieMentalArticle());

        card.getChildren().addAll(imageContainer, titre, categorie);
        return card;
    }

    private void afficherDetailsArticle(ArticleConseil article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/DetailsArticle.fxml"));
            Parent root = loader.load();

            DetailsArticleController controller = loader.getController();
            controller.setArticleDetails(article);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'article");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
