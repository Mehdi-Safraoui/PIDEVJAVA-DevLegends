package Controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AfficherEvent {

    @FXML
    private TableView<Evenement> evenementTable;

    @FXML
    private TableColumn<Evenement, Integer> idColumn;

    @FXML
    private TableColumn<Evenement, String> titreColumn;

    @FXML
    private TableColumn<Evenement, java.util.Date> dateColumn;

    @FXML
    private TableColumn<Evenement, String> lieuColumn;

    @FXML
    private TableColumn<Evenement, String> statutColumn;

    @FXML
    private TableColumn<Evenement, Integer> formationColumn;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    private void initialize() {
        this.getClass().getResource("/style.css").toExternalForm();
        // Initialisation des colonnes avec les noms des attributs
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titreEvent"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateEvent"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieuEvent"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutEvent"));
        formationColumn.setCellValueFactory(new PropertyValueFactory<>("formationId"));

        // Charger les données depuis le service
        ObservableList<Evenement> evenements = FXCollections.observableArrayList(serviceEvenement.getAll());
        evenementTable.setItems(evenements);
    }
}
