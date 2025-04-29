package Controllers.mehdi;

import Entities.mehdi.Avis;
import Services.mehdi.AvisService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherAvisFrontController {

    @FXML
    private TilePane tilePaneAvis;

    private final AvisService service = new AvisService();

    private List<Avis> allAvis;
    private static final int AVIS_PAR_PAGE = 9;
    private int currentPage = 0;

    @FXML
    private HBox paginationBox;


    @FXML
    public void initialize() {
        allAvis = service.find();
        afficherPage(currentPage);
        ajouterNavigation();
    }

    private void afficherPage(int pageIndex) {
        tilePaneAvis.getChildren().clear();

        int start = pageIndex * AVIS_PAR_PAGE;
        int end = Math.min(start + AVIS_PAR_PAGE, allAvis.size());

        List<Avis> sousListe = allAvis.subList(start, end);

        for (Avis avis : sousListe) {
            VBox card = createAvisCard(avis);
            tilePaneAvis.getChildren().add(card);
        }
    }

    private void ajouterNavigation() {
        Button btnPrecedent = new Button("⬅️ Précédent");
        Button btnSuivant = new Button("Suivant ➡️");

        btnPrecedent.setOnAction(e -> {
            if (currentPage > 0) {
                currentPage--;
                afficherPage(currentPage);
            }
        });

        btnSuivant.setOnAction(e -> {
            if ((currentPage + 1) * AVIS_PAR_PAGE < allAvis.size()) {
                currentPage++;
                afficherPage(currentPage);
            }
        });

        btnPrecedent.setStyle("-fx-background-color: white; -fx-text-fill: #3498db;");
        btnSuivant.setStyle("-fx-background-color: white; -fx-text-fill: #3498db;");

        paginationBox.getChildren().addAll(btnPrecedent, btnSuivant);
    }


    private VBox createAvisCard(Avis avis) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 3);");

        Label sujetLabel = new Label("📌 Sujet : " + avis.getSujetAvis());
        Label contenuLabel = new Label("📝 " + avis.getContenuAvis());
        Label emailLabel = new Label("✉️ Email : " + avis.getEmailAvis());

        int note = avis.getNoteAvis();
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            stars.append(i <= (note + 1) / 2 ? "★" : "☆");
        }
        Label etoilesLabel = new Label("⭐ Avis : " + stars);
        etoilesLabel.setStyle("-fx-font-size: 14px;");

        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");

        HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
        buttonBox.setStyle("-fx-alignment: center;");
        buttonBox.setPadding(new Insets(5, 0, 0, 0));

        btnModifier.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        btnModifier.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mehdi/ModifierAvis.fxml"));
                Parent root = loader.load();

                ModifierAvisController controller = loader.getController();
                controller.setAvis(avis);

                Stage stage = new Stage();
                stage.setTitle("Modifier Avis");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Recharge la liste après modification
                allAvis = service.find();
                afficherPage(currentPage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnSupprimer.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Suppression de l'avis");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet avis ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    service.deleteById(avis.getId());
                    allAvis = service.find();
                    afficherPage(currentPage);
                }
            });
        });

        card.getChildren().addAll(sujetLabel, contenuLabel, etoilesLabel, emailLabel, buttonBox);
        return card;
    }
}
