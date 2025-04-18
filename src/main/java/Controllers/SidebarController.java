package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


}
