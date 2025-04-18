package Controllers.mehdi;

import Entities.mehdi.Reclamation;
import Services.mehdi.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifierReclamationController {

    @FXML
    private TextField tfSujet;
    @FXML
    private TextArea taContenu;
    @FXML
    private TextField tfEmail;

    private Reclamation reclamation; // La réclamation en cours de modification
    private final ReclamationService service = new ReclamationService();

    // Appelé depuis le contrôleur d'affichage pour pré-remplir les champs
    public void setReclamation(Reclamation r) {
        this.reclamation = r;
        tfSujet.setText(r.getSujetRec());
        taContenu.setText(r.getContenuRec());
        tfEmail.setText(r.getEmailRec());
    }

    @FXML
    private void modifierReclamation() {
        this.getClass().getResource("/AvisRecStyle.css").toExternalForm();

        if (reclamation != null) {
            reclamation.setSujetRec(tfSujet.getText());
            reclamation.setContenuRec(taContenu.getText());
            reclamation.setEmailRec(tfEmail.getText());

            service.update(reclamation);

            // Fermer la fenêtre après modification
            Stage stage = (Stage) tfSujet.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void annulerModification() {
        Stage stage = (Stage) tfSujet.getScene().getWindow();
        stage.close();
    }
}
