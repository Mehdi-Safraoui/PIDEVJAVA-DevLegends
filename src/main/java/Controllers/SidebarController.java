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
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamations.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherAvis.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/ProduitAdmin.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCommande.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterProduit.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterCategorie.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCategorie.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Catégories");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
