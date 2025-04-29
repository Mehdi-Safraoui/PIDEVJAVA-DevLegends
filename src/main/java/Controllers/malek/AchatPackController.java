package Controllers.malek;

import Entities.malek.Pack;
import Services.malek.PackService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class AchatPackController {

    @FXML
    private Label nomPackLabel;

    @FXML
    private Label descriptPackLabel;

    @FXML
    private Label prixPackLabel;

    @FXML
    private Label dureePackLabel;

    @FXML
    private ImageView photoPackView;

    @FXML
    private Label discountCodeLabel;

    @FXML
    private Button generateCodeButton;

    @FXML
    private TextField codeInputField;

    @FXML
    private Button applyCodeButton;

    @FXML
    private Button payButton;

    private Pack currentPack;
    private double originalPrice;
    private boolean isDiscountApplied = false;

    public void setPack(Pack pack) {
        this.currentPack = pack;

        nomPackLabel.setText(pack.getNomPack());
        descriptPackLabel.setText(pack.getDescriptPack());
        prixPackLabel.setText(pack.getPrixPack() + " TND");
        originalPrice = pack.getPrixPack();
        dureePackLabel.setText("Durée : " + pack.getDureePack() + " jours");

        if (pack.getPhotoPack() != null && !pack.getPhotoPack().isEmpty()) {
            Image img = new Image("file:" + pack.getPhotoPack());
            photoPackView.setImage(img);
        }

        if (pack.getDiscountCode() != null && !pack.getDiscountCode().isEmpty()) {
            discountCodeLabel.setText("Code déjà généré : " + pack.getDiscountCode());
            generateCodeButton.setDisable(true);
        }
    }

    @FXML
    void onGenerateCodeClicked() {
        String generatedCode = generateRandomCode(8);
        discountCodeLabel.setText("Votre code promo : " + generatedCode);

        currentPack.setDiscountCode(generatedCode);
        currentPack.setUsed(false); // Le code n'est pas encore utilisé

        new PackService().update(currentPack);
        generateCodeButton.setDisable(true);
    }

    @FXML
    void onApplyCodeClicked() {
        String inputCode = codeInputField.getText().trim();
        if (inputCode.equals(currentPack.getDiscountCode()) && !currentPack.isUsed()) {
            if (!isDiscountApplied) {
                double reducedPrice = originalPrice * 0.9;
                prixPackLabel.setText(String.format("%.2f TND (réduction appliquée)", reducedPrice));
                isDiscountApplied = true;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Code invalide ou déjà utilisé !");
            alert.setContentText("Veuillez vérifier votre code de réduction.");
            alert.showAndWait();
        }
    }

    @FXML
    void onPayButtonClicked() {
        if (isDiscountApplied) {
            currentPack.setUsed(true);
            new PackService().update(currentPack);
        }

        try {
            // Charger la page de paiement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/malek/paymentPage.fxml"));
            AnchorPane root = loader.load();
            PaymentPageController paymentPageController = loader.getController();
            paymentPageController.setPack(currentPack);

            // Créer une nouvelle scène et afficher la page de paiement
            Scene paymentScene = new Scene(root);
            Stage paymentStage = new Stage();
            paymentStage.setTitle("Paiement");
            paymentStage.setScene(paymentScene);
            paymentStage.show();

            // Fermer la fenêtre actuelle si nécessaire
            ((Stage) payButton.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return code.toString();
    }
}
