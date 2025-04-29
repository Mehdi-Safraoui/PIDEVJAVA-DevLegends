package Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AfficherAvisFrontMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mehdi/AfficherAvisFront.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1366, 720);

        // Optional: Set minimum window size
        stage.setMinWidth(1024);
        stage.setMinHeight(600);

        stage.setScene(scene);
        stage.setTitle("Liste des Avis");
        stage.centerOnScreen();

        stage.show();
    }

}
