package Controllers;

import Entities.Evenement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipationPopup {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ParticipationPopup.class);
    @FXML
    private TextField emailField;

    private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }


    @FXML
    private void handleSend() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hmem.ali2003@gmail.com", "edub qvgc clfe seto");
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("hmem.ali2003@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailField.getText()));
            msg.setSubject("Confirmation de participation");

            // Message personnalisé avec titre et lieu de l'événement
            String messageTexte = String.format(
                    "Merci pour votre participation à l'événement \"%s\" qui aura lieu à %s.",
                    evenement.getTitreEvent(), evenement.getLieuEvent()
            );
            msg.setText(messageTexte);

            Transport.send(msg);
            showAlert("Succès", "Email de participation envoyé !");
        } catch (MessagingException e) {
            e.printStackTrace();
            showAlert("Erreur", "L'envoi de l'email a échoué.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
