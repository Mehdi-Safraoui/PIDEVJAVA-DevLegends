package Controllers;

import Entities.ArticleConseil;
import Services.ArticleConseilService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;

public class AfficherArticlesController {

    @FXML
    private FlowPane cardContainer;

    private final ArticleConseilService articleService = new ArticleConseilService();

    @FXML
    public void initialize() {
        List<ArticleConseil> articles = articleService.afficher();
        for (ArticleConseil article : articles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ArticleCard.fxml"));
                StackPane card = loader.load();

                ArticleCardController controller = loader.getController();
                controller.setArticle(article);

                // Assurez-vous que l'image de l'article est affichée dans la carte
                String imagePath = article.getImage(); // Le chemin de l'image dans l'article
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageView imageView = new ImageView(new Image("file:" + imagePath));
                    imageView.setFitWidth(100); // Ajustez la taille de l'image selon vos besoins
                    imageView.setFitHeight(100);
                    controller.setImageView(imageView); // Passez l'image à votre contrôleur de carte
                }

                cardContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public void initialize() {
//        List<ArticleConseil> articles = articleService.afficher();
//        for (ArticleConseil article : articles) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ArticleCard.fxml"));
//                StackPane card = loader.load();
//
//                ArticleCardController controller = loader.getController();
//                controller.setArticle(article);
//
//                // Assurez-vous que l'image de l'article est affichée dans la carte
//                String imagePath = article.getImage(); // Le chemin de l'image dans l'article
//                if (imagePath != null && !imagePath.isEmpty()) {
//                    ImageView imageView = new ImageView(new Image("file:" + imagePath));
//                    imageView.setFitWidth(100); // Ajustez la taille de l'image selon vos besoins
//                    imageView.setFitHeight(100);
//                    controller.setImageView(imageView); // Passez l'image à votre contrôleur de carte
//                }
//
//                cardContainer.getChildren().add(card);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // Méthode pour afficher la boîte de confirmation
    private void confirmDelete(ArticleConseil article) {
        // Création de l'alerte de confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet article ?");
        alert.setContentText("L'article sera définitivement supprimé.");

        // Ajouter un bouton pour confirmer la suppression
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Afficher la boîte de dialogue
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                // Supprimer l'article si l'utilisateur confirme
                articleService.delete(article); // Implémentez la méthode 'supprimer' dans votre service
                // Rafraîchir l'affichage
                refreshArticleList();
            }
        });
    }

    // Méthode pour rafraîchir la liste des articles après la suppression
    private void refreshArticleList() {
        cardContainer.getChildren().clear(); // Vider le GridPane actuel
        initialize(); // Réinitialiser et afficher les articles à jour
    }
    private void showArticleDetails(ArticleConseil article) {
        DetailsView.display(article); // ✅ Appel direct à la méthode statique
    }

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterArticle.fxml"));
            Parent root = loader.load();
            scrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
