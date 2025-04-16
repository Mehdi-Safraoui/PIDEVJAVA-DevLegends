package Controllers;

import Entities.Profil;
import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ModifierProfilController {

    @FXML private TextField txtNom, txtPrenom, txtAge, txtRole, txtSpecialite, txtTel, txtAdresse;
    @FXML private Button btnEnregistrer, btnAnnuler;
    @FXML private ComboBox<String> cbSpecialite;
    @FXML private HBox specialiteBox;
    @FXML private ImageView imageView; // Ajouté pour afficher l'image de profil
    @FXML private ImageView logoImage;

    private Profil profil;
    private User currentUser;
    private String newImagePath; // Variable pour stocker le chemin de la nouvelle image

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle();
        clip.setRadius(25); // half of 120 width/height
        clip.setCenterX(25);
        clip.setCenterY(25);
        logoImage.setClip(clip);
        // Ajouter les spécialités à la ComboBox
        cbSpecialite.getItems().addAll(
                "Psychiatre", "Psychothérapeute", "Psychologue"
        );
        if (cbSpecialite == null) {
            System.err.println("txtSpecialite est nul !");
        }
    }

    public void setProfil(Profil profil) {
        if (profil != null) {
            this.profil = profil;

            txtPrenom.setText(profil.getFirst_name());
            txtNom.setText(profil.getLast_name());
            txtAge.setText(String.valueOf(profil.getUser_age()));
            txtRole.setText(profil.getUser_role());

            // Affichage de l'image de profil si elle existe
            if (profil.getUser_picture() != null) {
                imageView.setImage(new Image("file:" + profil.getUser_picture()));
            }

            // Afficher la spécialité si le rôle est médecin
            if ("medecin".equalsIgnoreCase(profil.getUser_role())) {
                specialiteBox.setVisible(true);
                cbSpecialite.setValue(profil.getDoc_specialty());
            } else {
                specialiteBox.setVisible(false);
            }
        } else {
            System.err.println("Profil est nul !");
        }
    }


    public void setUser(User user) {
        this.currentUser = user;

        txtNom.setText(user.getLast_name());
        txtPrenom.setText(user.getFirst_name());
        txtAge.setText(String.valueOf(user.getUser_age()));
        if (profil != null && "medecin".equalsIgnoreCase(profil.getUser_role())) {
            txtSpecialite.setText(user.getDoc_specialty());
        }
        txtAdresse.setText(user.getAddress());
        txtTel.setText(user.getNum_tel());
        txtRole.setText(user.getUser_role());
    }

    @FXML
    private void handleEnregistrer() {
        if (currentUser != null) {
            if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtAge.getText().isEmpty()) {
                showConfirmation("Tous les champs obligatoires doivent être remplis.");
                return;
            }

            try {
                // ➕ Mettre à jour l'objet currentUser avec les champs du formulaire
                currentUser.setLast_name(txtNom.getText());
                currentUser.setFirst_name(txtPrenom.getText());
                currentUser.setUser_age(Integer.parseInt(txtAge.getText()));
                currentUser.setDoc_specialty(txtSpecialite.getText());
                currentUser.setAddress(txtAdresse.getText());
                currentUser.setNum_tel(txtTel.getText());
                currentUser.setUser_role(txtRole.getText());

                if (newImagePath != null && !newImagePath.isEmpty()) {
                    currentUser.setUser_picture(newImagePath);
                }

                UserService userService = new UserService();
                userService.update(currentUser); // ✅ ça met à jour aussi le profil

                showConfirmation("Profil mis à jour avec succès.");
                retourAuProfil();

            } catch (NumberFormatException e) {
                showConfirmation("L'âge doit être un nombre valide.");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        retourAuProfil();
    }



    private void retourAuProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profil.fxml"));
            Parent root = loader.load();

            ProfilController controller = loader.getController();
            if (currentUser != null) {
                controller.setUser(currentUser);
            } else {
                System.err.println("currentUser est null !");
            }

            Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil Utilisateur");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void changerImage(ActionEvent actionEvent) {
        // Créer un FileChooser pour sélectionner l'image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Mettre à jour l'image de profil
            newImagePath = file.getAbsolutePath();
            Image image = new Image("file:" + newImagePath);
            imageView.setImage(image);  // Afficher la nouvelle image dans le ImageView
        }
    }
}
