package Controllers.maya;

import Entities.maya.ArticleConseil;
import Services.maya.ArticleConseilService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListArticleController {

    @FXML
    private GridPane container;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> comboTri;
    @FXML
    private CheckBox checkDesc;

    private final ArticleConseilService service = new ArticleConseilService();
    private ObservableList<ArticleConseil> allArticles;

    @FXML
    public void initialize() {
        afficherArticles();
        comboTri.setItems(FXCollections.observableArrayList("ID", "Titre", "Catégorie"));
        comboTri.getSelectionModel().select("ID");

        // Ajoute un listener pour le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAndSortArticles(newValue);
        });

        // Ajouter un listener pour le changement du critère de tri
        comboTri.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterAndSortArticles(searchField.getText());
        });

        // Ajouter un listener pour la case à cocher du tri décroissant
        checkDesc.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filterAndSortArticles(searchField.getText());
        });
    }

    private void afficherArticles() {
        allArticles = FXCollections.observableArrayList(service.find());
        filterAndSortArticles(null); // Applique immédiatement les filtres et tris
    }

    private void filterAndSortArticles(String searchText) {
        // Crée une liste filtrée
        FilteredList<ArticleConseil> filteredData = new FilteredList<>(allArticles, article -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lowerCaseSearch = searchText.toLowerCase();
            return article.getTitreArticle().toLowerCase().contains(lowerCaseSearch) ||
                    article.getCategorieMentalArticle().toLowerCase().contains(lowerCaseSearch) ||
                    String.valueOf(article.getId()).contains(lowerCaseSearch);
        });

        // Récupère le critère de tri sélectionné et la direction de tri
        String selectedSortCriterion = comboTri.getValue();
        if (selectedSortCriterion == null) {
            selectedSortCriterion = "ID";  // Valeur par défaut si aucun critère n'est sélectionné
        }

        final boolean descending = checkDesc.isSelected();  // Déclare cette variable comme finale pour l'utiliser dans la lambda

        // Trie les articles selon le critère choisi
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

        // Met à jour l'affichage
        container.getChildren().clear();
        int row = 0, col = 0;
        for (ArticleConseil article : sortedData) {
            VBox card = createArticleCard(article);
            container.add(card, col, row);
            col++;
            if (col > 2) { // Passe à la ligne suivante après 3 éléments
                col = 0;
                row++;
            }
        }
    }




    private VBox createArticleCard(ArticleConseil article) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Image
        ImageView imageView = new ImageView();
        String imagePath = article.getImage();
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                imageView.setImage(new Image("file:" + imagePath)); // Chemin absolu
            } else {
                imageView.setImage(new Image("file:path/to/default/image.png")); // Image par défaut
            }
        } else {
            imageView.setImage(new Image("file:path/to/default/image.png")); // Image par défaut
        }

        imageView.setFitHeight(120);
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);

        // Infos
        Label titre = new Label("📝 " + article.getTitreArticle());
        Label categorie = new Label("📁 Catégorie : " + article.getCategorieMentalArticle());

        // Boutons
        HBox btnBox = new HBox(10);
        Button btnSupprimer = new Button("🗑 Supprimer");
        Button btnModifier = new Button("✏ Modifier");

        btnSupprimer.setOnAction(e -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet article ?");
            confirmationAlert.setContentText("Cette action est irréversible.");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    service.delete(article);
                    afficherArticles(); // Rafraîchir après suppression

                    // ✅ Message de succès
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("L'article a été supprimé avec succès.");
                    successAlert.showAndWait();
                }
            });
        });


        btnModifier.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ModifierArticle.fxml"));
                VBox root = loader.load();

                ModifierArticleController controller = loader.getController();
                controller.setArticleConseil(article);

                Stage stage = new Stage();
                stage.setTitle("Modifier Article");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnBox.getChildren().addAll(btnModifier, btnSupprimer);
        card.getChildren().addAll(imageView, titre, categorie, btnBox);

        return card;
    }
}
