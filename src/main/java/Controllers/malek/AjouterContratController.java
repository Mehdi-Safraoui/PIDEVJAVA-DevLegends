package Controllers.malek;

import Entities.malek.Contrat;
import Utils.Session;
import Entities.salsabil.User;
import Services.malek.CentreService;
import Services.malek.ContratService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AjouterContratController {

    @FXML private ComboBox<String> cbCentre;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private ComboBox<String> cbModePaiement;
    @FXML private CheckBox cbRenouvAuto;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;

    private ContratService contratService;
    private CentreService centreService;

    public void initialize() {
        contratService = new ContratService();
        centreService = new CentreService();

        // Initialiser les ComboBox
        initializeCentres();
        initializeModePaiement();
    }

    private void initializeCentres() {
        // Récupérer la liste des noms de centres depuis la base de données
        ObservableList<String> nomsCentres = FXCollections.observableArrayList(
                centreService.getAllNomsCentres()
        );
        cbCentre.setItems(nomsCentres);
    }

    private void initializeModePaiement() {
        ObservableList<String> modesPaiement = FXCollections.observableArrayList(
                "Espèces", "Carte bancaire", "Virement", "Chèque"
        );
        cbModePaiement.setItems(modesPaiement);
    }

    @FXML
    private void handleAjouter() {
        // Validation des champs
        if (!validateFields()) {
            return;
        }

        try {
            // Création du nouveau contrat
            Contrat contrat = new Contrat();

            // Récupérer l'ID du centre sélectionné par son nom
            String nomCentre = cbCentre.getValue();
            int centreId = centreService.getCentreIdByName(nomCentre);
            contrat.setCentreId(centreId);

            // Conversion des LocalDate en Date
            LocalDate localDateDebut = dpDateDebut.getValue();
            Date dateDebut = Date.from(localDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            contrat.setDatdebCont(dateDebut);

            LocalDate localDateFin = dpDateFin.getValue();
            Date dateFin = Date.from(localDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
            contrat.setDatfinCont(dateFin);

            contrat.setModpaimentCont(cbModePaiement.getValue());
            contrat.setRenouvAutoCont(cbRenouvAuto.isSelected());

            // Récupérer l'ID de l'utilisateur connecté depuis la session
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                contrat.setUserId(currentUser.getId());
            } else {
                showAlert("Erreur", "Aucun utilisateur connecté!", Alert.AlertType.ERROR);
                return;
            }

            // Ajout du contrat
            contratService.add(contrat);

            // Afficher un message de succès
            showAlert("Succès", "Contrat ajouté avec succès!", Alert.AlertType.INFORMATION);

            loadAccueilPage();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout du contrat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // Méthode pour charger la page d'accueil
    private void loadAccueilPage() {
        try {
            // Charger l'écran d'accueil (par exemple "LandingPage.fxml")
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/AfficherContratFront.fxml"));
            Parent root = loader.load();

            // Obtenez la scène actuelle
            Stage currentStage = (Stage) btnAnnuler.getScene().getWindow();

            // Définir la nouvelle scène sur la même fenêtre (Stage)
            Scene newScene = new Scene(root);
            currentStage.setScene(newScene);
            currentStage.setTitle("Page d'Accueil");

            // Afficher la scène d'accueil
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnuler() {
        loadAccueilPage();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        // Validation du centre
        if (cbCentre.getValue() == null || cbCentre.getValue().isEmpty()) {
            errors.append("- Veuillez sélectionner un centre\n");
        }

        // Validation de la date de début
        if (dpDateDebut.getValue() == null) {
            errors.append("- Veuillez sélectionner une date de début\n");
        } else {
            LocalDate dateDebut = dpDateDebut.getValue();
            LocalDate currentDate = LocalDate.now();
            if (dateDebut.isBefore(currentDate)) {
                errors.append("- La date de début doit être supérieure ou égale à la date actuelle\n");
            }
        }

        // Validation de la date de fin
        if (dpDateFin.getValue() == null) {
            errors.append("- Veuillez sélectionner une date de fin\n");
        } else if (dpDateDebut.getValue() != null && dpDateFin.getValue().isBefore(dpDateDebut.getValue())) {
            errors.append("- La date de fin doit être après la date de début\n");
        }

        // Validation du mode de paiement
        if (cbModePaiement.getValue() == null || cbModePaiement.getValue().isEmpty()) {
            errors.append("- Veuillez sélectionner un mode de paiement\n");
        }

        // Affichage des erreurs
        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString(), Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}
