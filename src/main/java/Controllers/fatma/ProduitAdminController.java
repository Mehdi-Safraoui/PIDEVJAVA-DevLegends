package Controllers.fatma;

import Entities.fatma.Produit;
import Services.fatma.ProduitService;
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
import javafx.scene.paint.Color;

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
    @FXML
    private ComboBox<String> categorieComboBox;

    private final ProduitService produitService = new ProduitService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        prixProduitColumn.setCellValueFactory(new PropertyValueFactory<>("prixProduit"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        statutProduitColumn.setCellValueFactory(new PropertyValueFactory<>("statutProduit"));

        statutProduitColumn.setCellFactory(param -> new TableCell<Produit, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setTextFill(item.equalsIgnoreCase("Indisponible") ? Color.RED :
                            item.equalsIgnoreCase("Disponible") ? Color.GREEN : Color.BLACK);
                }
            }
        });

        quantiteColumn.setCellFactory(col -> new TableCell<Produit, Integer>() {
            private final TextField textField = new TextField();

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(textField);
                    textField.setText(item.toString());
                    textField.setOnAction(e -> {
                        try {
                            int newQuantity = Integer.parseInt(textField.getText());
                            Produit produit = getTableView().getItems().get(getIndex());
                            produit.setQuantite(newQuantity);
                            produit.setStatutProduit(newQuantity == 0 ? "Indisponible" : "Disponible");
                            produitService.update(produit);
                            produitTable.refresh();
                        } catch (NumberFormatException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Quantité invalide !");
                            alert.showAndWait();
                        }
                    });
                }
            }
        });

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
                setGraphic(empty ? null : hbox);
            }
        });

        setupCategorieComboBox();
        loadProduits();
    }

    private void setupCategorieComboBox() {
        categorieComboBox.getItems().clear();
        categorieComboBox.getItems().add("Tous");
        categorieComboBox.getItems().addAll(produitService.findAllCategories());
        categorieComboBox.getSelectionModel().selectFirst();

        categorieComboBox.setOnAction(event -> filterByCategorie());
    }

    private void filterByCategorie() {
        String selectedCategorie = categorieComboBox.getValue();
        List<Produit> produits = "Tous".equals(selectedCategorie) ?
                produitService.find() : produitService.findByCategorie(selectedCategorie);
        produitTable.getItems().setAll(produits);
    }

    private void loadProduits() {
        produitTable.getItems().setAll(produitService.find());
    }

    private void handleModify(Produit produit) {
        if (produit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatma/ModifierProduit.fxml"));
                Parent root = loader.load();

                ModifierProduitController controller = loader.getController();
                controller.setProduit(produit);

                Stage stage = new Stage();
                stage.setTitle("Modifier Produit");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                loadProduits();
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
                produitService.delete(produit);
                loadProduits();
            }
        }
    }
}
