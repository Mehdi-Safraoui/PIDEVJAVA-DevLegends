package Controllers.fatma;

import Entities.fatma.Commande;
import Services.fatma.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherCommandeController implements Initializable {

    @FXML private TableView<Commande> commandeTable;
    @FXML private TableColumn<Commande, Integer> idColumn;
    @FXML private TableColumn<Commande, String> nomClientColumn;
    @FXML private TableColumn<Commande, String> adresseEmailColumn;
    @FXML private TableColumn<Commande, Date> dateCommandeColumn;
    @FXML private TableColumn<Commande, String> adresseColumn;
    @FXML private TableColumn<Commande, Double> totalColumn;
    @FXML private TableColumn<Commande, String> paysColumn;
    @FXML private TableColumn<Commande, Integer> numTelephoneColumn;
    @FXML private TableColumn<Commande, Void> actionColumn;
    @FXML private TextField searchField;

    private final CommandeService commandeService = new CommandeService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        nomClientColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nomClient"));
        adresseEmailColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("adresseEmail"));
        dateCommandeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateCommande"));
        adresseColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("adresse"));
        totalColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("totalCom"));
        paysColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("pays"));
        numTelephoneColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numTelephone"));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> handleDelete(getTableView().getItems().get(getIndex())));
            }

            private final HBox hbox = new HBox(10, deleteButton);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        loadCommandes();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadCommandes();
            } else {
                List<Commande> commandes = commandeService.findByNomClient(newValue);
                commandeTable.getItems().setAll(commandes);
            }
        });
    }

    private void loadCommandes() {
        List<Commande> commandes = commandeService.find();
        commandeTable.getItems().setAll(commandes);
    }

    private void handleDelete(Commande commande) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de commande");
        alert.setHeaderText("Supprimer la commande de : " + commande.getNomClient());
        alert.setContentText("Es-tu sûr de vouloir supprimer cette commande ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            commandeService.delete(commande);
            loadCommandes();
        }
    }
}
