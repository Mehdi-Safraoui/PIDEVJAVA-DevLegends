package Utils;

import jakarta.mail.Session; // <- celle de Jakarta Mail

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class MailUtil {
    private static final String USERNAME = "salsabilhafsi2@gmail.com";  // ton email Gmail
    private static final String PASSWORD = "vbia tirw smvu wcaq"; // mot de passe d'application

    private static Session getMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    public static void envoyerMail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(getMailSession());
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("✅ Mail envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de l'envoi du mail : " + e.getMessage());
        }
    }
}
