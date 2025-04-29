package Services.mehdi;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;

public class EmailService {
    private static final String SENDGRID_API_KEY = "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";
    private static final String FROM_EMAIL = "mahdisafraoui01@gmail.com";
    private static final String FROM_NAME = "TEST_SENDER";

    public static void sendReclamationProcessedEmail(String toEmail, String userName, int reclamationId) {
        Email from = new Email(FROM_EMAIL, FROM_NAME);
        Email to = new Email(toEmail);
        String subject = "Votre réclamation #" + reclamationId + " a été traitée";

        String htmlContent = "<!DOCTYPE html>" +
                "<html><body>" +
                "<h2>Cher " + userName + ",</h2>" +
                "<p>Nous vous informons que votre réclamation a été traitée avec succès.</p>" +
                "<p>Merci pour votre confiance.</p>" +
                "<br/><p>Cordialement,</p>" +
                "<p>L'équipe InnerBloom</p>" +
                "</body></html>";

        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Email envoyé. Statut: " + response.getStatusCode());
        } catch (IOException ex) {
            System.err.println("Erreur d'envoi d'email: " + ex.getMessage());
        }
    }
}