package Controllers.maya;

import Entities.maya.ArticleConseil;
import Services.maya.ArticleConseilService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class ModifierArticleController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField contenuField;
    @FXML
    private TextField categorieField;
    @FXML
    private ImageView imageView;
    @FXML
    private Button chooseImageButton;

    private ArticleConseil articleConseil;
    private final ArticleConseilService service = new ArticleConseilService();

    public void setArticleConseil(ArticleConseil articleConseil) {
        this.articleConseil = articleConseil;

        // Remplir les champs avec les informations de l'article
        titreField.setText(articleConseil.getTitreArticle());
        contenuField.setText(articleConseil.getContenuArticle());
        categorieField.setText(articleConseil.getCategorieMentalArticle());

        // Afficher l'image dans l'ImageView en utilisant un chemin correct
        String imagePath = articleConseil.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            imageView.setImage(new javafx.scene.image.Image("file:///" + imagePath));
        }
    }

    // Méthode pour enregistrer l'article modifié
    @FXML
    private void saveArticle() {
        String titre = titreField.getText().trim();
        String contenu = contenuField.getText().trim();
        String categorie = categorieField.getText().trim();

        // Contrôles de saisie
        if (titre.isEmpty() || titre.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Titre invalide", "Le titre doit contenir au moins 5 caractères.");
            return;
        }

//        // Vérifier si le titre est unique
//        if (!service.isTitreUnique(titre)) {
//            showAlert(Alert.AlertType.ERROR, "Titre existant", "Un article avec ce titre existe déjà. Choisissez un autre titre.");
//            return;
//        }

        if (contenu.isEmpty() || contenu.length() < 10) {
            showAlert(Alert.AlertType.ERROR, "Contenu insuffisant", "Le contenu doit contenir au moins 10 caractères.");
            return;
        }

        if (categorie.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Catégorie manquante", "Veuillez spécifier une catégorie.");
            return;
        }

        // Mise à jour de l'article avec les nouvelles données
        articleConseil.setTitreArticle(titre);
        articleConseil.setContenuArticle(contenu);
        articleConseil.setCategorieMentalArticle(categorie);

        // Sauvegarde de l'article modifié
        service.update(articleConseil);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Article modifié avec succès !");
        closeWindow();
    }

    // Méthode pour annuler la modification et fermer la fenêtre
    @FXML
    private void cancelModification() {
        // Affichage de la boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annuler");
        alert.setHeaderText("Êtes-vous sûr de vouloir annuler ?");
        alert.setContentText("Les modifications non enregistrées seront perdues.");

        // Attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur clique sur "OK", fermer la fenêtre sans enregistrer
                closeWindow();
            }
        });
    }

    // Méthode pour fermer la fenêtre actuelle
    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    // Méthode pour choisir l'image
    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Utilisation du chemin absolu du fichier
            String filePath = selectedFile.getAbsolutePath();

            // Affiche l'image dans l'ImageView en utilisant un chemin correct
            imageView.setImage(new javafx.scene.image.Image("file:///" + filePath));

            // Sauvegarde le chemin de l'image dans l'articleConseil pour un enregistrement ultérieur
            articleConseil.setImage(filePath);
        }
    }

    // Méthode pour afficher les alertes
    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }
}
