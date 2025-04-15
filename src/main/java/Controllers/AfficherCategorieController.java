package Controllers;

import Entities.Categorie;
import Services.CategorieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class AfficherCategorieController implements Initializable {

    @FXML
    private TableView<Categorie> categorieTable;

    @FXML
    private TableColumn<Categorie, String> nomCategorieColumn;

    @FXML
    private TableColumn<Categorie, Void> actionColumn;

    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomCategorieColumn.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));

        actionColumn.setCellFactory(param -> new TableCell<>() {

            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox hbox = new HBox(10, modifyButton, deleteButton);

            {
                modifyButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");

                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5, 5, 5, 5));

                modifyButton.setOnAction(e -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    handleModify(categorie);
                });

                deleteButton.setOnAction(e -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    handleDelete(categorie);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        loadCategories();
    }

    private void loadCategories() {
        List<Categorie> categories = categorieService.find();
        categorieTable.getItems().setAll(categories);
    }

    private void handleModify(Categorie categorie) {
        if (categorie != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCategorie.fxml"));
                Parent root = loader.load();

                ModifierCategorieController controller = loader.getController();
                controller.setCategorie(categorie);

                Stage stage = new Stage();
                stage.setTitle("Modifier Catégorie");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                loadCategories();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDelete(Categorie categorie) {
        if (categorie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Suppression");
            alert.setHeaderText("Supprimer la catégorie : " + categorie.getNomCategorie());
            alert.setContentText("Es-tu sûr de vouloir supprimer cette catégorie ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                categorieService.delete(categorie);
                loadCategories();
            }
        }
    }
}
