package Controllers.maya;

import Entities.maya.ArticleConseil;
import Services.maya.TranslationService;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DetailsArticleController {

    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private TextArea contentText;

    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private Button translateButton;
    @FXML
    private Button backToFrenchButton;
    @FXML
    private VBox rootVBox; // Ton VBox principal

    private String originalText;
    private Label translationStatusLabel = new Label();

    public void setArticleDetails(ArticleConseil article) {
        titleLabel.setText(article.getTitreArticle());
        categoryLabel.setText("Catégorie : " + article.getCategorieMentalArticle());
        contentText.setText(article.getContenuArticle());

        String imagePath = article.getImage();
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imagePath = "resources/images/default.png"; // Image par défaut
        }
        imageView.setImage(new Image("file:" + imagePath));

        // Important : sauvegarder le texte original une fois ici
        originalText = article.getContenuArticle();
    }

    @FXML
    private void initialize() {
        translationStatusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        translationStatusLabel.setVisible(false);
        rootVBox.getChildren().add(translationStatusLabel);

        languageChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Français".equals(newVal)) {
                backToFrenchButton.setVisible(false);
            } else {
                backToFrenchButton.setVisible(true);
            }
        });

        backToFrenchButton.setVisible(false);
    }

    @FXML
    private void onTranslateButtonClick() {
        String selectedLanguage = languageChoiceBox.getValue();

        if (selectedLanguage == null || selectedLanguage.isEmpty()) {
            showAlert("Veuillez choisir une langue avant de traduire !");
            return;
        }

        if ("Français".equals(selectedLanguage)) {
            onBackToFrenchButtonClick();
            return;
        }

        String targetLangCode = switch (selectedLanguage) {
            case "Anglais" -> "en";
            case "Espagnol" -> "es";
            case "Arabe" -> "ar";
            case "Italien" -> "it";
            case "Allemand" -> "de";
            default -> "fr";
        };

        translateWithLoading(targetLangCode);
    }

    @FXML
    private void onBackToFrenchButtonClick() {
        animateTextChange(originalText);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void animateTextChange(String newText) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentText);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            contentText.setText(newText);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentText);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private void translateWithLoading(String targetLangCode) {
        translateButton.setDisable(true);
        backToFrenchButton.setDisable(true);

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        rootVBox.getChildren().add(loadingIndicator);

        Task<String> translationTask = new Task<>() {
            @Override
            protected String call() {
                return TranslationService.translate(contentText.getText(), "fr", targetLangCode);
            }
        };

        translationTask.setOnSucceeded(e -> {
            rootVBox.getChildren().remove(loadingIndicator);
            translateButton.setDisable(false);
            backToFrenchButton.setDisable(false);

            animateTextChange(translationTask.getValue());

            showTemporaryMessage("Traduction terminée !");
        });

        translationTask.setOnFailed(e -> {
            rootVBox.getChildren().remove(loadingIndicator);
            translateButton.setDisable(false);
            backToFrenchButton.setDisable(false);

            showAlert("Erreur lors de la traduction !");
        });

        Thread thread = new Thread(translationTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void showTemporaryMessage(String message) {
        translationStatusLabel.setText(message);
        translationStatusLabel.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), translationStatusLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), translationStatusLabel);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> translationStatusLabel.setVisible(false));
            fadeOut.play();
        });
        pause.play();
    }
}
