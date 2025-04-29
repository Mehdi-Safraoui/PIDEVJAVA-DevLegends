package Controllers.malek;

import Entities.malek.Pack;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class PaymentPageController {

    // Configuration PayPal

    private static final String PAYPAL_MODE = "sandbox";

    @FXML private VBox paymentForm;
    @FXML private Label nomPackLabel;
    @FXML private Label paymentAmountLabel;
    @FXML private TextField cardHolderField;
    @FXML private TextField cardNumberField;
    @FXML private TextField cardExpiryField;
    @FXML private TextField cardCVCField;
    @FXML private TextField cardZipField;
    @FXML private Button payButton;
    @FXML private VBox creditCardForm;
    @FXML private VBox paypalForm;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private HBox cardLogosBox;
    @FXML private TextField paypalEmailField;

    private Pack currentPack;
    private String paypalClientId;
    private String paypalSecret;

    @FXML
    public void initialize() {
        loadPayPalCredentials();
        setupPaymentMethods();
        setupCardNumberFormatting();
        setupExpiryDateFormatting();
        setupPaymentMethodListener();
        togglePaymentForm();
    }

    // Méthodes pour le formatage de la carte
    private void setupCardNumberFormatting() {
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            String formatted = newValue.replaceAll("\\s", "");
            if (formatted.length() > 16) {
                formatted = formatted.substring(0, 16);
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < formatted.length(); i++) {
                if (i > 0 && i % 4 == 0) {
                    sb.append(" ");
                }
                sb.append(formatted.charAt(i));
            }
            cardNumberField.setText(sb.toString());
        });
    }

    private void setupExpiryDateFormatting() {
        cardExpiryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardExpiryField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            if (newValue.length() == 2 && oldValue.length() == 1) {
                cardExpiryField.setText(newValue + "/");
            }

            if (newValue.length() > 5) {
                cardExpiryField.setText(oldValue);
            }
        });
    }

    // Méthodes de validation
    private boolean isValidCardNumber(String cardNumber) {
        String cleanNumber = cardNumber.replaceAll("\\s", "");
        return cleanNumber.matches("\\d{13,16}");
    }

    private boolean isValidExpiryDate(String expiryDate) {
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }

        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);

        return month >= 1 && month <= 12 && year >= 0 && year <= 99;
    }

    // Le reste des méthodes reste inchangé
    private void loadPayPalCredentials() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Fichier config.properties introuvable");
                return;
            }

            Properties props = new Properties();
            props.load(input);

            paypalClientId = props.getProperty("paypal.client.id", paypalClientId);
            paypalSecret = props.getProperty("paypal.client.secret", paypalSecret);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des identifiants PayPal: " + e.getMessage());
            // Utilise les valeurs par défaut définies dans la classe
        }
    }

    private void setupPaymentMethods() {
        paymentMethodComboBox.getItems().addAll("Carte Bancaire", "PayPal");
        paymentMethodComboBox.getSelectionModel().selectFirst();
    }

    private void setupPaymentMethodListener() {
        paymentMethodComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            togglePaymentForm();
        });
    }

    private void togglePaymentForm() {
        String selectedMethod = paymentMethodComboBox.getValue();
        boolean isCreditCard = "Carte Bancaire".equals(selectedMethod);

        creditCardForm.setVisible(isCreditCard);
        paypalForm.setVisible(!isCreditCard);
        cardLogosBox.setVisible(isCreditCard);

        if (isCreditCard) {
            paypalEmailField.clear();
        } else {
            clearCreditCardFields();
        }
    }

    private void clearCreditCardFields() {
        cardHolderField.clear();
        cardNumberField.clear();
        cardExpiryField.clear();
        cardCVCField.clear();
        cardZipField.clear();
    }

    public void setPack(Pack pack) {
        this.currentPack = pack;
        updateUI();
    }

    private void updateUI() {
        if (currentPack != null) {
            nomPackLabel.setText(currentPack.getNomPack());
            paymentAmountLabel.setText(String.format("%.2f TND", currentPack.getPrixPack()));
        }
    }

    @FXML
    private void onPayClicked() {
        if (!validateForm()) return;

        String selectedMethod = paymentMethodComboBox.getValue();
        if ("PayPal".equals(selectedMethod)) {
            processPayPalPayment();
        } else {
            processCreditCardPayment();
        }
    }

    private boolean validateForm() {
        String selectedMethod = paymentMethodComboBox.getValue();

        if ("Carte Bancaire".equals(selectedMethod)) {
            return validateCreditCardForm();
        } else {
            return validatePayPalForm();
        }
    }

    private boolean validateCreditCardForm() {
        if (cardHolderField.getText().trim().isEmpty() ||
                cardNumberField.getText().trim().isEmpty() ||
                cardExpiryField.getText().trim().isEmpty() ||
                cardCVCField.getText().trim().isEmpty() ||
                cardZipField.getText().trim().isEmpty()) {

            showAlert("Erreur", "Veuillez remplir tous les champs du formulaire de carte bancaire", Alert.AlertType.ERROR);
            return false;
        }

        if (!isValidCardNumber(cardNumberField.getText())) {
            showAlert("Erreur", "Numéro de carte invalide (doit contenir 13 à 16 chiffres)", Alert.AlertType.ERROR);
            return false;
        }

        if (!isValidExpiryDate(cardExpiryField.getText())) {
            showAlert("Erreur", "Date d'expiration invalide (format MM/AA)", Alert.AlertType.ERROR);
            return false;
        }

        if (!cardCVCField.getText().matches("\\d{3,4}")) {
            showAlert("Erreur", "Code de sécurité invalide (3 ou 4 chiffres)", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validatePayPalForm() {
        if (paypalEmailField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre email PayPal", Alert.AlertType.ERROR);
            return false;
        }

        if (!paypalEmailField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Erreur", "Email PayPal invalide", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void processCreditCardPayment() {
        String message = String.format("Paiement par carte de %.2f TND effectué avec succès", currentPack.getPrixPack());
        showAlert("Succès", message, Alert.AlertType.INFORMATION);
    }

    private void processPayPalPayment() {
        try {
            APIContext apiContext = new APIContext(paypalClientId, paypalSecret, PAYPAL_MODE);

            Amount amount = new Amount();
            amount.setCurrency("USD");

            double amountValue = currentPack.getPrixPack();
            if (amountValue <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif");
            }
            amount.setTotal(String.format(Locale.US, "%.2f", amountValue));

            Transaction transaction = new Transaction();
            transaction.setDescription("Paiement pour " + currentPack.getNomPack());
            transaction.setAmount(amount);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(List.of(transaction));

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost/cancel");
            redirectUrls.setReturnUrl("http://localhost/success");
            payment.setRedirectUrls(redirectUrls);

            Payment createdPayment = payment.create(apiContext);

            createdPayment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .ifPresentOrElse(
                            link -> openPayPalWebView(link.getHref()),
                            () -> showAlert("Erreur", "Impossible d'obtenir le lien PayPal", Alert.AlertType.ERROR)
                    );

        } catch (PayPalRESTException e) {
            String errorDetails = e.getDetails() != null ? e.getDetails().toString() : e.getMessage();
            showAlert("Erreur PayPal", "Détails de l'erreur:\n" + errorDetails, Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du traitement du paiement: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void openPayPalWebView(String url) {
        try {
            Stage stage = new Stage();
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(url);

            webEngine.locationProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    if (newVal.contains("localhost/success")) {
                        stage.close();
                        showAlert("Succès", "Paiement PayPal effectué avec succès", Alert.AlertType.INFORMATION);
                    } else if (newVal.contains("localhost/cancel")) {
                        stage.close();
                        showAlert("Annulé", "Paiement PayPal annulé", Alert.AlertType.WARNING);
                    }
                }
            });

            Scene scene = new Scene(webView, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Paiement PayPal");
            stage.show();
        } catch (Exception e) {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            } catch (Exception ex) {
                showAlert("Erreur", "Impossible d'ouvrir PayPal: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}