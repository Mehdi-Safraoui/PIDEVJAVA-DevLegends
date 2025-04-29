package Controllers;

import Utils.NotificationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML
    private void goToReclamations(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/mehdi/AfficherReclamations.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réclamations");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAvis(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/mehdi/AfficherAvis.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Avis");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAddAvis(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/mehdi/AjouterAvis.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Avis");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToProduit(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/ProduitAdmin.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Produits");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToCommande(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/AfficherCommande.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Commandes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAjouterCommande(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/AjouterCommande.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter des Commandes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAjouterProduit(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/AjouterProduit.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Produit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAjouterCategorie(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/AjouterCategorie.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Catégorie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAfficherCategorie(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/AfficherCategorie.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Catégories");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToProduitFront(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fatma/ProduitFront.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Produits");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void goToCentre(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AfficherCentre.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Centres");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToPack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AfficherPack.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Packs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToContrat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/malek/AfficherContrat.fxml"));
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
    private void goToUsers(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/salsabil/AfficherUsers.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Utilisateurs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goToLandingpage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/maya/LandingPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Utilisateurs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToFormation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ali/AjoutFormation.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Formations");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToEvent(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ali/AjoutEvent.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Evenement");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToArticles() {
        loadScene("/maya/AjouterArticle.fxml", "Articles de Conseil");
    }

    @FXML
    private void goToConsultations() {
        loadScene("/maya/AfficherConsultation.fxml", "Consultations");
    }

    @FXML
    private void goToQuestion() {
        loadScene("/maya/AjouterQuestion.fxml", "Questions");
    }

    @FXML
    private void goToReponse() {
        loadScene("/maya/AjouterReponse.fxml", "Réponses");
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

            // Fermer la page actuelle si souhaité :
            // ((Stage) titreButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListArticles() {
        try {
            // Charger le fichier FXML de la liste des articles
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/ListArticle.fxml"));
            AnchorPane root = loader.load();

            // Créer une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (Stage) pour afficher la scène
            Stage stage = new Stage();
            stage.setTitle("Liste des Articles");
            stage.setScene(scene);

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
