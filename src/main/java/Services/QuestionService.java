package Services;

import Entities.Question;
import Entities.Reponse;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements InterfaceCRUD<Question> {
    private Connection con;

    public QuestionService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Question q) {
        String req = "INSERT INTO question (question_text, answer_type, points) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, q.getQuestionText());
            pst.setString(2, q.getAnswerType());
            if (q.getPoints() != null)
                pst.setInt(3, q.getPoints());
            else
                pst.setNull(3, Types.INTEGER);

            pst.executeUpdate();
            System.out.println("✅ Question ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Question q) {
        String req = "UPDATE question SET question_text=?, answer_type=?, points=? WHERE id=?";
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, q.getQuestionText());
            pst.setString(2, q.getAnswerType());
            if (q.getPoints() != null)
                pst.setInt(3, q.getPoints());
            else
                pst.setNull(3, Types.INTEGER);
            pst.setInt(4, q.getId());

            pst.executeUpdate();
            System.out.println("✅ Question mise à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Question q) {
        String req = "DELETE FROM question WHERE id=?";
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, q.getId());

            pst.executeUpdate();
            System.out.println("✅ Question supprimée !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Question> find() {
        String req = "SELECT * FROM question";
        List<Question> questions = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setQuestionText(rs.getString("question_text"));
                q.setAnswerType(rs.getString("answer_type"));
                q.setPoints(rs.getObject("points") != null ? rs.getInt("points") : null);

                questions.add(q);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return questions;
    }

    public Question findById(int id) {
        String req = "SELECT * FROM question WHERE id=?";
        Question question = null;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                question = new Question();
                question.setId(rs.getInt("id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setAnswerType(rs.getString("answer_type"));
                question.setPoints(rs.getInt("points"));
//                question.setQuizId(rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de la question : " + e.getMessage());
        }

        return question;
    }

        public List<Question> findAll() {
            List<Question> questions = new ArrayList<>();
            String query = "SELECT * FROM question";

            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setId(rs.getInt("id"));
                    q.setQuestionText(rs.getString("question_text"));
                    q.setAnswerType(rs.getString("answer_type"));
                    q.setPoints(rs.getInt("points"));

                    // Récupérer les réponses associées à la question
                    List<Reponse> reponses = findReponsesForQuestion(q.getId());
                    q.setReponses(reponses); // Ajouter les réponses à la question

                    questions.add(q);
                }
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de la récupération des questions : " + e.getMessage());
            }
            return questions;
        }

        private List<Reponse> findReponsesForQuestion(int questionId) {
            List<Reponse> reponses = new ArrayList<>();
            String query = "SELECT * FROM reponse WHERE question_id = ?";

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, questionId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Reponse reponse = new Reponse(rs.getInt("id"));
                    reponse.setAnswerText(rs.getString("answer_text"));
                    reponse.setQuestionId(rs.getInt("question_id"));
                    reponse.setScore(rs.getInt("score"));

                    reponses.add(reponse);
                }
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de la récupération des réponses : " + e.getMessage());
            }

            return reponses;
        }

    public List<Question> read() {
        // Exemple de données fictives, remplace par ta logique JDBC si nécessaire
        List<Question> list = new ArrayList<>();
        list.add(new Question(1, "Comment vous sentez-vous aujourd’hui ?", "Choix unique", 3));
        list.add(new Question(2, "Avez-vous bien dormi ?", "Oui/Non", 2));
        return list;
    }
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM question";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setQuestionText(rs.getString("question_text"));
                questions.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public String getQuestionTextById(int id) {
        String sql = "SELECT question_text FROM question WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("question_text");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur récupération texte question : " + e.getMessage());
        }
        return "Question inconnue";
    }
}


