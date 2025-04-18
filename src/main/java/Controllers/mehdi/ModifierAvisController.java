package Controllers.mehdi;

import Entities.mehdi.Avis;
import Services.mehdi.AvisService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifierAvisController {

    @FXML
    private TextField tfSujet;
    @FXML
    private TextArea taContenu;
    @FXML
    private TextField tfEmail;
    @FXML
    private Spinner<Integer> spNote;

    private Avis avis;
    private final AvisService service = new AvisService();

    public void setAvis(Avis a) {
        this.avis = a;
        tfSujet.setText(a.getSujetAvis());
        taContenu.setText(a.getContenuAvis());
        tfEmail.setText(a.getEmailAvis());

        if (spNote.getValueFactory() == null) {
            spNote.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, a.getNoteAvis()));
        } else {
            spNote.getValueFactory().setValue(a.getNoteAvis());
        }
    }

    @FXML
    private void modifierAvis() {
        if (avis != null) {
            avis.setSujetAvis(tfSujet.getText());
            avis.setContenuAvis(taContenu.getText());
            avis.setEmailAvis(tfEmail.getText());
            avis.setNoteAvis(spNote.getValue());

            service.update(avis);

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
