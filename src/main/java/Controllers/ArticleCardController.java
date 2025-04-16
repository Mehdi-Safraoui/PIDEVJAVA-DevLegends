package Controllers;

import Entities.ArticleConseil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;

public class ArticleCardController {

    @FXML private ImageView articleImage;
    @FXML private Label titleLabel;
    @FXML private VBox overlay;
    @FXML private StackPane card;

    private ArticleConseil article;
    public void setArticle(ArticleConseil article) {
        this.article = article;
        titleLabel.setText(article.getTitreArticle());

        String imagePath = article.getImage();
        Image img;

        try {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                img = new Image(imagePath);
            } else {
                // Assurez-vous que l'image est dans le dossier resources/images
                File file = new File(getClass().getResource("/images/" + imagePath).toURI());
                if (file.exists()) {
                    img = new Image(file.toURI().toString());
                } else {
                    System.out.println("Image not found: " + imagePath); // Affichage dans la console si l'image est introuvable
                    img = new Image(getClass().getResource("/images/default.png").toExternalForm());
                }
            }
            articleImage.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
            articleImage.setImage(new Image(getClass().getResource("/images/default.png").toExternalForm()));
        }

        card.setOnMouseEntered(e -> overlay.setVisible(true));
        card.setOnMouseExited(e -> overlay.setVisible(false));
    }

//
//    public void setArticle(ArticleConseil article) {
//        this.article = article;
//        titleLabel.setText(article.getTitreArticle());
//
//        String imagePath = article.getImage();
//        Image img;
//
//        try {
//            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
//                img = new Image(imagePath);
//            } else {
//                File file = new File(imagePath);
//                if (file.exists()) {
//                    img = new Image(file.toURI().toString());
//                } else {
//                    img = new Image(getClass().getResource("/images/default.png").toExternalForm());
//                }
//            }
//            articleImage.setImage(img);
//        } catch (Exception e) {
//            e.printStackTrace();
//            articleImage.setImage(new Image(getClass().getResource("/images/default.png").toExternalForm()));
//        }
//
//        card.setOnMouseEntered(e -> overlay.setVisible(true));
//        card.setOnMouseExited(e -> overlay.setVisible(false));
//    }

    @FXML
    public void handleViewDetails() {
        DetailsView.display(article);
    }
    // Ajout de la méthode setImageView
    public void setImageView(ImageView imageView) {
        this.articleImage = imageView;  // Met à jour l'ImageView avec celui passé en paramètre
    }
}
