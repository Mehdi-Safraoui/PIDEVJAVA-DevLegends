package Utils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AvatarUtils {

    public static String genererAvatar(String email) {
        int size = 128;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Générer une couleur unique pour le fond (ou peau)
        Color skinColor = getColorFromEmail(email);
        g2.setColor(skinColor);
        g2.fillOval(0, 0, size, size);

        // Paramètres pseudo-aléatoires
        int hash = Math.abs(email.hashCode());
        int eyeSize = 12 + (hash % 6);              // 12 à 18 px
        int eyeY = 40 + (hash % 5);                 // position verticale des yeux
        int smileWidth = 50 + (hash % 20);          // largeur du sourire
        int smileDepth = 15 + (hash % 10);          // profondeur du sourire

        // Yeux
        g2.setColor(Color.BLACK);
        g2.fillOval(35, eyeY, eyeSize, eyeSize);    // œil gauche
        g2.fillOval(85, eyeY, eyeSize, eyeSize);    // œil droit

        // Sourire
        g2.setStroke(new BasicStroke(3));
        g2.drawArc((size - smileWidth) / 2, 80, smileWidth, smileDepth, 0, -180);

        // Optionnel : lunettes ou cheveux stylisés
        if (hash % 3 == 0) {
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(30, eyeY + eyeSize / 2, 98, eyeY + eyeSize / 2); // lunettes
        }

        g2.dispose();

        // Sauvegarder l'image
        String fileName = "avatar_face_" + hash + ".png";
        String outputPath = "C:\\Users\\khoub\\OneDrive\\Bureau\\projet\\src\\main\\resources\\images\\avatars\\" + fileName;
        try {
            ImageIO.write(image, "png", new File(outputPath));
            return outputPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Color getColorFromEmail(String email) {
        int hash = email.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = (hash & 0x0000FF);
        return new Color(Math.abs(r) % 200 + 30, Math.abs(g) % 200 + 30, Math.abs(b) % 200 + 30);
    }
}

