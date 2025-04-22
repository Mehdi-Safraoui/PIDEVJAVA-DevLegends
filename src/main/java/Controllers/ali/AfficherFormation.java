package Controllers.ali;

import Entities.ali.Formation;
import Services.ali.ServiceFormation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.sql.Date;

public class AfficherFormation {

    @FXML
    private TableView<Formation> tableView;
    @FXML
    private TableColumn<Formation, Integer> idColumn;
    @FXML
    private TableColumn<Formation, String> titreColumn;
    @FXML
    private TableColumn<Formation, Date> dateColumn;
    @FXML
    private TableColumn<Formation, String> lieuColumn;
    @FXML
    private TableColumn<Formation, String> statutColumn;

    private final ServiceFormation formationService = new ServiceFormation();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titreFor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFor"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieuFor"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutFor"));


        // Charger les données
        List<Formation> formations = formationService.getAll();
        tableView.getItems().setAll(formations);
    }
}
