package Controllers.maya;

import Entities.maya.ArticleConseil;
import Services.maya.ArticleConseilService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

public class AjouterArticleController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea contenuArea;

    @FXML
    private ComboBox<String> categorieCombo;

    @FXML
    private Label imageLabel;

    private String selectedImageName = null;

    private final ArticleConseilService service = new ArticleConseilService();

    @FXML
    public void initialize() {
        categorieCombo.getItems().addAll(Arrays.asList(
                "Anxiété", "Dépression", "Bonheur", "Motivation", "Stress"
        ));
    }

    @FXML
    void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImageName = selectedFile.getName();
            imageLabel.setText(selectedImageName);

            Path destination = Paths.get("src/main/resources/images/" + selectedImageName);
            try {
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Image copiée avec succès : " + selectedImageName);
            } catch (IOException e) {
                System.out.println("❌ Erreur lors de la copie de l'image : " + selectedFile.getAbsolutePath());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur image", "Impossible de copier l’image dans le dossier 'images'.");
            }
        } else {
            imageLabel.setText("Aucune image sélectionnée");
        }
    }

    @FXML
    void ajouterArticle(ActionEvent event) {
        String titre = titreField.getText().trim();
        String contenu = contenuArea.getText().trim();
        String categorie = categorieCombo.getValue();

        // Contrôles de saisie
        if (titre.isEmpty() || titre.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Titre invalide", "Le titre doit contenir au moins 5 caractères.");
            return;
        }

//        if (!service.isTitreUnique(titre)) {
//            showAlert(Alert.AlertType.ERROR, "Titre existant", "Un article avec ce titre existe déjà. Choisissez un autre titre.");
//            return;
//        }

        if (contenu.isEmpty() || contenu.length() < 10) {
            showAlert(Alert.AlertType.ERROR, "Contenu insuffisant", "Le contenu doit contenir au moins 10 caractères.");
            return;
        }

        if (categorie == null) {
            showAlert(Alert.AlertType.ERROR, "Catégorie manquante", "Veuillez choisir une catégorie.");
            return;
        }

        if (selectedImageName == null) {
            showAlert(Alert.AlertType.ERROR, "Image manquante", "Veuillez choisir une image.");
            return;
        }

        String imagePath = "images/" + selectedImageName;
        ArticleConseil article = new ArticleConseil(titre, contenu, categorie, imagePath);
        service.add(article);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Article ajouté avec succès !");

        titreField.clear();
        contenuArea.clear();
        categorieCombo.getSelectionModel().clearSelection();
        imageLabel.setText("Aucune image sélectionnée");
        selectedImageName = null;
    }

    @FXML
    private void goToAfficherArticles(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/AfficherArticles.fxml"));
            Parent root = loader.load();
            titreField.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }
}