package Controllers.salsabil;

import Entities.salsabil.User;
import Services.salsabil.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.regex.Pattern;

public class ModifierUserController {

    @FXML private TextField TFNom;
    @FXML private TextField TFPrenom;
    @FXML private TextField TFEmail;
    @FXML private TextField TFAge;
    @FXML private TextField TFSpecialite;
    @FXML private TextField TFNumTel;
    @FXML private TextField TFAdresse;
    @FXML private TextField TFRole;
    @FXML private TextField TFImage;
    @FXML private Button btnUploadImage;

    private User userAModifier;
    private final UserService userService = new UserService();
    private String newImagePath;

    public void setUserAModifier(User user) {
        this.userAModifier = user;
        remplirChamps();
    }

    private void remplirChamps() {
        TFNom.setText(userAModifier.getLast_name());
        TFPrenom.setText(userAModifier.getFirst_name());
        TFEmail.setText(userAModifier.getUser_email());
        TFAge.setText(String.valueOf(userAModifier.getUser_age()));
        TFRole.setText(userAModifier.getUser_role());
        TFSpecialite.setText(userAModifier.getDoc_specialty());
        TFNumTel.setText(userAModifier.getNum_tel());
        TFAdresse.setText(userAModifier.getAddress());
        TFImage.setText(userAModifier.getUser_picture());
    }

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", ".jpg", ".jpeg", ".png", ".gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            newImagePath = file.getAbsolutePath();
            TFImage.setText(newImagePath);
        }
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        if (!validateFields()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Modifier le profil ?");
        confirm.setContentText("Êtes-vous sûr de vouloir enregistrer les modifications ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            updateUserFromForm();
            userService.update(userAModifier);
            showAlert("Succès", "Profil utilisateur mis à jour avec succès.", Alert.AlertType.INFORMATION);
            closeWindow(event);
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Annulation");
        confirm.setHeaderText("Annuler les modifications ?");
        confirm.setContentText("Voulez-vous quitter sans enregistrer ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            closeWindow(event);
        }
    }

    private void updateUserFromForm() {
        userAModifier.setFirst_name(TFPrenom.getText());
        userAModifier.setLast_name(TFNom.getText());
        userAModifier.setUser_email(TFEmail.getText());
        userAModifier.setUser_role(TFRole.getText());
        userAModifier.setUser_age(Integer.parseInt(TFAge.getText()));
        userAModifier.setDoc_specialty(TFSpecialite.getText());
        userAModifier.setNum_tel(TFNumTel.getText());
        userAModifier.setAddress(TFAdresse.getText());
        if (newImagePath != null) {
            userAModifier.setUser_picture(newImagePath);
        }
    }

    private boolean validateFields() {
        resetFieldStyles();
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;

        if (TFNom.getText().trim().length() < 3) {
            errors.append("- Le nom est trop court\n");
            TFNom.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (TFPrenom.getText().trim().length() < 3) {
            errors.append("- Le prénom est trop court\n");
            TFPrenom.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!TFEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.append("- Email invalide\n");
            TFEmail.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        try {
            int age = Integer.parseInt(TFAge.getText());
            if (age <= 0) {
                errors.append("- L'âge doit être positif\n");
                TFAge.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            errors.append("- Âge invalide\n");
            TFAge.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!TFNumTel.getText().matches("^\\+216\\d{8}$")) {
            errors.append("- Numéro de téléphone invalide (+216XXXXXXXX)\n");
            TFNumTel.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            showAlert("Erreurs", errors.toString(), Alert.AlertType.ERROR);
        }

        return isValid;
    }

    private void resetFieldStyles() {
        TFNom.setStyle("");
        TFPrenom.setStyle("");
        TFEmail.setStyle("");
        TFAge.setStyle("");
        TFNumTel.setStyle("");
        TFAdresse.setStyle("");
        TFRole.setStyle("");
        TFSpecialite.setStyle("");
        TFImage.setStyle("");
    }

    private void closeWindow(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}