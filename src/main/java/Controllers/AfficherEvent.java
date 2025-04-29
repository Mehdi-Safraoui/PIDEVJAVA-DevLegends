package Controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherEvent {

    @FXML
    private Pagination pagination;

    private TableView<Evenement> evenementTable;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private ObservableList<Evenement> allEvenements;

    private final int rowsPerPage = 5;

    @FXML
    private void initialize() {
        allEvenements = FXCollections.observableArrayList(serviceEvenement.getAll());

        int pageCount = (int) Math.ceil((double) allEvenements.size() / rowsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Evenement> createPage(int pageIndex) {
        evenementTable = new TableView<>();

        TableColumn<Evenement, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        TableColumn<Evenement, String> titreColumn = new TableColumn<>("Titre");
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titreEvent"));
        titreColumn.setPrefWidth(150);

        TableColumn<Evenement, java.util.Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateEvent"));
        dateColumn.setPrefWidth(100);

        TableColumn<Evenement, String> lieuColumn = new TableColumn<>("Lieu");
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieuEvent"));
        lieuColumn.setPrefWidth(150);

        TableColumn<Evenement, String> statutColumn = new TableColumn<>("Statut");
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutEvent"));
        statutColumn.setPrefWidth(100);

        TableColumn<Evenement, Integer> formationColumn = new TableColumn<>("Formation ID");
        formationColumn.setCellValueFactory(new PropertyValueFactory<>("formationId"));
        formationColumn.setPrefWidth(100);

        // Bouton Participer
        TableColumn<Evenement, Void> participerColumn = new TableColumn<>("Participer");
        participerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button participerBtn = new Button("Participer");

            {
                participerBtn.setOnAction(event -> {
                    Evenement selectedEvent = getTableView().getItems().get(getIndex());
                    openPopup(selectedEvent);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(participerBtn);
                }
            }
        });

        evenementTable.getColumns().addAll(idColumn, titreColumn, dateColumn, lieuColumn, statutColumn, formationColumn, participerColumn);

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, allEvenements.size());
        evenementTable.setItems(FXCollections.observableArrayList(allEvenements.subList(fromIndex, toIndex)));

        return evenementTable;
    }

    private void openPopup(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ParticipationPopup.fxml"));
            AnchorPane popupPane = loader.load();

            ParticipationPopup controller = loader.getController();
            controller.setEvenement(evenement); // envoyer l'événement

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Participation à l'Événement");
            popupStage.setScene(new Scene(popupPane));
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleVoirCalendrier(ActionEvent event) {
        for (Evenement evt : allEvenements) {
            GoogleCalendarService.ajouterEvenement(evt);
        }
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Les événements ont été ajoutés à Google Calendar.");
    }
}
