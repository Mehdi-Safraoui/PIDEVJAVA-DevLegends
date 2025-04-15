package Controllers;

import Entities.Contrat;
import Services.CentreService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.SimpleDateFormat;

public class ContratCardController {

    @FXML private ImageView imageContrat;
    @FXML private Label labelId;
    @FXML private Label labelCentre;
    @FXML private Label labelDateDebut;
    @FXML private Label labelDateFin;
    @FXML private Label labelModePaiement;
    @FXML private Label labelRenouvellement;

    private CentreService centreService = new CentreService();

    private Contrat contrat;

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        labelId.setText("Contrat #" + contrat.getId());
        String nomCentre = centreService.findById(contrat.getCentreId()).getNomCentre();
        labelCentre.setText("Centre: " + nomCentre);
        labelDateDebut.setText("Début: " + dateFormat.format(contrat.getDatdebCont()));
        labelDateFin.setText("Fin: " + dateFormat.format(contrat.getDatfinCont()));
        labelModePaiement.setText("Paiement: " + contrat.getModpaimentCont());
        labelRenouvellement.setText("Renouv. auto: " + (contrat.isRenouvAutoCont() ? "Oui" : "Non"));

        try {
            imageContrat.setImage(new Image("/images/contrat_icon.png"));
        } catch (Exception e) {
            imageContrat.setImage(new Image("/images/default_icon.png"));
        }
    }

    @FXML
    private void handleModifier() {
        System.out.println("Modifier le contrat : " + contrat.getId());
        // Ici tu peux ouvrir une nouvelle fenêtre de modification avec ce contrat
    }

    @FXML
    private void handleSupprimer() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer ce contrat ?");
        confirm.setContentText("Cette action est irréversible.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.out.println("Suppression du contrat : " + contrat.getId());
            // Ici tu peux appeler ton service pour supprimer le contrat
            // contratService.delete(contrat.getId());
        }
    }
}
