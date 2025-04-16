package Controllers;

import Entities.ArticleConseil;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class DetailsView {

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
}
