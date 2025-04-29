package Controllers.mehdi;

import Entities.mehdi.Reclamation;
import Services.mehdi.EmailService;
import Services.mehdi.ReclamationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.util.Optional;

public class AfficherReclamationsController {

    @FXML private TableView<Reclamation> tableReclamations;
    @FXML private TableColumn<Reclamation, Integer> colId;
    @FXML private TableColumn<Reclamation, String> colSujet;
    @FXML private TableColumn<Reclamation, String> colContenu;
    @FXML private TableColumn<Reclamation, String> colDate;
    @FXML private TableColumn<Reclamation, String> colEmail;
    @FXML private TableColumn<Reclamation, String> colStatut;
    @FXML private TableColumn<Reclamation, Void> colActions;

    private final ReclamationService service = new ReclamationService();
    private ObservableList<Reclamation> reclamationsList;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        reclamationsList = FXCollections.observableArrayList(service.find());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujetRec"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenuRec"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateRec().toString()));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailRec"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutRec"));

        addActionsButtons();
        tableReclamations.setItems(reclamationsList);
    }

    private void addActionsButtons() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnTraiter = new Button("Traiter");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox hbox = new HBox(10, btnTraiter, btnSupprimer);

            {
                hbox.setStyle("-fx-alignment: center;");
                btnTraiter.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnTraiter.setOnAction(event -> handleTraiterAction());
                btnSupprimer.setOnAction(event -> handleSupprimerAction());
            }

            private void handleTraiterAction() {
                Reclamation rec = getTableView().getItems().get(getIndex());

                if ("Traitée".equalsIgnoreCase(rec.getStatutRec())) {
                    return;
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmer le traitement");
                alert.setHeaderText("Traiter la réclamation #" + rec.getId());
                alert.setContentText("Êtes-vous sûr de traiter cette réclamation ?");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            rec.setStatutRec("Traitée");
                            service.update(rec);
                            getTableView().refresh();

                            EmailService.sendReclamationProcessedEmail(
                                    rec.getEmailRec(),
                                    "Client",
                                    rec.getId()
                            );

                            showAlert(Alert.AlertType.INFORMATION,
                                    "Succès",
                                    "Réclamation traitée",
                                    "L'utilisateur a été notifié par email.");
                        } catch (Exception e) {
                            showAlert(Alert.AlertType.ERROR,
                                    "Erreur",
                                    "Problème d'envoi d'email",
                                    "La réclamation a été traitée mais l'email n'a pas pu être envoyé: " + e.getMessage());
                        }
                    }
                });
            }

            private void handleSupprimerAction() {
                Reclamation rec = getTableView().getItems().get(getIndex());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Supprimer la réclamation #" + rec.getId());
                alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?\nSujet: " + rec.getSujetRec());

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        service.deleteById(rec.getId());
                        reclamationsList.remove(rec);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                Reclamation rec = getTableView().getItems().get(getIndex());
                btnTraiter.setDisable("Traitée".equalsIgnoreCase(rec.getStatutRec()));
                btnTraiter.setText("Traitée".equalsIgnoreCase(rec.getStatutRec()) ? "Déjà traitée" : "Traiter");
                setGraphic(hbox);
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}