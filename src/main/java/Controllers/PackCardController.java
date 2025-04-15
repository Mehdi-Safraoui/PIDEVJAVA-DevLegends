package Controllers;

import Entities.Pack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PackCardController {

    @FXML private ImageView imagePack;
    @FXML private Label nomPack;
    @FXML private Label description;
    @FXML private Label prix;
    @FXML private Label duree;
    @FXML private Label discountCode;
    @FXML private Label isUsed;

    public void setPack(Pack pack) {
        nomPack.setText(pack.getNomPack());
        description.setText("Description: " + pack.getDescriptPack());
        prix.setText("Prix: " + pack.getPrixPack() + " €");
        duree.setText("Durée: " + pack.getDureePack() + " jours");
        discountCode.setText("Code promo: " + (pack.getDiscountCode() != null ? pack.getDiscountCode() : "Aucun"));
        isUsed.setText("Utilisé: " + (pack.isUsed() ? "Oui" : "Non"));


        try {
            if (pack.getPhotoPack() != null && !pack.getPhotoPack().isEmpty()) {
                imagePack.setImage(new Image("file:" + pack.getPhotoPack())); // ✅ adapté au nom correct
            } else {
                imagePack.setImage(new Image("/images/default_pack.png")); // image par défaut
            }
        } catch (Exception e) {
            imagePack.setImage(new Image("/images/default_pack.png"));
        }
    }
}
