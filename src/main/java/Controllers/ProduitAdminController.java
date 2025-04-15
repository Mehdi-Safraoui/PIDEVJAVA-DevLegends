package Controllers;

import Entities.Produit;
import Services.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProduitAdminController implements Initializable {

    @FXML
    private TableView<Produit> produitTable;

    @FXML
    private TableColumn<Produit, String> nomProduitColumn;

    @FXML
    private TableColumn<Produit, Integer> prixProduitColumn;

    @FXML
    private TableColumn<Produit, Integer> quantiteColumn;

    @FXML
    private TableColumn<Produit, String> statutProduitColumn;

    @FXML
    private TableColumn<Produit, Void> actionColumn;

    private ProduitService produitService = new ProduitService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des colonnes de la table
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        prixProduitColumn.setCellValueFactory(new PropertyValueFactory<>("prixProduit"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        statutProduitColumn.setCellValueFactory(new PropertyValueFactory<>("statutProduit"));

        // Initialisation de la colonne d'actions avec des boutons Modifier et Supprimer
        actionColumn.setCellFactory(param -> new TableCell<Produit, Void>() {

            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox hbox = new HBox(10, modifyButton, deleteButton);

            {
                modifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");

                modifyButton.setOnAction(e -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    handleModify(produit);
                });

                deleteButton.setOnAction(e -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    handleDelete(produit);
                });
            }

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

        loadProduits();

    }

    private void loadProduits() {
        List<Produit> produits = produitService.find();
        produitTable.getItems().setAll(produits); // Remplir la table avec les produits
    }

    private void handleModify(Produit produit) {
        if (produit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
                Parent root = loader.load();

                ModifierProduitController controller = loader.getController();
                controller.setProduit(produit);

                Stage stage = new Stage();
                stage.setTitle("Modifier Produit");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                loadProduits(); // Recharge la liste des produits après modification
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDelete(Produit produit) {
        if (produit != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Suppression");
            alert.setHeaderText("Supprimer le produit : " + produit.getNomProduit());
            alert.setContentText("Es-tu sûr de vouloir supprimer ce produit ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                produitService.delete(produit);  // Supprimer dans la base de données
                loadProduits();  // Rafraîchir la liste des produits
            }
        }
    }
}
