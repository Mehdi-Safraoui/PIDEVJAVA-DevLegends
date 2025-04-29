package Controllers.salsabil;

import Entities.salsabil.User;
import Services.salsabil.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import Utils.Session;

public class ChangerMotDePasseController {

    @FXML private PasswordField txtAncienMotDePasse;
    @FXML private PasswordField txtNouveauMotDePasse;
    @FXML private PasswordField txtConfirmationMotDePasse;

    private final UserService userService = new UserService();
    private final User utilisateurActuel = Session.getCurrentUser();

    @FXML
    private void handleValider() {
        String ancien = txtAncienMotDePasse.getText();
        String nouveau = txtNouveauMotDePasse.getText();
        String confirmation = txtConfirmationMotDePasse.getText();

        if (ancien.isEmpty() || nouveau.isEmpty() || confirmation.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!nouveau.equals(confirmation)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        // Vérification du mot de passe actuel (haché)
        String motDePasseHache = userService.getMotDePasseParId(utilisateurActuel.getId());

        if (!BCrypt.checkpw(ancien, motDePasseHache)) {
            showAlert(Alert.AlertType.ERROR, "Mot de passe incorrect", "L'ancien mot de passe est invalide.");
            return;
        }

        // Mise à jour du nouveau mot de passe haché
        String nouveauHash = BCrypt.hashpw(nouveau, BCrypt.gensalt());
        boolean success = userService.updateMotDePasse(utilisateurActuel.getId(), nouveauHash);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mot de passe modifié");
            alert.setHeaderText(null);
            alert.setContentText("Votre mot de passe a été modifié avec succès.\nVous allez être redirigé vers la page de connexion.");

            // Attendre que l'utilisateur clique sur OK
            alert.showAndWait();

            // Déconnexion et redirection
            Session.clear();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) txtAncienMotDePasse.getScene().getWindow();
            currentStage.close();  // Ferme la fenêtre actuelle

            // Charger Login.fxml
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Login.fxml"));
                Parent root = loader.load();

                Stage loginStage = new Stage();
                loginStage.setTitle("Connexion");
                loginStage.setScene(new Scene(root));
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
