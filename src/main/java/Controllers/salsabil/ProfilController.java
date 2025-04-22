package Controllers.salsabil;

import Entities.salsabil.Profil;
import Entities.salsabil.User;
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

public class ProfilController {

    @FXML
    private Label lblNom, lblPrenom, lblRole, lblAge, lblSpecialite;  // Ajout du Label pour la spécialité
    @FXML private ImageView profileImageView;
    @FXML private Button btnRetour;
    @FXML private Button btnModifier;
    private User user;

    public void setUser(User user) {
        this.user = user; // Enregistrer l'utilisateur

        lblNom.setText("Nom : " + user.getLast_name());
        lblPrenom.setText("Prénom : " + user.getFirst_name());
        lblRole.setText("Rôle : " + user.getUser_role());
        lblAge.setText("Âge : " + user.getUser_age());

        // Affichage de l'image de profil
        if (user.getUser_picture() != null && !user.getUser_picture().isEmpty()) {
            Image image = new Image("file:" + user.getUser_picture());
            profileImageView.setImage(image);
        }

        // Affichage de la spécialité si l'utilisateur est médecin
        if ("medecin".equalsIgnoreCase(user.getUser_role())) {
            lblSpecialite.setText("Spécialité : " + user.getDoc_specialty()); // Affichage de la spécialité
        } else {
            lblSpecialite.setText(""); // Masquer la spécialité si l'utilisateur n'est pas médecin
        }

        btnRetour.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/AjouterUser.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
        });
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/AjouterUser.fxml"));
            Parent ajouterUserRoot = loader.load();

            Stage stage = (Stage) lblNom.getScene().getWindow();
            stage.setScene(new Scene(ajouterUserRoot));
            stage.setTitle("Ajout d'un utilisateur");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Profil getProfilActuel() {
        if (user != null) {
            return new Profil(user);
        }
        return null;
    }

    @FXML
    private void handleModifier() {
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

}
