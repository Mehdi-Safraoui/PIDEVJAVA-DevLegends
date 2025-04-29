package Controllers.maya;

import Entities.maya.ArticleConseil;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class DetailsView {

    @FXML
    private ImageView imageView;

    public static void display(ArticleConseil article) {
        Stage window = new Stage();
        window.setTitle("Détails de l'article");

        // Chargement de l'image depuis le dossier resources/images
        InputStream imageStream = DetailsView.class.getResourceAsStream("/images/" + article.getImage());
        if (imageStream == null) {
            System.out.println("❌ Image non trouvée : " + article.getImage());
            // Image par défaut si l'image n'existe pas
            imageStream = DetailsView.class.getResourceAsStream("/images/default.png");
        }

        ImageView imageView = new ImageView(new Image(imageStream));
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        // Labels des données de l'article
        Label title = new Label("📝 Titre : " + article.getTitreArticle());
        Label cat = new Label("📁 Catégorie : " + article.getCategorieMentalArticle());
        Label desc = new Label("📄 Contenu :\n" + article.getContenuArticle());

        VBox layout = new VBox(15, imageView, title, cat, desc);
        layout.setStyle("-fx-padding: 20; -fx-font-size: 14; -fx-background-color: #f8f8f8;");

        Scene scene = new Scene(layout, 420, 550);
        window.setScene(scene);
        window.show();
    }
    @FXML
    private void handleRetour() {
        // Code pour revenir à la liste des articles
        // Exemple : Retourner à la scène précédente
        Stage stage = (Stage) imageView.getScene().getWindow();
        stage.close();  // Vous pouvez choisir une autre façon de revenir à la liste si nécessaire
    }
}