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
    private void goToCentre(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCentre.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherPack.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherContrat.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterContrat.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Contrats");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
