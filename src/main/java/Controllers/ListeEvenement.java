//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Controllers;

import Entities.Evenement;
import Services.ServiceEvenement;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ListeEvenement {
    @FXML
    private TableView<Evenement> tableEvenements;
    @FXML
    private TableColumn<Evenement, Integer> colId;
    @FXML
    private TableColumn<Evenement, String> colTitre;
    @FXML
    private TableColumn<Evenement, Date> colDate;
    @FXML
    private TableColumn<Evenement, String> colLieu;
    @FXML
    private TableColumn<Evenement, String> colStatut;
    @FXML
    private TableColumn<Evenement, Integer> colFormationId;
    @FXML
    private TableColumn<Evenement, Void> colActions;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private ComboBox<String> sortComboBox;
    private final ServiceEvenement service = new ServiceEvenement();
    private ObservableList<Evenement> tousLesEvenements = FXCollections.observableArrayList();
    private ObservableList<Evenement> evenementsFiltres = FXCollections.observableArrayList();

    public ListeEvenement() {
    }

    @FXML
    public void initialize() {
        this.colId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colTitre.setCellValueFactory(new PropertyValueFactory("titreEvent"));
        this.colDate.setCellValueFactory(new PropertyValueFactory("dateEvent"));
        this.colLieu.setCellValueFactory(new PropertyValueFactory("lieuEvent"));
        this.colStatut.setCellValueFactory(new PropertyValueFactory("statutEvent"));
        this.colFormationId.setCellValueFactory(new PropertyValueFactory("formationId"));
        this.sortComboBox.getItems().addAll(new String[]{"Date (récent)", "Date (ancien)", "Titre (A-Z)", "Titre (Z-A)"});
        this.loadEvenements();
    }

    private void loadEvenements() {
        List<Evenement> evenements = this.service.getAll();
        this.tousLesEvenements.setAll(evenements);
        this.evenementsFiltres.setAll(evenements);
        this.tableEvenements.setItems(this.evenementsFiltres);
        this.addActionButtons();
    }

    private void addActionButtons() {
        this.colActions.setCellFactory((param) -> new TableCell<Evenement, Void>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnQRCode = new Button("QR Code");
            private final HBox pane;

            {
                this.pane = new HBox((double)5.0F, new Node[]{this.btnModifier, this.btnSupprimer, this.btnQRCode});
                this.btnModifier.setOnAction((event) -> {
                    Evenement evt = (Evenement)this.getTableView().getItems().get(this.getIndex());
                    ListeEvenement.this.ouvrirPageModification(evt);
                });
                this.btnSupprimer.setOnAction((event) -> {
                    Evenement evt = (Evenement)this.getTableView().getItems().get(this.getIndex());
                    if (ListeEvenement.this.confirmerSuppression()) {
                        ListeEvenement.this.service.delete(evt.getId());
                        ListeEvenement.this.loadEvenements();
                        ListeEvenement.this.showAlert(AlertType.INFORMATION, "Succès", "Événement supprimé avec succès.");
                    }

                });
                this.btnQRCode.setOnAction((event) -> {
                    Evenement evt = (Evenement)this.getTableView().getItems().get(this.getIndex());
                    String pdfPath = "QRCode_Evenement_" + evt.getId() + ".pdf";
                    QRCodePDFGenerator.generateQRCodePDF(
                            evt.getTitreEvent(),        // String
                            evt.getId(),                // int
                            evt.getDateEvent().toString(), // String (date au format texte)
                            evt.getLieuEvent(),          // String
                            pdfPath                     // String (chemin du PDF)
                    );

                    try {
                        File pdfFile = new File(pdfPath);
                        if (pdfFile.exists()) {
                            Desktop.getDesktop().open(pdfFile);
                        }

                        ListeEvenement.this.showAlert(AlertType.INFORMATION, "QR Code généré", "QR Code généré dans : " + pdfPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ListeEvenement.this.showAlert(AlertType.ERROR, "Erreur", "Impossible d'ouvrir le fichier QR Code.");
                    }

                });
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                this.setGraphic(empty ? null : this.pane);
            }
        });
    }

    private void ouvrirPageModification(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/ModifierEvent.fxml"));
            AnchorPane pane = (AnchorPane)loader.load();
            ModifierEvenement controller = (ModifierEvenement)loader.getController();
            controller.setEvenement(evenement);
            Stage stage = new Stage();
            stage.setTitle("Modifier un événement");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert(AlertType.ERROR, "Erreur", "Impossible d’ouvrir la page de modification.");
        }

    }

    private boolean confirmerSuppression() {
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText((String)null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void allerAjoutPage() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/AjoutEvent.fxml"));
            AnchorPane pane = (AnchorPane)loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.showAlert(AlertType.ERROR, "Erreur", "Impossible d’ouvrir la page d'ajout.");
        }

    }

    @FXML
    public void filtrerEvenements() {
        String searchText = this.searchField.getText().toLowerCase();
        LocalDate selectedDate = (LocalDate)this.dateDebutPicker.getValue();
        List<Evenement> filtered = (List)this.tousLesEvenements.stream().filter((e) -> searchText.isEmpty() || e.getTitreEvent().toLowerCase().contains(searchText) || e.getLieuEvent().toLowerCase().contains(searchText)).filter((e) -> selectedDate == null || e.getDateEvent().toLocalDate().isEqual(selectedDate)).collect(Collectors.toList());
        this.evenementsFiltres.setAll(filtered);
        this.tableEvenements.setItems(this.evenementsFiltres);
    }

    @FXML
    public void trierEvenements() {
        String critere = (String)this.sortComboBox.getValue();
        if (critere != null) {
            Comparator var10000;
            switch (critere) {
                case "Date (récent)" -> var10000 = Comparator.comparing(Evenement::getDateEvent).reversed();
                case "Date (ancien)" -> var10000 = Comparator.comparing(Evenement::getDateEvent);
                case "Titre (A-Z)" -> var10000 = Comparator.comparing(Evenement::getTitreEvent, String.CASE_INSENSITIVE_ORDER);
                case "Titre (Z-A)" -> var10000 = Comparator.comparing(Evenement::getTitreEvent, String.CASE_INSENSITIVE_ORDER).reversed();
                default -> var10000 = null;
            }

            Comparator<Evenement> comparator = var10000;
            if (comparator != null) {
                FXCollections.sort(this.evenementsFiltres, comparator);
                this.tableEvenements.refresh();
            }

        }
    }

    @FXML
    public void reinitialiserFiltres() {
        this.searchField.clear();
        this.dateDebutPicker.setValue((LocalDate) null);
        this.sortComboBox.getSelectionModel().clearSelection();
        this.evenementsFiltres.setAll(this.tousLesEvenements);
        this.tableEvenements.setItems(this.evenementsFiltres);
    }
}
