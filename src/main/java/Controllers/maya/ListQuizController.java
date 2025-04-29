package Controllers.maya;

import Entities.maya.Quiz;
import Entities.maya.Reponse;
import Services.maya.QuizService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;
import java.util.*;

public class ListQuizController implements Initializable {

    @FXML private TableView<Quiz> quizTableView; // TableView qui affiche les quiz
    @FXML private TableColumn<Quiz, Integer> idColumn;
    @FXML private TableColumn<Quiz, Integer> scoreColumn;
    @FXML private TableColumn<Quiz, String> etatMentalColumn;
    @FXML private TableColumn<Quiz, Void> actionsColumn;

    @FXML private ComboBox<String> comboTri;
    @FXML private CheckBox checkDesc;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private final QuizService quizService = new QuizService();
    private ObservableList<Quiz> listeQuiz = FXCollections.observableArrayList(); // Liste des quiz à afficher

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        etatMentalColumn.setCellValueFactory(new PropertyValueFactory<>("etatMental"));

        // Initialiser le ComboBox de tri
        comboTri.getItems().addAll("État mental", "Score");
        comboTri.setValue("État mental");

        // Mettre à jour la TableView avec la liste triée
        quizTableView.setItems(listeQuiz);

        // Charger les données et ajouter les boutons de suppression
        loadData();
        addDeleteButtonToTable();
    }

    private void loadData() {
        // Charger les quiz depuis le service
        List<Quiz> quizzes = quizService.getAllQuizzes();
        ObservableList<Quiz> observableList = FXCollections.observableArrayList(quizzes);

        // Filtrer les résultats en fonction de la recherche
        FilteredList<Quiz> filteredData = new FilteredList<>(observableList, b -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(quiz -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lowerCase = newVal.toLowerCase();
                return String.valueOf(quiz.getId()).contains(lowerCase)
                        || String.valueOf(quiz.getScore()).contains(lowerCase)
                        || (quiz.getEtatMental() != null && quiz.getEtatMental().toLowerCase().contains(lowerCase));
            });
        });

        // Trier les résultats
        SortedList<Quiz> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(quizTableView.comparatorProperty());

        // Mettre à jour la TableView
        quizTableView.setItems(sortedData);
    }

    private void addDeleteButtonToTable() {
        // Ajouter un bouton de suppression dans la colonne "Actions"
        actionsColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Quiz, Void> call(final TableColumn<Quiz, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Supprimer");

                    {
                        btn.setOnAction(event -> {
                            Quiz selectedQuiz = getTableView().getItems().get(getIndex());
                            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                            confirm.setTitle("Confirmation");
                            confirm.setHeaderText(null);
                            confirm.setContentText("Voulez-vous vraiment supprimer ce quiz ?");
                            Optional<ButtonType> result = confirm.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                quizService.delete(selectedQuiz.getId());
                                refreshTable();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        });
    }

    private void refreshTable() {
//        quizTableView.getItems().clear();
        loadData();
    }

    @FXML
    public void trierQuiz() {
        String critere = comboTri.getValue();
        boolean desc = checkDesc.isSelected();

        // On part d'une copie des données originales
        List<Quiz> quizList = new ArrayList<>(quizTableView.getItems());

        Comparator<Quiz> comparator = null;

        switch (critere) {
            case "État mental":
                comparator = Comparator.comparing(
                        q -> q.getEtatMental() == null ? "" : q.getEtatMental(),
                        String.CASE_INSENSITIVE_ORDER
                );
                break;

            case "Score":
                comparator = Comparator.comparingInt(Quiz::getScore);
                break;
        }

        if (comparator != null) {
            if (desc) {
                comparator = comparator.reversed();
            }
            quizList.sort(comparator);
        }

        // On recharge les données dans la table
        quizTableView.setItems(FXCollections.observableArrayList(quizList));
    }

//    public void exportToPdf(ActionEvent event) {
//        // Récupérer les données du quiz sélectionné
//        Quiz selectedQuiz = quizTableView.getSelectionModel().getSelectedItem();
//        if (selectedQuiz == null) {
//            System.out.println("Aucun quiz sélectionné.");
//            return;
//        }
//
//        // Créer un fichier PDF
//        String outputFile = "Quiz_Result_" + selectedQuiz.getId() + ".pdf";
//        File file = new File(outputFile);
//        try {
//            // Créer un document PDF
//            PdfDocument pdfDoc = new PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(new FileOutputStream(file)));
//            Document document = new Document(pdfDoc);
//
//            // Ajouter du contenu au PDF
//            document.add(new Paragraph("Résultats du Quiz ID: " + selectedQuiz.getId()));
//            document.add(new Paragraph("Score : " + selectedQuiz.getScore()));
//            document.add(new Paragraph("État mental : " + selectedQuiz.getEtatMental()));
//
//            // Si tu as plus de détails sur les réponses du quiz, tu peux aussi les ajouter ici
//            document.add(new Paragraph("Détails des réponses :"));
//            for (Reponse rep : selectedQuiz.getReponses()) {
//                document.add(new Paragraph("Question : " + rep.getQuestionText()));
//                document.add(new Paragraph("Réponse : "  + rep.getAnswerText()));
//            }
//
//            // Fermer le document PDF
//            document.close();
//
//            System.out.println("PDF exporté avec succès dans le fichier : " + outputFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
