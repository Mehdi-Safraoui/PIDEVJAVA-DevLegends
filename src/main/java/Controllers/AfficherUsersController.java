package Controllers;

import Entities.User;
import Services.UserService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class AfficherUsersController {
    @FXML private ImageView logoImage;
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, Integer> colID;
    @FXML private TableColumn<User, String> colNom, colPrenom, colEmail, colRole, colSpecialite, colTelephone, colAdresse;
    @FXML private TableColumn<User, Integer> colAge;
    @FXML private TableColumn<User, Boolean> colStatut;
    @FXML private TableColumn<User, ImageView> colPhoto;
    @FXML private TableColumn<User, Void> colAction;

    @FXML
    public void initialize() {
        // Logo setup
        Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        logoImage.setImage(img);
        Circle clip = new Circle();
        clip.setRadius(25); // half of 120 width/height
        clip.setCenterX(25);
        clip.setCenterY(25);
        logoImage.setClip(clip);

        // Set up columns
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("user_email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("user_role"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("user_age"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("doc_specialty"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut_compte"));

        // Photo as ImageView
        colPhoto.setCellValueFactory(cellData -> {
            String path = cellData.getValue().getUser_picture();
            ImageView view = new ImageView();
            if (path != null && !path.isEmpty()) {
                Image image = new Image("file:" + path, 50, 50, true, true);
                view.setImage(image);
                view.setFitHeight(50);
                view.setFitWidth(50);
            }
            return new SimpleObjectProperty<>(view);
        });

        // Bouton Modifier
        ajouterColonneActions();

        // Charger les utilisateurs
        UserService userService = new UserService();
        List<User> users = userService.find();
        userTableView.getItems().setAll(users);
    }

    private void ajouterColonneActions() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final Button btnModifier = new Button("✏️ Modifier");
                    private final Button btnProfil = new Button("👁️ Voir Profil");
                    private final HBox buttonBox = new HBox(10); // Espace entre les boutons

                    {
                        btnModifier.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 8;");
                        btnProfil.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 8;");

                        btnModifier.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProfil.fxml"));
                                Parent root = loader.load();

                                ModifierProfilController controller = loader.getController();
                                controller.setUser(user);

                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Modifier Profil");
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        btnProfil.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profil.fxml"));
                                Parent root = loader.load();

                                ProfilController controller = loader.getController();
                                controller.setUser(user);

                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Profil Utilisateur");
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        buttonBox.getChildren().addAll(btnModifier, btnProfil);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        };

        colAction.setCellFactory(cellFactory);
    }

}