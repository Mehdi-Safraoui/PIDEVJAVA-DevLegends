package Services.maya;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


public class ChatbotUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML contenant l'interface utilisateur
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatbotUI.fxml"));
        Scene scene = new Scene(loader.load(), 400, 400);
        primaryStage.setTitle("Chatbot Santé Mentale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

