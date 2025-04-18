package Controllers.maya;

import Entities.maya.Consultation;
import Services.maya.ConsultationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class AfficherConsultationController {

    @FXML private TableView<Consultation> tableConsultations;
    @FXML private TableColumn<Consultation, java.util.Date> colDate;
    @FXML private TableColumn<Consultation, String> colLien;
    @FXML private TableColumn<Consultation, String> colNotes;
    @FXML private TableColumn<Consultation, String> colNom;
    @FXML private TableColumn<Consultation, String> colPrenom;
    @FXML private TableColumn<Consultation, Integer> colAge;
    @FXML private TableColumn<Consultation, Void> colModifier;
    @FXML private TableColumn<Consultation, Void> colSupprimer;

    private final ConsultationService consultationService = new ConsultationService();

    @FXML
    public void initialize() {

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCons"));
        colLien.setCellValueFactory(new PropertyValueFactory<>("lienVisioCons"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notesCons"));

        loadData();
        addModifierButtonToTable();
        addSupprimerButtonToTable();
    }

    private void loadData() {
        List<Consultation> consultations = consultationService.find();
        ObservableList<Consultation> observableList = FXCollections.observableArrayList(consultations);
        tableConsultations.setItems(observableList);
    }

    public void refreshTable() {
        tableConsultations.getItems().clear();
        loadData();
    }

    private void addModifierButtonToTable() {
        colModifier.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(e -> {
                    Consultation selected = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ModifierConsultation.fxml"));
                        Parent root = loader.load();

                        ModifierConsultationController controller = loader.getController();
                        controller.setConsultation(selected);

                        Stage stage = new Stage();
                        stage.setTitle("Modifier Consultation");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                        refreshTable();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    btn.getStyleClass().add("button-modifier");
                }
            }
        });
    }

    private void addSupprimerButtonToTable() {
        colSupprimer.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(e -> {
                    Consultation selected = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText(null);
                    alert.setContentText("Voulez-vous vraiment supprimer cette consultation ?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            consultationService.delete(selected);
                            refreshTable();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    btn.getStyleClass().add("button-supprimer");
                }
            }
        });
    }

    @FXML
    void retourAjouter(ActionEvent event) {
        try {
            // Charger la fenêtre d'ajout (AjouterConsultation.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/AjouterConsultation.fxml"));

            Parent root = loader.load(); // Charger le fichier FXML

            // Définir la nouvelle scène pour le stage
            Stage stage = (Stage) tableConsultations.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Afficher un message d'erreur détaillé
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Chargement");
            alert.setHeaderText("Erreur lors du chargement de la scène");
            alert.setContentText("Une erreur est survenue : " + e.getMessage());
            alert.showAndWait();
        }
    }



}
