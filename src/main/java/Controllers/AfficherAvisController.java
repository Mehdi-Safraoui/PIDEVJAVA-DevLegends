package Controllers;

import Entities.Avis;
import Services.AvisService;
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

public class AfficherAvisController {

    @FXML
    private TableView<Avis> tableAvis;
    @FXML
    private TableColumn<Avis, Integer> colId;
    @FXML
    private TableColumn<Avis, String> colSujet;
    @FXML
    private TableColumn<Avis, String> colContenu;
    @FXML
    private TableColumn<Avis, String> colDate;
    @FXML
    private TableColumn<Avis, String> colEmail;
    @FXML
    private TableColumn<Avis, Integer> colNote;
    @FXML
    private TableColumn<Avis, String> colStatut;
    @FXML
    private TableColumn<Avis, Void> colActions;

    private final AvisService service = new AvisService();
    private ObservableList<Avis> avisList;

    @FXML
    public void initialize() {
        loadData();
        this.getClass().getResource("/AvisRecStyle.css").toExternalForm();

    }

    private void loadData() {
        avisList = FXCollections.observableArrayList(service.find());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujetAvis"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenuAvis"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateAvis().toString()));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailAvis"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("noteAvis"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutAvis"));

        // Ajouter boutons
        addActionsButtons();

        tableAvis.setItems(avisList);
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
                    Avis avis = getTableView().getItems().get(getIndex());
                    try {
                        // Charger le FXML de la fenêtre de modification
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAvis.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur du FXML
                        ModifierAvisController controller = loader.getController();

                        // Passer l'avis sélectionné au contrôleur
                        controller.setAvis(avis);

                        // Créer une nouvelle scène et l'afficher dans une fenêtre modale
                        Stage stage = new Stage();
                        stage.setTitle("Modifier Avis");
                        stage.setScene(new Scene(root));
                        stage.initOwner(tableAvis.getScene().getWindow()); // Définir la fenêtre principale comme propriétaire
                        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Fenêtre modale
                        stage.showAndWait(); // Attendre que l'utilisateur ferme la fenêtre

                        // 🔁 Rafraîchir la liste après modification
                        loadData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                btnSupprimer.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());
                    service.deleteById(avis.getId());
                    loadData(); // Rafraîchir la liste
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
