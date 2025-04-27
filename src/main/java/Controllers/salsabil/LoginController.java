package Controllers.salsabil;

import Entities.malek.Contrat;
import Entities.salsabil.User;
import Services.malek.CentreService;
import Services.malek.ContratService;
import Services.salsabil.CaptchaService;
import Services.salsabil.UserService;
import Utils.CaptchaQuestion;
import Utils.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.Notifications;
import javafx.util.Duration;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label captchaQuestionLabel; // Ajout de la question CAPTCHA
    @FXML private TextField captchaAnswerField; // Champ pour la réponse CAPTCHA
    @FXML private Label captchaErrorLabel; // Étiquette d'erreur pour CAPTCHA

    private UserService userService = new UserService();
    private CaptchaService captchaService = new CaptchaService(); // Service pour gérer les questions CAPTCHA
    private CaptchaQuestion currentCaptchaQuestion;

    @FXML
    private void initialize() {
        // Charger une question CAPTCHA au démarrage
        loadCaptchaQuestion();
    }

    private void loadCaptchaQuestion() {
        currentCaptchaQuestion = captchaService.getRandomQuestion(); // Obtenir une question aléatoire
        captchaQuestionLabel.setText(currentCaptchaQuestion.getQuestion()); // Afficher la question
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String captchaAnswer = captchaAnswerField.getText().trim(); // Récupérer la réponse CAPTCHA

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier la réponse du CAPTCHA
        if (!captchaService.checkAnswer(currentCaptchaQuestion, captchaAnswer)) {
            captchaErrorLabel.setText("Réponse CAPTCHA incorrecte.");
            return;
        }

        // Réinitialiser l'erreur CAPTCHA si la réponse est correcte
        captchaErrorLabel.setText("");

        User user = userService.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {

            // Vérification du bannissement
            if (user.getBanned_until() != null) {
                if (user.getBanned_until().isAfter(java.time.LocalDateTime.now())) {
                    errorLabel.setText("Vous êtes banni jusqu’au : " +
                            user.getBanned_until().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    return;
                } else {
                    // Bannissement expiré : le lever
                    user.setBanned_until(null);
                    userService.liftBan(user.getId());  // À implémenter si pas encore fait
                }
            }

            // Réactivation du compte désactivé
            if (!user.isStatut_compte()) {
                user.setStatut_compte(true);
                userService.update(user);  // Mise à jour en base
                Alert reactivated = new Alert(Alert.AlertType.INFORMATION);
                reactivated.setTitle("Compte réactivé");
                reactivated.setHeaderText(null);
                reactivated.setContentText("Votre compte a été réactivé automatiquement.");
                reactivated.showAndWait();
            }

            Session.setCurrentUser(user);
            verifierContratsExpires(user);
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

    @FXML
    private void redirectToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/AjouterUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleForgotPassword() {
        try {
            // Charger le fichier FXML de réinitialisation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/reinitialisermdp.fxml"));
            Parent root = loader.load();

            // Passer l'email saisi au contrôleur si nécessaire
            ReinitialiserMdpController controller = loader.getController();
            controller.setUserEmail(emailField.getText().trim());

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Réinitialiser le mot de passe");
            stage.setScene(new Scene(root));
            stage.show();

            // (optionnel) Fermer la fenêtre de login si tu veux
            // ((Stage) emailField.getScene().getWindow()).close();

        } catch (IOException e) {
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
    private void verifierContratsExpires(User user) {
        ContratService contratService = new ContratService();
        List<Contrat> contrats = contratService.findByUserId(user.getId());

        List<Contrat> contratsExpirés = contrats.stream()
                .filter(contrat -> contrat.getDatfinCont() != null && contrat.getDatfinCont().before(new Date()))
                .collect(Collectors.toList());

        if (!contratsExpirés.isEmpty()) {
            Platform.runLater(() -> {
                for (Contrat c : contratsExpirés) {
                    String centreNom = new CentreService().findById(c.getCentreId()).getNomCentre();

                    // Vérification et conversion si nécessaire de la date avant de la formater
                    String content = "Contrat #" + c.getId()
                            + " - Centre: " + centreNom;

                    if (c.getDatfinCont() instanceof Date) {
                        content += "\nFin: " + new SimpleDateFormat("dd/MM/yyyy").format(c.getDatfinCont());
                    } else {
                        // Si c.getDatfinCont() est un autre type (par exemple LocalDate)
                        content += "\nFin: " + c.getDatfinCont().toString();  // Utilise toString() si c'est un LocalDate
                    }

                    content += "\nVeuillez visiter votre espace de contrats et vérifier. \nMerci.";

                    // Créer une notification pour chaque contrat expiré
                    Notifications.create()
                            .title("Contrat expiré !")
                            .text(content)
                            .hideAfter(Duration.seconds(60))
                            .position(Pos.TOP_RIGHT)
                            .showWarning();
                }
            });
        }
    }

}
