package Controllers;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.io.image.ImageDataFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class QRCodePDFGenerator {

    public static void generateQRCodePDF(String titreEvent, int idEvent, String dateEvent, String lieuEvent, String pdfPath) {
        try {
            // ✅ 1. Générer le contenu du QR code (avec TOUTES les infos)
            String qrContent = "Événement : " + titreEvent +
                    "\nID : " + idEvent +
                    "\nDate : " + dateEvent +
                    "\nLieu : " + lieuEvent;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] qrCodeImageData = pngOutputStream.toByteArray();

            // ✅ 2. Création du PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addNewPage();
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(50, 50, 50, 50);

            float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
            float pageHeight = pdfDoc.getDefaultPageSize().getHeight();

            // Couleurs
            DeviceRgb darkBlue = new DeviceRgb(10, 36, 106);
            DeviceRgb grayText = new DeviceRgb(120, 120, 120);
            DeviceRgb lightBackground = new DeviceRgb(245, 245, 245);

            // ✅ Fond clair
            PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
            canvas.saveState()
                    .setFillColor(lightBackground)
                    .rectangle(0, 0, pageWidth, pageHeight)
                    .fill()
                    .restoreState();

            // ✅ Logo
            InputStream logoStream = QRCodePDFGenerator.class.getResourceAsStream("/logo.png");
            if (logoStream != null) {
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = new Image(ImageDataFactory.create(logoBytes));
                logo.scaleToFit(80, 80);
                logo.setFixedPosition(50, pageHeight - 100);
                document.add(logo);
            } else {
                System.err.println("Attention : Logo non trouvé !");
            }

            // ✅ Titre
            Paragraph title = new Paragraph("Billet d'Événement")
                    .setFontSize(26)
                    .setBold()
                    .setFontColor(darkBlue)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setMarginBottom(10);
            document.add(title);

            // ✅ Ligne de séparation
            canvas.setStrokeColor(darkBlue)
                    .setLineWidth(1)
                    .moveTo(50, pageHeight - 180)
                    .lineTo(pageWidth - 50, pageHeight - 180)
                    .stroke();

            // ✅ Détails visibles dans le PDF (juste titre + ID)
            Paragraph eventDetails = new Paragraph("Événement : " + titreEvent + "\nID : " + idEvent)
                    .setFontSize(14)
                    .setFontColor(grayText)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setMarginBottom(20);
            document.add(eventDetails);

            // ✅ QR code centré avec cadre
            float qrX = (pageWidth - 220) / 2;
            float qrY = pageHeight / 2 - 100;

            canvas.setStrokeColor(darkBlue)
                    .rectangle(qrX, qrY, 220, 220)
                    .stroke();

            Image qrCodeImage = new Image(ImageDataFactory.create(qrCodeImageData));
            qrCodeImage.setWidth(200);
            qrCodeImage.setHeight(200);
            qrCodeImage.setFixedPosition(qrX + 10, qrY + 10);
            document.add(qrCodeImage);

            Paragraph scanInfo = new Paragraph("Scannez pour voir les détails de l'événement.")
                    .setFontSize(12)
                    .setFontColor(grayText)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(scanInfo);

            // ✅ Footer
            Paragraph footer = new Paragraph("Merci pour votre confiance - Innerbloom Events")
                    .setFontSize(10)
                    .setFontColor(grayText)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(50, 20, pageWidth - 100);
            document.add(footer);

            document.close();
            System.out.println("✅ PDF généré avec succès : " + pdfPath);

        } catch (IOException | WriterException e) {
            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
