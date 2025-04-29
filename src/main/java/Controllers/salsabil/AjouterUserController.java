package Controllers.salsabil;

import Entities.salsabil.User;
import Services.salsabil.UserService;
//import javafx.animation.FadeTransition;
import Utils.AvatarUtils;
import Utils.MailUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import javafx.util.Duration;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class AjouterUserController implements Initializable {

    @FXML private ImageView logoImage;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField phoneField;
    @FXML private TextField ageField;
    @FXML private TextField addressField;
    @FXML private TextField pictureField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label specialtyLabel;
    @FXML private ComboBox<String> specialtyComboBox;
    @FXML private Button submitButton;
    @FXML private ImageView previewImageView;
    //@FXML private VBox formVBox;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), formVBox);
//        fadeIn.setFromValue(0);
//        fadeIn.setToValue(1);
//        fadeIn.play();

        Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle();
        clip.setRadius(25); // half of 120 width/height
        clip.setCenterX(25);
        clip.setCenterY(25);
        logoImage.setClip(clip);
        roleComboBox.getItems().addAll("Patient", "Medecin");
        specialtyComboBox.getItems().addAll("Psychiatre", "Psychothérapeute", "Psychologue");

        roleComboBox.setOnAction(event -> {
            boolean isMedecin = "Medecin".equals(roleComboBox.getValue());
            specialtyLabel.setVisible(isMedecin);
            specialtyComboBox.setVisible(isMedecin);
        });
        phoneField.setText("+216"); // Valeur par défaut

        phoneField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.startsWith("+216")) {
                phoneField.setText("+216");
            } else if (newText.length() > 12) { // +216 + 8 chiffres = 12 caractères max
                phoneField.setText(oldText);
            } else if (!newText.matches("\\+216\\d*")) {
                phoneField.setText(oldText); // Annule toute entrée non-numérique après +216
            }
        });
        submitButton.setOnAction(event -> handleSubmit());
        clearForm();
    }

    @FXML
    private void handleBrowsePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            pictureField.setText(filePath);

            Image image = new Image("file:" + filePath);
            previewImageView.setImage(image);
        }
    }


    private void handleSubmit() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText().trim();
        String ageText = ageField.getText().trim();
        String address = addressField.getText().trim();
        String picture = pictureField.getText().trim().isEmpty() ? null : pictureField.getText().trim();
        String role = roleComboBox.getValue();
        String specialty = specialtyComboBox.getValue();

        // Vérification des champs requis
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() || phone.isEmpty() || ageText.isEmpty()
                || address.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Format de l'email invalide.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas.");
            return;
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$")) {
            showAlert(Alert.AlertType.ERROR, "Le mot de passe doit contenir au moins 8 caractères, incluant une majuscule, une minuscule, un chiffre et un caractère spécial.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 0 || age > 120) {
                showAlert(Alert.AlertType.ERROR, "Âge invalide.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "L'âge doit être un nombre.");
            return;
        }

        if (!phone.matches("^\\+216\\d{8}$")) {
            showAlert(Alert.AlertType.ERROR, "Le numéro doit commencer par +216 et contenir 8 chiffres.");
            return;
        }
        if (userService.isPhoneExists(phone)) {
            showAlert(Alert.AlertType.ERROR, "Ce numéro de téléphone est déjà utilisé.");
            return;
        }

        if ("Medecin".equals(role) && (specialty == null || specialty.isEmpty())) {
            showAlert(Alert.AlertType.ERROR, "Veuillez sélectionner une spécialité.");
            return;
        }


        User user = new User(
                firstName, lastName, email, password,
                role, age, specialty, phone, address, picture
        );
        user.setStatut_compte(true);
        // Si aucune image sélectionnée, générer un avatar automatiquement
        if (user.getUser_picture() == null || user.getUser_picture().isEmpty()) {
            String avatarPath = AvatarUtils.genererAvatar(user.getUser_email());
            user.setUser_picture(avatarPath);
        }

        userService.add(user);
        // Envoi de l'email de bienvenue
        String subject = "Bienvenue sur notre plateforme InnerBloom !";
        String message = "Bonjour " + user.getFirst_name() + ",\n\nMerci pour votre inscription sur notre plateforme InnerBloom.";
        MailUtil.envoyerMail(user.getUser_email(), subject, message);

        // Afficher une alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inscription réussie");
        alert.setHeaderText(null);
        alert.setContentText("Inscription réussie. Vous pouvez maintenant vous connecter.");
        alert.showAndWait(); // Attendre que l'utilisateur ferme la boîte de dialogue

// Rediriger vers la page de connexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Login.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) submitButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Connexion");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        phoneField.clear();
        ageField.clear();
        addressField.clear();
        pictureField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        specialtyComboBox.getSelectionModel().clearSelection();
        specialtyLabel.setVisible(false);
        specialtyComboBox.setVisible(false);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleCancel() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        phoneField.clear();
        ageField.clear();
        addressField.clear();
        pictureField.clear();
        previewImageView.setImage(null);
        roleComboBox.getSelectionModel().clearSelection();
        specialtyComboBox.getSelectionModel().clearSelection();
        specialtyLabel.setVisible(false);
        specialtyComboBox.setVisible(false);
    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Login.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et y afficher la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleGoToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Login.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et y afficher la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
