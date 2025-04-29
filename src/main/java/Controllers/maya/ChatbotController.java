//AVEC AUTRE QUE RASA
package Controllers.maya;

import Entities.maya.Chatbot;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ChatbotController {

    @FXML
    private TextArea conversationArea;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Chatbot chatbot;

    @FXML
    public void initialize() {
        chatbot = new Chatbot(); // Créer une instance du chatbot

        sendButton.setOnAction(e -> {
            String userMessage = userInput.getText();
            if (!userMessage.isEmpty()) {
                conversationArea.appendText("Tu: " + userMessage + "\n"); // Afficher le message de l'utilisateur
                String botResponse = chatbot.getResponse(userMessage); // Obtenir la réponse du chatbot
                conversationArea.appendText("Chatbot: " + botResponse + "\n"); // Afficher la réponse du chatbot
                userInput.clear(); // Effacer le champ de texte après l'envoi
            }
        });
    }
}
