package Controllers.salsabil;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Services.salsabil.UserService;
import Entities.salsabil.User;
import java.util.Random;

public class ReinitialiserMdpController {

    @FXML
    private TextField codeField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button sendCodeButton;

    @FXML
    private Button validateButton;

    private String generatedCode;
    private String userPhoneNumber;
    private String userEmail;

    private final UserService userService = new UserService();

    // Twilio Credentials
    private static final String ACCOUNT_SID = "AC8143edf86b523602dafda92cf882646b"; // À remplacer
    private static final String AUTH_TOKEN = "3eaed5f138e4199ffd5845a17e2e55b0";   // À remplacer
    private static final String FROM_NUMBER = "+12342595374"; // format international ex: "+1415xxxxxxx"

    public void setUserEmail(String email) {
        this.userEmail = email;
        chercherNumeroDeTelephone();
    }

    private void chercherNumeroDeTelephone() {
        User user = userService.findNumtelByEmail(userEmail);
        if (user != null) {
            userPhoneNumber = user.getNum_tel();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur introuvable !");
        }
    }

    @FXML
    private void initialize() {
        sendCodeButton.setOnAction(e -> envoyerCode());
        validateButton.setOnAction(e -> validerCodeEtChangerMotDePasse());
    }

    private void envoyerCode() {
        if (userPhoneNumber == null || userPhoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Numéro de téléphone introuvable !");
            return;
        }

        generatedCode = generateSixDigitCode();

        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(userPhoneNumber),
                    new com.twilio.type.PhoneNumber(FROM_NUMBER),
                    "Votre code de réinitialisation InnerBloom est : " + generatedCode
            ).create();

            System.out.println("SID du message : " + message.getSid());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Code envoyé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'envoi du SMS. Vérifiez votre configuration Twilio.");
        }
    }

    private void validerCodeEtChangerMotDePasse() {
        String enteredCode = codeField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!enteredCode.equals(generatedCode)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Code incorrect !");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas !");
            return;
        }

        if (newPassword.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Mot de passe faible", "Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }

        userService.updatePassword(userEmail, newPassword);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe réinitialisé avec succès !");
    }

    private String generateSixDigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
