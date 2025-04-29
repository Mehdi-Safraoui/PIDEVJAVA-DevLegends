package Controllers.fatma;

import Entities.fatma.Commande;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class FactureController {

    @FXML private Label labelNomClient, labelEmail, labelAdresse, labelTotal, labelDate;

    private Commande commande;

    public void setCommande(Commande commande) {
        this.commande = commande;

        // Vérification des données avant affichage
        labelNomClient.setText("Nom : " + (commande.getNomClient() != null ? commande.getNomClient() : "Non défini"));
        labelEmail.setText("Email : " + (commande.getAdresseEmail() != null ? commande.getAdresseEmail() : "Non défini"));
        labelAdresse.setText("Adresse : " + (commande.getAdresse() != null ? commande.getAdresse() : "Non définie"));
        labelTotal.setText("Total : " + commande.getTotalCom() + " EUR");
        labelDate.setText("Date : " + (commande.getDateCommande() != null ? commande.getDateCommande().toString() : "Non définie"));
    }

    @FXML
    public void telechargerPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la facture");
        fileChooser.setInitialFileName("facture_" + commande.getId() + ".pdf");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDPageContentStream content = new PDPageContentStream(document, page);

                float margin = 50;
                float y = 750;

                // En-tête INNERBLOOM
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 20);
                content.setNonStrokingColor(0, 128, 0); // Vert
                content.newLineAtOffset(margin, y);
                content.showText("INNERBLOOM");
                content.endText();

                y -= 20;
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.setNonStrokingColor(0, 0, 0);
                content.newLineAtOffset(margin, y);
                content.showText("innerbloom@gmail.com | +21655889966 | Al Ghazela, Ariana");
                content.endText();

                // Titre Facture
                y -= 40;
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.setNonStrokingColor(0, 128, 0);
                content.newLineAtOffset(margin, y);
                content.showText("Facture");
                content.endText();

                // Ligne verte
                y -= 10;
                content.setLineWidth(1);
                content.setStrokingColor(0, 128, 0);
                content.moveTo(margin, y);
                content.lineTo(550, y);
                content.stroke();

                // Rectangle encadré pour les infos client
                y -= 30;
                content.setLineWidth(1);
                content.setStrokingColor(0, 128, 0);
                content.moveTo(margin - 5, y - 5);
                content.lineTo(margin + 500, y - 5);
                content.lineTo(margin + 500, y - 120);
                content.lineTo(margin - 5, y - 120);
                content.closePath();
                content.stroke();

                // Texte client
                float textX = margin;
                float textY = y - 20;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.setNonStrokingColor(0, 0, 0);
                content.newLineAtOffset(textX, textY);
                content.showText("Nom : " + (commande.getNomClient() != null ? commande.getNomClient() : "Non défini"));
                content.newLineAtOffset(0, -20);
                content.showText("Email : " + (commande.getAdresseEmail() != null ? commande.getAdresseEmail() : "Non défini"));
                content.newLineAtOffset(0, -20);
                content.showText("Adresse : " + (commande.getAdresse() != null ? commande.getAdresse() : "Non définie"));
                content.newLineAtOffset(0, -20);
                content.showText("Date : " + (commande.getDateCommande() != null ? commande.getDateCommande() : "Non définie"));
                content.newLineAtOffset(0, -20);
                content.showText("Téléphone : +216" + commande.getNumTelephone());
                content.endText();

                // Total
                y -= 160;
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 14);
                content.setNonStrokingColor(0, 128, 0);
                content.newLineAtOffset(margin, y);
                content.showText("Total Commande : " + commande.getTotalCom() + " EUR");
                content.endText();

                // Message de remerciement
                y = 300;
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                content.setNonStrokingColor(0, 0, 0);
                content.newLineAtOffset(margin, y);
                content.showText("Merci pour votre fidélité. INNERBLOOM vous remercie pour votre confiance !");
                content.endText();

                content.close();
                document.save(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("✅ Facture générée avec succès !");
                alert.showAndWait();

                System.out.println("✅ Facture générée avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
