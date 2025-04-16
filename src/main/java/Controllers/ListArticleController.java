package Controllers;

import Entities.ArticleConseil;
import Services.ArticleConseilService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListArticleController {

//    @FXML
//    private VBox container;
    @FXML
    private GridPane container;

    private final ArticleConseilService service = new ArticleConseilService();

    @FXML
    public void initialize() {
        afficherArticles();
    }

    private void afficherArticles() {
        container.getChildren().clear();
        List<ArticleConseil> articles = service.find();

        int row = 0;
        int col = 0;

        for (ArticleConseil a : articles) {
            VBox card = new VBox();
            card.setSpacing(10);
            card.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");

            // Image
            ImageView imageView = new ImageView();
            String imagePath = a.getImage(); // récupère le chemin de l'image

            // Vérifie si le chemin de l'image est valide
            if (imagePath != null && !imagePath.trim().isEmpty()) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    imageView.setImage(new Image("file:" + imagePath)); // Chemin absolu
                } else {
                    imageView.setImage(new Image("file:path/to/default/image.png")); // Utilise une image par défaut si introuvable
                }
            } else {
                // Si le chemin est null ou vide, utilise une image par défaut
                imageView.setImage(new Image("file:path/to/default/image.png"));
            }

            imageView.setFitHeight(120);
            imageView.setFitWidth(180);
            imageView.setPreserveRatio(true);

            // Infos
            Label titre = new Label("📝 " + a.getTitreArticle());
            Label categorie = new Label("📁 Catégorie : " + a.getCategorieMentalArticle());

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
                        service.delete(a);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierArticle.fxml"));
                    VBox root = loader.load();

                    ModifierArticleController controller = loader.getController();
                    controller.setArticleConseil(a);

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

            // Ajoute chaque carte dans le GridPane
            container.add(card, col, row);

            // Gère la position des articles dans la grille
            col++;
            if (col > 2) { // Passe à la ligne suivante après 3 éléments
                col = 0;
                row++;
            }
        }
    }


}
