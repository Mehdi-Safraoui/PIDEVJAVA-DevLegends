package Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFXm extends Application {

    private Stage primaryStage;
    private Scene centreScene;
    private Scene packScene;
    private Scene contratScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Charger la scène des centres
        Parent centreRoot = FXMLLoader.load(getClass().getResource("/AfficherCentre.fxml"));
        centreScene = new Scene(centreRoot, 900, 650);

        // Charger la scène des packs
        Parent packRoot = FXMLLoader.load(getClass().getResource("/AfficherPack.fxml"));
        packScene = new Scene(packRoot, 900, 650);

        // Charger la scène des contrats
        Parent contratRoot = FXMLLoader.load(getClass().getResource("/AfficherContrat.fxml"));
        contratScene = new Scene(contratRoot, 900, 650);

        // Configuration de la fenêtre principale
        primaryStage.setTitle("Gestion des Centres de Formation");
        primaryStage.setScene(centreScene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    // Méthodes pour changer de scène
    public void showCentres() {
        primaryStage.setTitle("Gestion des Centres");
        primaryStage.setScene(centreScene);
    }

    public void showPacks() {
        primaryStage.setTitle("Gestion des Packs");
        primaryStage.setScene(packScene);
    }

    public void showContrats() {
        primaryStage.setTitle("Gestion des Contrats");
        primaryStage.setScene(contratScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}