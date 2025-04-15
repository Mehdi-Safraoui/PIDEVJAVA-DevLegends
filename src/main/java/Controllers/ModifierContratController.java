package Controllers;

import Entities.Contrat;
import Services.CentreService;
import Services.ContratService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierContratController {

    @FXML private Label labelTitre;
    @FXML private Label labelCentre; // Remplace le ComboBox
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private ComboBox<String> comboPaiement;
    @FXML private CheckBox checkRenouvellement;
    @FXML private Button btnEnregistrer;
    @FXML private Button btnAnnuler;

    private ContratService contratService;
    private CentreService centreService;
    private Contrat contratToEdit;

    public void initialize() {
        contratService = new ContratService();
        centreService = new CentreService();
        initializeComboBoxes();
    }

    private void initializeComboBoxes() {
        // Initialiser les modes de paiement seulement
        comboPaiement.getItems().addAll(
                "Espèces",
                "Carte bancaire",
                "Virement",
                "Chèque"
        );
    }

    public void setContratToEdit(Contrat contrat) {
        this.contratToEdit = contrat;
        populateFields();
    }

    private void populateFields() {
        if (contratToEdit != null) {
            labelTitre.setText("Modifier le contrat #" + contratToEdit.getId());

            // Afficher le centre (non modifiable)
            String nomCentre = centreService.findById(contratToEdit.getCentreId()).getNomCentre();
            labelCentre.setText(nomCentre);

            // Dates
            dateDebut.setValue(toLocalDate(contratToEdit.getDatdebCont()));
            dateFin.setValue(toLocalDate(contratToEdit.getDatfinCont()));

            // Autres champs
            comboPaiement.setValue(contratToEdit.getModpaimentCont());
            checkRenouvellement.setSelected(contratToEdit.isRenouvAutoCont());
        }
    }

    private LocalDate toLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @FXML
    private void handleEnregistrer() {
        try {
            // Valider les champs
            if (!validateFields()) {
                return;
            }

            // Mettre à jour le contrat (sans changer le centre)
            contratToEdit.setDatdebCont(toDate(dateDebut.getValue()));
            contratToEdit.setDatfinCont(toDate(dateFin.getValue()));
            contratToEdit.setModpaimentCont(comboPaiement.getValue());
            contratToEdit.setRenouvAutoCont(checkRenouvellement.isSelected());

            // Sauvegarder
            contratService.update(contratToEdit);

            // Fermer la fenêtre
            closeWindow();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        // Validation de la date de début
        if (dateDebut.getValue() == null) {
            errors.append("- Veuillez sélectionner une date de début\n");
        } else {
            LocalDate dateDebutValue = dateDebut.getValue();
            LocalDate currentDate = LocalDate.now();
            if (dateDebutValue.isBefore(currentDate)) {
                errors.append("- La date de début doit être supérieure ou égale à la date actuelle\n");
            }
        }

        // Validation de la date de fin
        if (dateFin.getValue() == null) {
            errors.append("- Veuillez sélectionner une date de fin\n");
        } else if (dateDebut.getValue() != null && dateFin.getValue().isBefore(dateDebut.getValue())) {
            errors.append("- La date de fin doit être après la date de début\n");
        }

        // Validation du mode de paiement
        if (comboPaiement.getValue() == null) {
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
