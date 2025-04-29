package Controllers.maya;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class QuizSelectionPageController {

    @FXML
    private Button classicQuizButton;

    @FXML
    private Button apiQuizButton;

    @FXML
    public void handleClassicQuiz() {
        // Ouvrir la page du quiz classique (par exemple, QuizController)
        loadQuizPage("/maya/Quiz.fxml");
    }

    @FXML
    public void handleApiQuiz() {
        // Ouvrir la page du quiz via l'API
//        loadQuizPage("/maya/QuizAPIHuggingFace.fxml");
        loadQuizPage("/maya/ApiQuiz.fxml");

    }

    private void loadQuizPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) classicQuizButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger la page du quiz");
            alert.showAndWait();
        }
    }
}
