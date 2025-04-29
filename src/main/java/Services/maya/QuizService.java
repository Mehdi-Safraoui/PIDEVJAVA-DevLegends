package Services.maya;
import Entities.maya.Question;

import Entities.maya.Quiz;
import Entities.maya.Reponse;
import Utils.MyDB;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizService {

    private Connection con;

    public QuizService() {
        con = MyDB.getInstance().getCon();
    }

    private List<Question> questions; // Liste des questions, à récupérer depuis la base de données ou une autre source

    // Calcul du score en fonction des réponses
    public int calculateScore(List<Reponse> reponses) {
        int score = 0;

        for (Reponse reponse : reponses) {
            score += reponse.getScore();
        }

        return score;
    }

    // Méthode pour associer un état mental à un mot-clé pour Unsplash
    public String getImageFromEtatMental(String etatMental) {
        // Mots-clés associés à chaque état mental
        Map<String, String> keywords = new HashMap<>();
        keywords.put("Bonne santé mentale", "happy mind");
        keywords.put("Légère fatigue émotionnelle", "calm nature");
        keywords.put("Signes d’anxiété ou de stress", "stress relief");
        keywords.put("État dépressif modéré", "dark mood");
        keywords.put("État très préoccupant", "mental health struggle");

        // Retourne le mot-clé correspondant à l'état mental
        return keywords.getOrDefault(etatMental, "general health");
    }

    // Génère l'URL d'image selon l'état mental
    public String generateImageUrl(String etatMental) {
        Map<String,String> keywords = Map.of(
                "Bonne santé mentale", "happy mind",
                "Légère fatigue émotionnelle", "calm nature",
                "Signes d’anxiété ou de stress", "stress relief",
                "État dépressif modéré", "dark mood",
                "État très préoccupant", "mental health struggle"
        );
        String key = keywords.getOrDefault(etatMental, "general health");
        return "https://source.unsplash.com/400x300/?" + key;
    }

    // Conserve la même logique de mapping score -> état mental
    public String interpretScore(int score) {
        String etatMental;
        if (score <= 10) {
            etatMental = "Bonne santé mentale";
        } else if (score <= 20) {
            etatMental = "Légère fatigue émotionnelle";
        } else if (score <= 30) {
            etatMental = "Signes d’anxiété ou de stress";
        } else if (score <= 40) {
            etatMental = "État dépressif modéré";
        } else {
            etatMental = "État très préoccupant";
        }
        return etatMental;
    }

    public String getDescriptionFromEtatMental(String etatMental) {
        switch (etatMental) {
            case "Bonne santé mentale":
                return "Vous semblez équilibrée, sereine et en bonne forme mentale. Continuez à prendre soin de vous !";
            case "Légère fatigue émotionnelle":
                return "Vous présentez quelques signes de fatigue mentale. Une petite pause ou un moment de détente pourrait vous faire du bien.";
            case "Signes d’anxiété ou de stress":
                return "Vous montrez des signes d’anxiété ou de stress. Il est peut-être temps d’en parler ou de chercher un moyen de relâcher la pression.";
            case "État dépressif modéré":
                return "Votre score indique un état mental préoccupant. Essayez de ne pas rester seule et envisagez de consulter un professionnel.";
            case "État très préoccupant":
                return "Votre état mental nécessite une attention urgente. N’hésitez pas à demander de l’aide, vous n’êtes pas seule.";
            default:
                return "État non reconnu. Veuillez réessayer.";
        }
    }

    // Crée un Quiz avec score et état mental
    public Quiz generateQuiz(List<Reponse> reponses) {
        int score = calculateScore(reponses);
        String etatMental = interpretScore(score);
        return new Quiz(score, etatMental);
    }

    public List<Question> getRandomQuestions(int limit) {
        List<Question> randomQuestions = new ArrayList<>();

        try (Connection conn = MyDB.getInstance().getCon()) {
            String query = "SELECT * FROM question ORDER BY RAND() LIMIT ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, limit);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Question q = new Question();
                int questionId = rs.getInt("id");
                q.setId(questionId);
                q.setQuestionText(rs.getString("question_Text"));
                q.setAnswerType(rs.getString("answer_Type"));
                q.setPoints(rs.getInt("points"));

                // Charger les réponses associées
                List<Reponse> reponses = ReponseService.getReponsesByQuestionId(questionId);
                System.out.println("Réponses pour la question " + questionId + ": " + reponses.size());  // Log
                q.setReponses(reponses);

                randomQuestions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return randomQuestions;
    }

    public void saveQuizResult(Quiz result) {
        String sql = "INSERT INTO quiz (score, etat_mental) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, result.getScore());
            pst.setString(2, result.getEtatMental());
            pst.executeUpdate();
            System.out.println("✅ Quiz enregistré avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'enregistrement du quiz : " + e.getMessage());
        }
    }



    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizList = new ArrayList<>();

        try (Connection conn = MyDB.getInstance().getCon()) {
            String query = "SELECT * FROM quiz";  // On récupère tous les quiz
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int score = rs.getInt("score");
                String etatMental = rs.getString("etat_mental");

                // ➔ Créer directement avec l'id, score et état mental
                Quiz quiz = new Quiz(id, score, etatMental);

                quizList.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizList;
    }





    public void delete(int id) {
        String sql = "DELETE FROM quiz WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows>0) System.out.println("✅ Quiz ID " + id + " supprimé.");
            else System.out.println("ℹ️ Aucun quiz trouvé pour ID " + id);
        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression : " + e.getMessage());
        }
    }


}
