package Components.malek;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.InputStream;
import java.util.Locale;

public class PhoneNumberField extends HBox {
    private final ComboBox<String> countryCodeCombo = new ComboBox<>();
    private final TextField phoneNumberField = new TextField();
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public PhoneNumberField() {
        super(5);
        this.getStyleClass().add("phone-number-field");
        initializeComponent();
    }

    private void initializeComponent() {
        setupCountryComboBox();
        setupPhoneNumberField();
        this.getChildren().addAll(countryCodeCombo, phoneNumberField);
    }

    private void setupCountryComboBox() {
        phoneUtil.getSupportedRegions().forEach(regionCode -> {
            String countryName = new Locale("", regionCode).getDisplayCountry();
            String countryCode = "+" + phoneUtil.getCountryCodeForRegion(regionCode);
            countryCodeCombo.getItems().add(regionCode + " " + countryCode + " " + countryName);
        });

        setDefaultCountry("TN"); // Tunisie par défaut

        countryCodeCombo.setCellFactory(lv -> new ListCell<>() {
            private final ImageView flagView = new ImageView();

            {
                flagView.setFitWidth(20);
                flagView.setFitHeight(15);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    String[] parts = item.split(" ");
                    flagView.setImage(loadFlagImage(parts[0]));
                    setGraphic(flagView);
                    setText(" " + parts[2] + " (" + parts[1] + ")");
                }
            }
        });

        countryCodeCombo.setButtonCell(new ListCell<>() {
            private final ImageView flagView = new ImageView();

            {
                flagView.setFitWidth(20);
                flagView.setFitHeight(15);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    String[] parts = item.split(" ");
                    flagView.setImage(loadFlagImage(parts[0]));
                    setGraphic(flagView);
                    setText(" " + parts[1]);
                }
            }
        });
    }

    private void setupPhoneNumberField() {
        phoneNumberField.setPromptText("Numéro de téléphone");
        phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                phoneNumberField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    private Image loadFlagImage(String countryCode) {
        try {
            String path = "/flags/" + countryCode.toLowerCase() + ".png";
            InputStream input = getClass().getResourceAsStream(path);
            return input != null ? new Image(input) : null;
        } catch (Exception e) {
            System.err.println("Error loading flag: " + e.getMessage());
            return null;
        }
    }

    public void setDefaultCountry(String regionCode) {
        String countryName = new Locale("", regionCode).getDisplayCountry();
        String countryCode = "+" + phoneUtil.getCountryCodeForRegion(regionCode);
        countryCodeCombo.getSelectionModel().select(regionCode + " " + countryCode + " " + countryName);
    }

    public PhoneNumberUtil getPhoneNumberUtil() {
        return phoneUtil;
    }

    // Modifiez cette méthode pour une meilleure validation
    public String getPhoneNumber() {
        try {
            String selected = countryCodeCombo.getSelectionModel().getSelectedItem();
            String countryCode = selected.split("\\+")[1].split(" ")[0];
            String number = "+" + countryCode + phoneNumberField.getText();

            // Valide le numéro avant de le retourner
            PhoneNumber phoneNumber = phoneUtil.parse(number, null);
            if (phoneUtil.isValidNumber(phoneNumber)) {
                return number;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void setPhoneNumber(String number) {
        try {
            if (number != null && !number.isEmpty()) {
                PhoneNumber phoneNumber = phoneUtil.parse(number, null);
                String regionCode = phoneUtil.getRegionCodeForNumber(phoneNumber);
                String nationalNumber = phoneUtil.format(phoneNumber,
                        PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

                setDefaultCountry(regionCode);
                phoneNumberField.setText(nationalNumber.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            setDefaultCountry("TN");
            phoneNumberField.clear();
        }
    }

    public TextField getPhoneNumberField() {
        return phoneNumberField;
    }
}