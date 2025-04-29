package Controllers;

import Controllers.salsabil.ProfilController;
import Entities.salsabil.User;
import Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavbarController {

    @FXML
    private void goToLandingPage(ActionEvent event) {
        loadScene("/maya/LandingPage.fxml", "Accueil", event);
    }

    @FXML
    private void goToProduitsFront(ActionEvent event) {
        loadScene("/fatma/ProduitFront.fxml", "Nos Produits", event);
    }

    @FXML
    private void goToServices(ActionEvent event) {
        loadScene("/malek/ServicesFront.fxml", "Nos Services", event);
    }

    @FXML
    private void goToEventsFront(ActionEvent event) {
        loadScene("/ali/EventsFront.fxml", "Événements", event);
    }

    @FXML
    private void goToFormationsFront(ActionEvent event) {
        loadScene("/ali/FormationsFront.fxml", "Formations", event);
    }

    @FXML
    private void goToContratsFront(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AfficherContratFront.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Contrats");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAddContrat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AjouterContrat.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Contrats");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToPacksFront(ActionEvent event) {
        loadScene("/malek/AfficherPackFront.fxml", "Nos Centres", event);
    }

    @FXML
    private void goToCentresFront(ActionEvent event) {
        loadScene("/malek/AfficherCentreFront.fxml", "Nos Centres", event);
    }

    @FXML
    private void goToArticlesFront(ActionEvent event) {
        loadScene("/maya/ArticlesFront.fxml", "Articles", event);
    }

    @FXML
    private void goToProfil(ActionEvent event) {
        User currentUser = Session.getCurrentUser();

        if (currentUser != null) {
            try {
                // Charger la vue du profil
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/ProfilUser.fxml"));
                Parent root = loader.load();

                // Passer l'utilisateur au contrôleur du profil
                ProfilController profilController = loader.getController();
                profilController.setUser(currentUser);

                // Charger la scène du profil
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Mon Profil");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    @FXML
    private void goToCart(ActionEvent event) {
        loadScene("/fatma/Panier.fxml", "Panier", event);
    }

    private void loadScene(String fxmlPath, String title, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAvisFront(ActionEvent actionEvent) {
    }

    public void goToReclamationFront(ActionEvent actionEvent) {
    }

    public void goToArticles(ActionEvent actionEvent) {
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Nettoyer la session
            Session.clear();

            // Fermer la fenêtre actuelle
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

            // Charger l'écran de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/salsabil/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}