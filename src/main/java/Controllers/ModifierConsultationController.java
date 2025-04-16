package Controllers;

import Entities.Consultation;
import Services.ConsultationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Date;
import java.time.LocalDate;

public class ModifierConsultationController {

    @FXML private DatePicker datePicker;
    @FXML private TextField lienField;
    @FXML private TextArea notesField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField ageField;
    @FXML private Button btnModifier;

    private Consultation consultation;
    private final ConsultationService service = new ConsultationService();

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;

        // Convertir java.util.Date en LocalDate manuellement
        LocalDate localDate = convertToLocalDate(consultation.getDateCons());
        datePicker.setValue(localDate);

        lienField.setText(consultation.getLienVisioCons());
        notesField.setText(consultation.getNotesCons());
        nomField.setText(consultation.getNom());
        prenomField.setText(consultation.getPrenom());
        ageField.setText(String.valueOf(consultation.getAge()));
    }

    private LocalDate convertToLocalDate(java.util.Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }



    @FXML
    public void modifierConsultation() {
        if (consultation != null) {
            // VALIDATIONS
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String notes = notesField.getText().trim();
            String lien = lienField.getText().trim();
            String ageText = ageField.getText().trim();
            LocalDate selectedDate = datePicker.getValue();
            LocalDate currentDate = LocalDate.now();

            // Validation des champs
            if (nom.length() < 3 || prenom.length() < 3 || notes.length() < 3 || lien.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs invalides", "Veuillez remplir tous les champs avec au moins 3 caractères.");
                return;
            }

            // Âge
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age > 100) {
                    showAlert(Alert.AlertType.WARNING, "Âge invalide", "L'âge doit être inférieur ou égal à 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'âge", "Veuillez entrer un âge valide.");
                return;
            }

            // Date
            if (selectedDate == null || !selectedDate.isAfter(currentDate)) {
                showAlert(Alert.AlertType.WARNING, "Date invalide", "Veuillez choisir une date postérieure à aujourd'hui.");
                return;
            }

            // MODIFICATION
            consultation.setDateCons(java.sql.Date.valueOf(selectedDate));
            consultation.setLienVisioCons(lien);
            consultation.setNotesCons(notes);
            consultation.setNom(nom);
            consultation.setPrenom(prenom);
            consultation.setAge(age);

            service.update(consultation);

            // Fermer la fenêtre
            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.close();
        }
    }

    @FXML private Button btnAnnuler;

    @FXML
    public void annulerModification() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
