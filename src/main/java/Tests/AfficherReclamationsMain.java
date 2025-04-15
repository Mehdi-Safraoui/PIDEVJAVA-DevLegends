package Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AfficherReclamationsMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReclamations.fxml"));
        Parent root = loader.load();

        // Create scene with specified dimensions
        Scene scene = new Scene(root, 1366, 720);

        // Optional: Set minimum window size
        stage.setMinWidth(1024);
        stage.setMinHeight(600);

        stage.setScene(scene);
        stage.setTitle("Liste des Réclamations");

        // Optional: Center the window on screen
        stage.centerOnScreen();

        stage.show();
    }

}
