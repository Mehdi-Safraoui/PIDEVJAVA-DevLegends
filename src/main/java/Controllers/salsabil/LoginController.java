package Controllers.salsabil;

import Entities.salsabil.User;
import Services.salsabil.UserService;
import Utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserService userService = new UserService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        User user = userService.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {

            // Vérification du bannissement
            if (user.getBanned_until() != null) {
                if (user.getBanned_until().isAfter(java.time.LocalDateTime.now())) {
                    errorLabel.setText("Vous êtes banni jusqu’au : " + user.getBanned_until().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    return;
                } else {
                    // Le bannissement est expiré : on le lève
                    user.setBanned_until(null);
                    userService.liftBan(user.getId());  // Méthode à créer pour mettre à jour en base
                }
            }

            Session.setCurrentUser(user);

            // Redirection vers le profil de l'utilisateur
            loadProfile(user);

        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }

    private void loadProfile(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Profil.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur du profil et lui passer l'utilisateur
            ProfilController profilController = loader.getController();
            profilController.setUser(user);  // Passer l'utilisateur au contrôleur de profil

            // Changer la scène pour afficher le profil
            emailField.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("❌ Erreur de chargement du profil : " + e.getMessage());
            e.printStackTrace();
        }
    }


    //    @FXML
//    private void handleLogin() {
//        String email = emailField.getText().trim();
//        String password = passwordField.getText();
//
//        if (email.isEmpty() || password.isEmpty()) {
//            errorLabel.setText("Veuillez remplir tous les champs.");
//            return;
//        }
//
//        User user = userService.findByEmail(email);
//        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
//
//            // Vérification du bannissement
//            if (user.getBanned_until() != null) {
//                if (user.getBanned_until().isAfter(java.time.LocalDateTime.now())) {
//                    errorLabel.setText("Vous êtes banni jusqu’au : " + user.getBanned_until().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
//                    return;
//                } else {
//                    // Le bannissement est expiré : on le lève
//                    user.setBanned_until(null);
//                    userService.liftBan(user.getId());  // Méthode à créer pour mettre à jour en base
//                }
//            }
//
//            Session.setCurrentUser(user);
//
//            // Redirection selon le rôle
//            String role = user.getUser_role().toLowerCase();
//            switch (role) {
//                case "admin":
//                    loadDashboard("/salsabil/AdminDashboard.fxml", user);
//                    break;
//                case "medecin":
//                case "patient":
//                    loadDashboard("/salsabil/UserDashboard.fxml", user);
//                    break;
//                default:
//                    errorLabel.setText("Rôle inconnu.");
//            }
//
//        } else {
//            errorLabel.setText("Email ou mot de passe incorrect.");
//        }
//
//    }
//
//
//    private void loadDashboard(String fxmlPath, User user) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//            Parent root = loader.load();
//
//            // Obtenir le contrôleur du dashboard et lui passer l'utilisateur
//            Object controller = loader.getController();
//            if (controller instanceof AdminDashboardController) {
//                ((AdminDashboardController) controller).setUser(user);
//            } else if (controller instanceof UserDashboardController) {
//                ((UserDashboardController) controller).setUser(user);
//            }
//
//            emailField.getScene().setRoot(root);
//        } catch (IOException e) {
//            System.out.println("❌ Erreur de chargement du dashboard : " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
