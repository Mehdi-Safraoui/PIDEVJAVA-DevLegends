package Controllers.maya;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import rasa.RasaClient;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.application.HostServices;

public class ChatController {

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private VBox chatArea;

    private HostServices hostServices; // Variable pour les HostServices

    // Setter pour injecter HostServices dans ce contrôleur
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    // Méthode pour gérer le clic sur le Hyperlink (ouvre l'URL dans le navigateur)
    @FXML
    public void handleHyperlink(ActionEvent event) {
        String url = ((Hyperlink) event.getSource()).getText(); // Récupère l'URL du texte du lien
        if (hostServices != null) {
            hostServices.showDocument(url); // Ouvre l'URL dans le navigateur par défaut
        }
    }

    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> handleSend());
    }

    private void handleSend() {
        String userMessage = userInput.getText();
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return;
        }

        addUserMessage(userMessage);

        String rasaResponse = RasaClient.sendMessageToRasa(userMessage);

        parseAndDisplayBotResponse(rasaResponse);

        userInput.clear();
    }

    private void addUserMessage(String message) {
        HBox userBox = new HBox();
        userBox.getStyleClass().add("user-message");

        Text text = new Text(message);
        TextFlow flow = new TextFlow(text);
        userBox.getChildren().add(flow);

        chatArea.getChildren().add(userBox);
    }

    private void parseAndDisplayBotResponse(String jsonResponse) {
        try {
            JSONArray array = new JSONArray(jsonResponse);
            for (int i = 0; i < array.length(); i++) {
                JSONObject msgObj = array.getJSONObject(i);

                HBox botBox = new HBox();
                botBox.getStyleClass().add("bot-message");

                VBox contentBox = new VBox(5);

                if (msgObj.has("text")) {
                    Text text = new Text(msgObj.getString("text"));
                    contentBox.getChildren().add(new TextFlow(text));
                }

                if (msgObj.has("image")) {
                    ImageView imageView = new ImageView(new Image(msgObj.getString("image")));
                    imageView.setFitWidth(200);
                    imageView.setPreserveRatio(true);
                    contentBox.getChildren().add(imageView);
                }

                if (msgObj.has("buttons")) {
                    JSONArray buttons = msgObj.getJSONArray("buttons");
                    for (int j = 0; j < buttons.length(); j++) {
                        JSONObject button = buttons.getJSONObject(j);
                        String title = button.getString("title");
                        String payload = button.getString("payload");

                        Hyperlink link = new Hyperlink(title);
                        link.setOnAction(e -> handleHyperlink(new ActionEvent(link, e.getTarget()))); // Corrected action handler
                        contentBox.getChildren().add(link);
                    }
                }

                botBox.getChildren().add(contentBox);
                chatArea.getChildren().add(botBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
            addBotMessage("Erreur de lecture de la réponse !");
        }
    }

    private void addBotMessage(String message) {
        HBox botBox = new HBox();
        botBox.getStyleClass().add("bot-message");

        Text text = new Text(message);
        TextFlow flow = new TextFlow(text);
        botBox.getChildren().add(flow);

        chatArea.getChildren().add(botBox);
    }
}
