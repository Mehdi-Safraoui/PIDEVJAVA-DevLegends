package Controllers.malek;

import Entities.malek.Centre;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CentreCardController {

    @FXML private ImageView imageCentre;
    @FXML private Label nomCentre;
    @FXML private Label adresse;
    @FXML private Label telephone;
    @FXML private Label email;
    @FXML private Label specialite;
    @FXML private Label capacite;

    public void setCentre(Centre centre) {
        nomCentre.setText(centre.getNomCentre());
        adresse.setText("Adresse: " + centre.getAdresseCentre());
        telephone.setText("Téléphone: " + centre.getTelCentre());
        email.setText("Email: " + centre.getEmailCentre());
        specialite.setText("Spécialité: " + centre.getSpecialiteCentre());
        capacite.setText("Capacité: " + centre.getCapaciteCentre()); // ✅ adapté au nom correct

        try {
            if (centre.getPhotoCentre() != null && !centre.getPhotoCentre().isEmpty()) {
                imageCentre.setImage(new Image("file:" + centre.getPhotoCentre())); // ✅ adapté au nom correct
            } else {
                imageCentre.setImage(new Image("/images/default_centre.png")); // image par défaut
            }
        } catch (Exception e) {
            imageCentre.setImage(new Image("/images/default_centre.png"));
        }
    }
}
