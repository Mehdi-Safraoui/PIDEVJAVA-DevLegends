package Controllers.maya;

import Entities.maya.Question;
import Services.maya.QuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListQuestionController implements Initializable {

    @FXML
    private TableView<Question> questionTable;

    @FXML
    private TableColumn<Question, Integer> idColumn;

    @FXML
    private TableColumn<Question, String> questionTextColumn;

    @FXML
    private TableColumn<Question, String> answerTypeColumn;

    @FXML
    private TableColumn<Question, Integer> pointsColumn;

    @FXML
    private TableColumn<Question, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    private final QuestionService questionService = new QuestionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionTextColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        answerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("answerType"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        // Cell factory pour la colonne Actions
        actionsColumn.setCellFactory(new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {
            @Override
            public TableCell<Question, Void> call(TableColumn<Question, Void> param) {
                return new TableCell<Question, Void>() {
                    private final Button editButton = new Button("Modifier");
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        // Gestion des actions de chaque bouton
                        editButton.setOnAction(event -> {
                            Question selectedQuestion = getTableView().getItems().get(getIndex());
                            handleEditQuestion(selectedQuestion);
                        });

                        deleteButton.setOnAction(event -> {
                            Question selectedQuestion = getTableView().getItems().get(getIndex());
                            handleDeleteQuestion(selectedQuestion);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Créer une HBox pour contenir les boutons
                            HBox hbox = new HBox(10);
                            hbox.getChildren().addAll(editButton, deleteButton);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        // Charger les questions dans la table
        loadQuestions();
    }

    private void loadQuestions() {
        List<Question> questions = questionService.findAll(); // Remplacer `read()` par `findAll()`
        ObservableList<Question> observableList = FXCollections.observableArrayList(questions);
        questionTable.setItems(observableList);
    }

    @FXML
    private void handleDeleteQuestion(Question question) {
        if (question != null) {
            // Création d'une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette question ?");
            confirmationAlert.setContentText("Cette action est irréversible.");

            // Attente de la réponse de l'utilisateur
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Suppression de la question si l'utilisateur confirme
                    questionService.delete(question);
                    questionTable.getItems().remove(question); // Mise à jour de la TableView
                    statusLabel.setText("Question supprimée avec succès !");
                } else {
                    statusLabel.setText("Suppression annulée.");
                }
            });
        } else {
            statusLabel.setText("Veuillez sélectionner une question à supprimer.");
        }
    }

    @FXML
    private void handleEditQuestion(Question question) {
        if (question != null) {
            // Ouvrir une nouvelle fenêtre avec le formulaire de modification
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ModifierQuestion.fxml"));
                Parent root = loader.load();

                // Passer la question sélectionnée au contrôleur de la fenêtre ModifierQuestion
                ModifierQuestionController controller = loader.getController();
                controller.setQuestion(question);

                // Créer une nouvelle scène et une nouvelle fenêtre
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier la Question");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("Veuillez sélectionner une question à modifier.");
        }
    }
    public void refreshTable() {
        // Recharger les questions
        loadQuestions(); // Vous avez déjà cette méthode qui charge les questions
    }

}
