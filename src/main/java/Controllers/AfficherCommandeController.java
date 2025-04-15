package Controllers;

import Entities.Commande;
import Services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherCommandeController implements Initializable {

    @FXML
    private TableView<Commande> commandeTable;

    @FXML
    private TableColumn<Commande, String> nomClientColumn;

    @FXML
    private TableColumn<Commande, String> adresseEmailColumn;

    @FXML
    private TableColumn<Commande, Double> totalColumn;

    @FXML
    private TableColumn<Commande, Void> actionColumn;

    private CommandeService commandeService = new CommandeService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomClientColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nomClient"));
        adresseEmailColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("adresseEmail"));
        totalColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("totalCom"));

        // Supprimer uniquement
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
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });

        loadCommandes();
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

        Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            commandeService.delete(commande);
            loadCommandes();  // Rafraîchir la liste
        }
    }
}
