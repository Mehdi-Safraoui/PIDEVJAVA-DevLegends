package Controllers.maya;

import Services.maya.QuizService;
import Services.maya.YouTubeAPI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import Services.maya.UnsplashAPI; // Important : Assure-toi que cette classe existe

import java.io.IOException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javafx.embed.swing.SwingFXUtils;

import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.awt.Color;

public class ResultatQuizController {

    @FXML
    private Label etatMentalLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private ImageView imageViewEtatMental;

    private String etatMental;

    @FXML
    private Label descriptionLabel;

    @FXML private Button btnExporterPDF;
    private String imageUrl;
    private int score;
    private QuizService quizService = new QuizService();

//    public void setResult(String etatMental, int score) {
//        // afficher le texte de l'état mental
//        etatMentalLabel.setText("État mental : " + etatMental);
//        // afficher le score
//        scoreLabel.setText("Score obtenu : " + score);
//
//        // C'est ici que l'on fait la requête HTTP vers Unsplash
//        afficherImageDepuisUnsplash(etatMental);
//
////        // obtenir et afficher l'image
////        String url = quizService.generateImageUrl(etatMental);
////        Image img = new Image(url, true);
////        imageViewEtatMental.setImage(img);
//    }

    public void setResult(String etatMental, int score) {
        this.etatMental = etatMental; // <<< AJOUT !!
        this.score = score;           // <<< AJOUT !!

        etatMentalLabel.setText("État mental : " + etatMental);
        scoreLabel.setText("Score obtenu : " + score);

        afficherImageDepuisUnsplash(etatMental);
    }




//    private void afficherImageDepuisUnsplash(String etatMental) {
//        new Thread(() -> {
//            try {
//                String imageUrl = UnsplashAPI.getImageUrlForEtatMental(etatMental); // ← méthode personnalisée !
//                Image image = new Image(imageUrl, true);
//                Platform.runLater(() -> imageViewEtatMental.setImage(image));
//            } catch (Exception e) {
//                e.printStackTrace();
//                Platform.runLater(() -> {
//                    Image defaultImage = new Image("/mental.jpg");
//                    imageViewEtatMental.setImage(defaultImage);
//                });
//            }
//        }).start();
//    }

    private void afficherImageDepuisUnsplash(String etatMental) {
        new Thread(() -> {
            try {
                imageUrl = UnsplashAPI.getImageUrlForEtatMental(etatMental); // <<< on met dans imageUrl
                Image image = new Image(imageUrl, true);
                Platform.runLater(() -> imageViewEtatMental.setImage(image));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Image defaultImage = new Image("/mental.jpg");
                    imageViewEtatMental.setImage(defaultImage);
                });
            }
        }).start();
    }

    @FXML
    private void handleRetourAccueil() {
        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleRecommencerQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maya/Quiz.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) scoreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exporterPDF() {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(50, 700);
                content.showText("Résultat du Quiz");
                content.newLineAtOffset(0, -25);
                content.showText("État mental : " + etatMental);
                content.newLineAtOffset(0, -20);
                content.showText("Score obtenu : " + score);
                content.endText();

                if (imageUrl != null) {
                    BufferedImage bimg = ImageIO.read(new URL(imageUrl));
                    PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bimg);
                    content.drawImage(pdImage, 50, 400, 200, 150);
                }
            }
            String output = "resultat_quiz_" + System.currentTimeMillis() + ".pdf";
            doc.save(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @FXML
//    private void handleExporterPDF() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Enregistrer le PDF");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
//        fileChooser.setInitialFileName("Resultat_Quiz.pdf");
//
//        Stage stage = (Stage) scoreLabel.getScene().getWindow();
//        File selectedFile = fileChooser.showSaveDialog(stage);
//
//        if (selectedFile != null) {
//            try (PDDocument document = new PDDocument()) {
//                PDPage page = new PDPage();
//                document.addPage(page);
//
//                PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//                // Texte titre
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
//                contentStream.newLineAtOffset(100, 700);
//                contentStream.showText("Résultat du Quiz Mental");
//                contentStream.endText();
//
//                // Texte état mental
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, 18);
//                contentStream.newLineAtOffset(100, 650);
//                contentStream.showText(etatMentalLabel.getText());
//                contentStream.endText();
//
//                // Texte score
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, 18);
//                contentStream.newLineAtOffset(100, 620);
//                contentStream.showText(scoreLabel.getText());
//                contentStream.endText();
//
//                contentStream.close();
//
//                // Ajouter l'image Unsplash (si présente)
//                if (imageViewEtatMental.getImage() != null) {
//                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageViewEtatMental.getImage(), null);
//                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
//
//                    try (PDPageContentStream imageContentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
//                        imageContentStream.drawImage(pdImage, 100, 300, 300, 300);
//                    }
//                }
//
//                document.save(selectedFile);
//                System.out.println("✅ PDF avec image exporté : " + selectedFile.getAbsolutePath());
//
//                // 🔔 Alert de succès
//                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Exportation Réussie");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Le fichier PDF a été exporté avec succès !");
//                    alert.showAndWait();
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                // 🔔 Alert d'erreur
//                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Erreur");
//                    alert.setHeaderText("Échec de l'exportation");
//                    alert.setContentText("Une erreur s'est produite lors de l'exportation du PDF.");
//                    alert.showAndWait();
//                });
//            }
//        }
//    }

//    @FXML
//    private void handleExporterPDF() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Enregistrer le PDF");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
//        fileChooser.setInitialFileName("Resultat_Quiz.pdf");
//
//        Stage stage = (Stage) scoreLabel.getScene().getWindow();
//        File selectedFile = fileChooser.showSaveDialog(stage);
//
//        if (selectedFile != null) {
//            try (PDDocument document = new PDDocument()) {
//
//                // ===== PAGE DE COUVERTURE =====
//                PDPage coverPage = new PDPage(PDRectangle.A4);
//                document.addPage(coverPage);
//                try (PDPageContentStream coverStream = new PDPageContentStream(document, coverPage)) {
//
//                    // Fond blanc
//                    coverStream.setNonStrokingColor(255, 255, 255);
//                    coverStream.addRect(0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
//                    coverStream.fill();
//
//                    // Logo
//                    BufferedImage logoBuffered = ImageIO.read(new File("C:/Users/Lenovo/IdeaProjects/PIDEVJAVA-DevLegends/src/main/resources/images/logo.png"));
//                    PDImageXObject logo = LosslessFactory.createFromImage(document, logoBuffered);
//                    coverStream.drawImage(logo, 180, 500, 250, 200);
//
//                    // Bandeau vert en haut
//                    coverStream.setNonStrokingColor(34, 139, 34);
//                    coverStream.addRect(0, 750, PDRectangle.A4.getWidth(), 70);
//                    coverStream.fill();
//
//                    // Titre principal
//                    coverStream.beginText();
//                    coverStream.setFont(PDType1Font.HELVETICA_BOLD, 26);
//                    coverStream.setNonStrokingColor(255, 255, 255); // blanc
//                    coverStream.newLineAtOffset(140, 775);
//                    coverStream.showText("🧠 Résultats de votre Quiz Mental");
//                    coverStream.endText();
//
//                    // Date en bas
//                    coverStream.beginText();
//                    coverStream.setFont(PDType1Font.HELVETICA, 12);
//                    coverStream.setNonStrokingColor(100, 100, 100);
//                    coverStream.newLineAtOffset(50, 50);
//                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
//                    coverStream.showText("Date : " + date);
//                    coverStream.endText();
//                }
//
//                // ===== PAGE RESULTATS =====
//                PDPage resultsPage = new PDPage(PDRectangle.A4);
//                document.addPage(resultsPage);
//                try (PDPageContentStream contentStream = new PDPageContentStream(document, resultsPage)) {
//
//                    // Fond général blanc
//                    contentStream.setNonStrokingColor(255, 255, 255);
//                    contentStream.addRect(0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
//                    contentStream.fill();
//
//                    // Bandeau vert
//                    contentStream.setNonStrokingColor(34, 139, 34);
//                    contentStream.addRect(0, 720, PDRectangle.A4.getWidth(), 50);
//                    contentStream.fill();
//
//                    // Titre Résultats
//                    contentStream.beginText();
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
//                    contentStream.setNonStrokingColor(255, 255, 255); // blanc
//                    contentStream.newLineAtOffset(200, 735);
//                    contentStream.showText("🌿 Résultats détaillés");
//                    contentStream.endText();
//
//                    // Petit rectangle vert clair pour le score
//                    contentStream.setNonStrokingColor(200, 255, 200);
//                    contentStream.addRect(50, 640, 500, 50);
//                    contentStream.fill();
//
//                    // Score
//                    contentStream.beginText();
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
//                    contentStream.setNonStrokingColor(0, 0, 0);
//                    contentStream.newLineAtOffset(60, 660);
//                    contentStream.showText(scoreLabel.getText());
//                    contentStream.endText();
//
//                    // Etat mental (petit encadré)
//                    contentStream.setNonStrokingColor(220, 255, 220);
//                    contentStream.addRect(50, 580, 500, 50);
//                    contentStream.fill();
//
//                    contentStream.beginText();
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
//                    contentStream.setNonStrokingColor(0, 0, 0);
//                    contentStream.newLineAtOffset(60, 600);
//                    contentStream.showText(etatMentalLabel.getText());
//                    contentStream.endText();
//
//                    // Ligne de séparation
//                    contentStream.setStrokingColor(34, 139, 34);
//                    contentStream.setLineWidth(2);
//                    contentStream.moveTo(50, 560);
//                    contentStream.lineTo(550, 560);
//                    contentStream.stroke();
//
//                    // Description mentale
//                    String description = generateDescription(etatMentalLabel.getText().replace("État mental : ", ""));
//                    addMultilineText(contentStream, description, 50, 530, 14, 18);
//
//                    // Image d'état mental (positionnée proprement)
//                    if (imageViewEtatMental.getImage() != null) {
//                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageViewEtatMental.getImage(), null);
//                        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
//                        contentStream.drawImage(pdImage, 300, 330, 200, 120);
//                    }
//
//                    // Citation
//                    String citation = generateCitation(etatMentalLabel.getText().replace("État mental : ", ""));
//                    addMultilineText(contentStream, "« " + citation + " »", 50, 300, 12, 16);
//
//                    // Signature
//                    contentStream.beginText();
//                    contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
//                    contentStream.setNonStrokingColor(120, 120, 120);
//                    contentStream.newLineAtOffset(400, 30);
//                    contentStream.showText("Powered by DevLegends ©");
//                    contentStream.endText();
//                }
//
//                document.save(selectedFile);
//                System.out.println("✅ PDF EXPORTÉ avec nouveau design : " + selectedFile.getAbsolutePath());
//
//                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Exportation Réussie");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Votre PDF stylisé a été généré avec succès ! 🌟");
//                    alert.showAndWait();
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//
//
//    private void addMultilineText(PDPageContentStream contentStream, String text, float x, float y, int fontSize, int leading) throws IOException {
//        contentStream.beginText();
//        contentStream.setFont(PDType1Font.HELVETICA, fontSize);
//        contentStream.newLineAtOffset(x, y);
//        for (String line : text.split("\n")) {
//            contentStream.showText(line);
//            contentStream.newLineAtOffset(0, -leading);
//        }
//        contentStream.endText();
//    }
//
//    private String generateDescription(String etatMental) {
//        if (etatMental == null) return "Merci de votre participation à ce test.";
//
//        switch (etatMental) {
//            case "Bonne santé mentale":
//                return "Félicitations ! 🌟\nVotre santé mentale est excellente.\nContinuez à entretenir cet équilibre positif.";
//            case "Légère fatigue émotionnelle":
//                return "Prenez soin de vous. 🌱\nUne pause ou un moment de détente pourrait vous faire du bien.";
//            case "Signes d’anxiété ou de stress":
//                return "Attention. ⚡\nDes signes de stress sont présents. Pensez à adopter des techniques de relaxation ou en parler.";
//            case "État dépressif modéré":
//                return "Soyez vigilant. 🌧️\nN'ignorez pas ces signes.\nCherchez du soutien et exprimez ce que vous ressentez.";
//            case "État très préoccupant":
//                return "Priorité absolue. 🚨\nVotre état nécessite une attention immédiate.\nConsultez un professionnel pour obtenir de l'aide.";
//            default:
//                return "Merci pour votre participation.";
//        }
//    }
//
//    private String generateCitation(String etatMental) {
//        switch (etatMental) {
//            case "Bonne santé mentale":
//                return "Le bonheur n'est réel que lorsqu'il est partagé.";
//            case "Légère fatigue émotionnelle":
//                return "Le repos est le premier pas vers la guérison.";
//            case "Signes d’anxiété ou de stress":
//                return "Ne laisse pas le stress voler ta lumière intérieure.";
//            case "État dépressif modéré":
//                return "Parler est la première étape vers la guérison.";
//            case "État très préoccupant":
//                return "Il n'y a pas de honte à demander de l'aide. Le courage commence par un simple mot.";
//            default:
//                return "La santé mentale est tout aussi importante que la santé physique.";
//        }
//    }

    // Ajoutez cette méthode pour dessiner des rectangles arrondis
    private void drawRoundedRect(PDPageContentStream stream, float x, float y, float width,
                                 float height, float radius, Color color) throws IOException {
        stream.setNonStrokingColor(color);

        // Dessiner les 4 coins arrondis
        stream.moveTo(x + radius, y);
        stream.lineTo(x + width - radius, y);
        stream.curveTo(x + width, y, x + width, y, x + width, y + radius);
        stream.lineTo(x + width, y + height - radius);
        stream.curveTo(x + width, y + height, x + width, y + height, x + width - radius, y + height);
        stream.lineTo(x + radius, y + height);
        stream.curveTo(x, y + height, x, y + height, x, y + height - radius);
        stream.lineTo(x, y + radius);
        stream.curveTo(x, y, x, y, x + radius, y);

        stream.fill();
    }

    @FXML
    private void handleExporterPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.setInitialFileName("Resultat_Quiz_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf");

        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (PDDocument document = new PDDocument()) {
                // ===== PAGE DE COUVERTURE =====
                PDPage coverPage = new PDPage(PDRectangle.A4);
                document.addPage(coverPage);
                try (PDPageContentStream coverStream = new PDPageContentStream(document, coverPage)) {
                    // Fond dégradé
                    coverStream.setNonStrokingColor(240, 248, 255); // AliceBlue
                    coverStream.addRect(0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
                    coverStream.fill();

                    // Logo (avec gestion d'erreur)
                    try {
                        InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
                        if (logoStream != null) {
                            BufferedImage logoBuffered = ImageIO.read(logoStream);
                            PDImageXObject logo = LosslessFactory.createFromImage(document, logoBuffered);
                            coverStream.drawImage(logo, 180, 500, 250, 200);
                        }
                    } catch (Exception e) {
                        System.err.println("Logo non trouvé, continuation sans logo");
                    }

                    // Titre principal (sans emoji)
                    coverStream.beginText();
                    coverStream.setFont(PDType1Font.HELVETICA_BOLD, 28);
                    coverStream.setNonStrokingColor(70, 130, 180); // SteelBlue
                    coverStream.newLineAtOffset(100, 400);
                    coverStream.showText("Résultats de votre Quiz Mental");
                    coverStream.endText();
                }

                // ===== PAGE DE RÉSULTATS =====
                PDPage resultsPage = new PDPage(PDRectangle.A4);
                document.addPage(resultsPage);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, resultsPage)) {
                    // Fond blanc
                    contentStream.setNonStrokingColor(255, 255, 255);
                    contentStream.addRect(0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
                    contentStream.fill();

                    // En-tête
                    contentStream.setNonStrokingColor(70, 130, 180); // SteelBlue
                    contentStream.addRect(0, 750, PDRectangle.A4.getWidth(), 50);
                    contentStream.fill();

                    // Titre Résultats (sans emoji)
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                    contentStream.setNonStrokingColor(255, 255, 255);
                    contentStream.newLineAtOffset(50, 765);
                    contentStream.showText("Vos Résultats Détaillés");
                    contentStream.endText();

                    // Section Score
                    drawRoundedRect(contentStream, 50, 650, 500, 60, 10, new Color(173, 216, 230));
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    contentStream.newLineAtOffset(70, 675);
                    contentStream.showText(scoreLabel.getText());
                    contentStream.endText();

                    // Section État Mental
                    drawRoundedRect(contentStream, 50, 570, 500, 60, 10, new Color(144, 238, 144));
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    contentStream.newLineAtOffset(70, 595);
                    contentStream.showText(etatMentalLabel.getText());
                    contentStream.endText();

                    // Description (sans emoji)
                    String description = generateDescription(etatMental).replaceAll("[^\\x00-\\x7F]", "");
                    addMultilineText(contentStream, description, 50, 500, 14, 18);

                    // Image
                    if (imageViewEtatMental.getImage() != null) {
                        try {
                            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageViewEtatMental.getImage(), null);
                            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

                            // Cadre autour de l'image
                            contentStream.setStrokingColor(200, 200, 200);
                            contentStream.setLineWidth(1);
                            contentStream.addRect(300, 300, 200, 150);
                            contentStream.stroke();

                            contentStream.drawImage(pdImage, 305, 305, 190, 140);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Citation (sans emoji)
                    String citation = generateCitation(etatMental).replaceAll("[^\\x00-\\x7F]", "");
                    drawRoundedRect(contentStream, 50, 200, 500, 80, 10, new Color(255, 250, 205));
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 14);
                    contentStream.setNonStrokingColor(70, 70, 70);
                    contentStream.newLineAtOffset(70, 240);
                    contentStream.showText(citation);
                    contentStream.endText();
                }

                document.save(selectedFile);

                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Exportation réussie");
                    alert.setHeaderText(null);
                    alert.setContentText("Le PDF a été généré avec succès!");
                    alert.showAndWait();
                });

            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Échec de l'exportation");
                    alert.setContentText("Une erreur est survenue lors de la génération du PDF.");
                    alert.showAndWait();
                });
            }
        }
    }

    // Méthode pour ajouter du texte multiligne
    private void addMultilineText(PDPageContentStream contentStream, String text,
                                  float x, float y, int fontSize, int leading) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, fontSize);
        contentStream.newLineAtOffset(x, y);

        for (String line : text.split("\n")) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -leading);
        }

        contentStream.endText(); // Ne pas oublier cette ligne!
    }

    // Méthodes pour générer description et citation (sans emojis)
    private String generateDescription(String etatMental) {
        if (etatMental == null) return "Merci de votre participation à ce test.";

        switch (etatMental) {
            case "Bonne santé mentale":
                return "Felicitation !\nVotre santé mentale est excellente.\nContinuez à entretenir cet équilibre positif.";
            case "Légère fatigue émotionnelle":
                return "Prenez soin de vous.\nUne pause ou un moment de détente pourrait vous faire du bien.";
            case "Signes d'anxiété ou de stress":
                return "Attention.\nDes signes de stress sont présents. Pensez à adopter des techniques de relaxation.";
            case "État dépressif modéré":
                return "Soyez vigilant.\nN'ignorez pas ces signes.\nCherchez du soutien et exprimez ce que vous ressentez.";
            case "État très préoccupant":
                return "Priorité absolue.\nVotre état nécessite une attention immédiate.\nConsultez un professionnel.";
            default:
                return "Merci pour votre participation.";
        }
    }

    private String generateCitation(String etatMental) {
        switch (etatMental) {
            case "Bonne santé mentale":
                return "Le bonheur n'est reel que lorsqu'il est partage.";
            case "Légère fatigue émotionnelle":
                return "Le repos est le premier pas vers la guerison.";
            case "Signes d'anxiété ou de stress":
                return "Ne laisse pas le stress voler ta lumiere interieure.";
            case "État dépressif modéré":
                return "Parler est la premiere etape vers la guerison.";
            case "État très préoccupant":
                return "Il n'y a pas de honte à demander de l'aide.";
            default:
                return "La santé mentale est tout aussi importante que la santé physique.";
        }
    }

    //YOUTUBE API
    @FXML
    private void handleGeneratePlaylist() {
        // Chercher la playlist en fonction de l'état mental
        String playlistUrl = YouTubeAPI.searchPlaylist(etatMental);

        // Si une playlist a été trouvée, ouvrir l'URL dans le navigateur par défaut
        if (playlistUrl != null) {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(playlistUrl));
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur d'ouverture de playlist");
                    alert.setContentText("Impossible d'ouvrir la playlist YouTube.");
                    alert.showAndWait();
                });
            }
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Aucune playlist trouvée");
                alert.setHeaderText(null);
                alert.setContentText("Aucune playlist correspondant à votre état mental n'a été trouvée.");
                alert.showAndWait();
            });
        }
    }

}
