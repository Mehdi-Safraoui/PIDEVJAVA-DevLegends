package Controllers.maya;

import Entities.maya.Reponse;
import Services.maya.ReponseService;
import Services.maya.QuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ListReponseController {

    @FXML private TableView<Reponse> reponseTableView;
    @FXML private TableColumn<Reponse, Integer> idCol;
    @FXML private TableColumn<Reponse, String> textCol;
    @FXML private TableColumn<Reponse, Integer> scoreCol;
    @FXML private TableColumn<Reponse, String> questionTextCol; // ✅ Texte de la question
    @FXML private TableColumn<Reponse, Void> modifierCol;
    @FXML private TableColumn<Reponse, Void> supprimerCol;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> comboTri;
    @FXML private CheckBox checkDesc;

    private ObservableList<Reponse> allReponses;

    private final ReponseService reponseService = new ReponseService();
    private final QuestionService questionService = new QuestionService(); // ✅ Pour charger les textes des questions

    private ObservableList<Reponse> reponses;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("answerText"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        comboTri.setItems(FXCollections.observableArrayList("ID", "Texte", "Score", "Question"));
        comboTri.getSelectionModel().select("ID");


        // ✅ Colonne personnalisée pour afficher le texte de la question au lieu de l'ID
        questionTextCol.setCellValueFactory(cellData -> {
            int questionId = cellData.getValue().getQuestionId();
            String questionText = questionService.getQuestionTextById(questionId);
            return new javafx.beans.property.SimpleStringProperty(questionText);
        });

        addModifierButtonToTable();
        addSupprimerButtonToTable();

        loadReponses();
    }

//    public void loadReponses() {
//        reponses = FXCollections.observableArrayList(reponseService.getAll());
//        reponseTableView.setItems(reponses);
//    }

    private void addModifierButtonToTable() {
        modifierCol.setCellFactory(col -> new TableCell<>() {
            private final Button modifierBtn = new Button("Modifier");

            {
                modifierBtn.setOnAction(event -> {
                    Reponse selected = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ModifierReponse.fxml"));
                        Parent root = loader.load();

                        ModifierReponseController controller = loader.getController();
                        controller.setReponse(selected);

                        // ✅ Pour que la table se recharge après la modification
                        controller.setListReponseController(ListReponseController.this);

                        Stage stage = new Stage();
                        stage.setTitle("Modifier Réponse");
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifierBtn);
            }
        });
    }

    private void addSupprimerButtonToTable() {
        supprimerCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Supprimer");

            {
                deleteBtn.setOnAction(event -> {
                    Reponse selected = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Voulez-vous vraiment supprimer cette réponse ?");
                    alert.setContentText("ID: " + selected.getId());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            reponseService.delete(selected);
                            loadReponses();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    public void loadReponses() {
        allReponses = FXCollections.observableArrayList(reponseService.getAll());

        FilteredList<Reponse> filteredData = new FilteredList<>(allReponses, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reponse -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String filter = newValue.toLowerCase();

                // Recherche dans le texte de la réponse, le score, la question et l'ID
                return reponse.getAnswerText().toLowerCase().contains(filter)
                        || String.valueOf(reponse.getScore()).contains(filter)
                        || questionService.getQuestionTextById(reponse.getQuestionId()).toLowerCase().contains(filter)
                        || String.valueOf(reponse.getId()).contains(filter); // Recherche par ID
            });
        });

        SortedList<Reponse> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reponseTableView.comparatorProperty());

        reponseTableView.setItems(sortedData);
    }


    @FXML
    void trierReponse() {
        String critere = comboTri.getValue();
        boolean desc = checkDesc.isSelected();

        // Copier dans une nouvelle liste modifiable
        ObservableList<Reponse> sortedList = FXCollections.observableArrayList(allReponses);

        sortedList.sort((r1, r2) -> {
            int result = 0;

            switch (critere) {
                case "ID":
                    result = Integer.compare(r1.getId(), r2.getId());
                    break;
                case "Texte":
                    result = r1.getAnswerText().compareToIgnoreCase(r2.getAnswerText());
                    break;
                case "Score":
                    result = Integer.compare(r1.getScore(), r2.getScore());
                    break;
                case "Question":
                    String q1 = questionService.getQuestionTextById(r1.getQuestionId());
                    String q2 = questionService.getQuestionTextById(r2.getQuestionId());
                    result = q1.compareToIgnoreCase(q2);
                    break;
            }

            return desc ? -result : result;
        });

        reponseTableView.setItems(sortedList);
    }


}
