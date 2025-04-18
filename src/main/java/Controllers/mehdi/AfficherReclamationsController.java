package Controllers.mehdi;

import Entities.mehdi.Reclamation;
import Services.mehdi.ReclamationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherReclamationsController {

    @FXML
    private TableView<Reclamation> tableReclamations;
    @FXML
    private TableColumn<Reclamation, Integer> colId;
    @FXML
    private TableColumn<Reclamation, String> colSujet;
    @FXML
    private TableColumn<Reclamation, String> colContenu;
    @FXML
    private TableColumn<Reclamation, String> colDate;
    @FXML
    private TableColumn<Reclamation, String> colEmail;
    @FXML
    private TableColumn<Reclamation, String> colStatut;
    @FXML
    private TableColumn<Reclamation, Void> colActions;

    private final ReclamationService service = new ReclamationService();
    private ObservableList<Reclamation> reclamationsList;

    @FXML
    public void initialize() {
        loadData();
        this.getClass().getResource("/AvisRecStyle.css").toExternalForm();

    }

    private void loadData() {
        reclamationsList = FXCollections.observableArrayList(service.find());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujetRec"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenuRec"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateRec().toString()));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailRec"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutRec"));

        // Ajouter boutons
        addActionsButtons();

        tableReclamations.setItems(reclamationsList);
    }

    private void addActionsButtons() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox hbox = new HBox(10, btnModifier, btnSupprimer);

            {
                hbox.setStyle("-fx-alignment: center;");
                btnModifier.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    Reclamation rec = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mehdi/ModifierReclamation.fxml"));
                        Parent root = loader.load();

                        ModifierReclamationController controller = loader.getController();
                        controller.setReclamation(rec); // on lui envoie l’objet à modifier

                        Stage stage = new Stage();
                        stage.setTitle("Modifier Réclamation");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                        loadData(); // rafraîchir la liste après modification
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                btnSupprimer.setOnAction(event -> {
                    Reclamation rec = getTableView().getItems().get(getIndex());
                    service.deleteById(rec.getId());
                    loadData();
                    System.out.println("🗑️ Supprimé: " + rec);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }


}
