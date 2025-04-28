package Controllers.salsabil;

import Entities.salsabil.User;
import Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label lblTotalUsers;

    @FXML
    private Label lblTotalDoctors;

    @FXML
    private Label lblTotalPatients;
    @FXML private Label dashboardTitle;
    @FXML private ImageView roleIcon;
    private User user;
    @FXML private Button btnModifier;

    @FXML
    public void initialize() {
        // Exemple : tu pourrais charger ces stats depuis la base
//        lblTotalUsers.setText("120");
//        lblTotalDoctors.setText("35");
//        lblTotalPatients.setText("85");
    }

    @FXML
    private void handleManageUsers() {
        System.out.println("🔧 Gérer utilisateurs");
        // Charger page utilisateurs
    }
    public void setUser(User user) {
        this.user = user;

        dashboardTitle.setText("Bienvenue " + user.getFirst_name() + " " + user.getLast_name());

        setRoleIcon(user.getUser_role());
    }
    @FXML
    private void handleAfficherProfil(ActionEvent event) {
        // Code pour afficher les infos du profil de l'utilisateur courant
        // Par exemple : ouvrir une nouvelle fenêtre ou basculer vers une autre scène
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Profil.fxml"));
            Parent root = loader.load();

            // Optionnel : passer l'utilisateur courant au contrôleur AfficherProfilController
            ProfilController controller = loader.getController();
            controller.setUser(Session.getCurrentUser()); // si tu as un champ currentUser

            Stage stage = new Stage();
            stage.setTitle("Profil Administrateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRoleIcon(String role) {
        String imagePath;

        if (role.toLowerCase().equals("admin")) {
            imagePath = "/images/admin_icon.png";
        } else {
            imagePath = "/images/user_icon.jpg";
        }

        Image icon = new Image(getClass().getResource(imagePath).toExternalForm());
        roleIcon.setImage(icon);
    }

    @FXML
    private void handleShowUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/AfficherUsers.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Liste des utilisateurs");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/ModifierProfil.fxml"));
            Parent root = loader.load();

            ModifierProfilController controller = loader.getController();
            controller.setUser(user); // ➕ On envoie l'utilisateur au lieu d'un Profil

            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Profil");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageCentres() {
        System.out.println("🏥 Gérer centres");
        // Charger page centres
    }

    @FXML
    private void handleLogout() {
        System.out.println("🚪 Déconnexion");

        // 1. Supprimer l'utilisateur de la session
        Session.clear();

        // 2. Fermer la fenêtre actuelle
        Stage currentStage = (Stage) roleIcon.getScene().getWindow(); // ou n'importe quel composant de la scène
        currentStage.close();

        // 3. Ouvrir la fenêtre de login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Connexion");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
