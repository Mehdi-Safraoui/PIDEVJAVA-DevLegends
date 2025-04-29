package Services.salsabil;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Random;

public class SmsService {

    // Ton ACCOUNT_SID et AUTH_TOKEN Twilio
    private static final String ACCOUNT_SID = "AC8143edf86b523602dafda92cf882646b";
    private static final String AUTH_TOKEN = "3eaed5f138e4199ffd5845a17e2e55b0";

    // Ton numéro Twilio vérifié (exemple : "+1234567890")
    private static final String TWILIO_PHONE_NUMBER = "+12342595374";

    static {
        // Initialisation de Twilio une seule fois
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Génère un code à 6 chiffres
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // Envoie le SMS
    public static boolean sendVerificationCode(String phoneNumber, String code) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    "Votre code de vérification est : " + code
            ).create();

            System.out.println("Message envoyé, SID = " + message.getSid());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur d'envoi : " + e.getMessage());
            return false;
        }
    }
}
