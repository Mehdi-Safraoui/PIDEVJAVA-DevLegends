package Controllers.mehdi;

import Entities.mehdi.Avis;
import Services.mehdi.AvisService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherAvisController {

    @FXML
    private TableView<Avis> tableAvis;
    @FXML
    private TableColumn<Avis, Integer> colId;
    @FXML
    private TableColumn<Avis, String> colSujet;
    @FXML
    private TableColumn<Avis, String> colContenu;
    @FXML
    private TableColumn<Avis, String> colDate;
    @FXML
    private TableColumn<Avis, String> colEmail;
    @FXML
    private TableColumn<Avis, Integer> colNote;
    @FXML
    private TableColumn<Avis, String> colStatut;
    @FXML
    private TableColumn<Avis, Void> colActions;
    @FXML
    private Label labelMoyenne;
    @FXML
    private BarChart<String, Number> barChart;


    private final AvisService service = new AvisService();
    private ObservableList<Avis> avisList;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        avisList = FXCollections.observableArrayList(service.find());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujetAvis"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenuAvis"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateAvis().toString()));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailAvis"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("noteAvis"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutAvis"));

        addActionsButtons();

        tableAvis.setItems(avisList);

        updateBarChart(); // 👈 Appelle la méthode ci-dessous
        updateMoyenneLabel(); // 👈 Appelle la méthode ci-dessous

    }

    private void updateMoyenneLabel() {
        if (avisList.isEmpty()) {
            labelMoyenne.setText("Aucun avis disponible.");
            return;
        }

        double somme = avisList.stream().mapToInt(Avis::getNoteAvis).sum();
        double moyenne = somme / avisList.size();

        labelMoyenne.setText(String.format("⭐ Moyenne des avis : %.2f / 10 📝", moyenne));
    }


    private void updateBarChart() {
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre d'avis par note");

        Map<Integer, Long> distribution = avisList.stream()
                .collect(Collectors.groupingBy(Avis::getNoteAvis, Collectors.counting()));

        for (int i = 1; i <= 10; i++) {
            long count = distribution.getOrDefault(i, 0L);
            series.getData().add(new XYChart.Data<>(String.valueOf(i), count));
        }

        barChart.getData().add(series);
    }

    private void addActionsButtons() {
        colActions.setCellFactory(param -> new TableCell<>() {
            //            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox hbox = new HBox(10, btnSupprimer);

            {
//                btnModifier.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                hbox.setStyle("-fx-alignment: center;");

//                btnModifier.setOnAction(event -> {
//                    Avis avis = getTableView().getItems().get(getIndex());
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mehdi/ModifierAvis.fxml"));
//                        Parent root = loader.load();
//
//                        ModifierAvisController controller = loader.getController();
//                        controller.setAvis(avis);
//
//                        Stage stage = new Stage();
//                        stage.setTitle("Modifier Avis");
//                        stage.setScene(new Scene(root));
//                        stage.initOwner(tableAvis.getScene().getWindow());
//                        stage.initModality(Modality.APPLICATION_MODAL);
//                        stage.showAndWait();
//
//                        loadData();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });

                btnSupprimer.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Voulez-vous vraiment supprimer cet avis ?");
                    alert.setContentText("Sujet : " + avis.getSujetAvis());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            service.deleteById(avis.getId());
                            loadData();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }
}
