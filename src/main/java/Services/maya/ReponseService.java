package Services.maya;

import Entities.maya.Reponse;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements InterfaceCRUD<Reponse> {
    private final Connection con;

    public ReponseService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Reponse reponse) {
        String req = "INSERT INTO reponse (answer_text, question_id, score) VALUES (?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, reponse.getAnswerText());
            pst.setInt(2, reponse.getQuestionId());
            pst.setInt(3, reponse.getScore());

            pst.executeUpdate();
            System.out.println("✅ Réponse ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
    }

    @Override
    public void update(Reponse reponse) {
        String req = "UPDATE reponse SET answer_text=?, question_id=?, score=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, reponse.getAnswerText());
            pst.setInt(2, reponse.getQuestionId());
            pst.setInt(3, reponse.getScore());
            pst.setInt(4, reponse.getId());

            pst.executeUpdate();
            System.out.println("✅ Réponse mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de la réponse : " + e.getMessage());
        }
    }

    @Override
    public void delete(Reponse reponse) {
        try {
            String sql = "DELETE FROM reponse WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reponse.getId());
            ps.executeUpdate();
            System.out.println("✅ Réponse supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de la réponse : " + e.getMessage());
        }
    }

    @Override
    public List<Reponse> find() {
        String req = "SELECT * FROM reponse";
        List<Reponse> reponses = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setAnswerText(rs.getString("answer_text"));
                r.setQuestionId(rs.getInt("question_id"));
                r.setScore(rs.getInt("score"));
                reponses.add(r);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des réponses : " + e.getMessage());
        }

        return reponses;
    }

    public Reponse findById(int id) {
        String query = "SELECT * FROM reponse WHERE id = ?";
        Reponse reponse = null;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setAnswerText(rs.getString("answer_text"));
                reponse.setQuestionId(rs.getInt("question_id"));
                reponse.setScore(rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de la réponse : " + e.getMessage());
        }

        return reponse;
    }

    public List<Reponse> findByQuestionId(int questionId) {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE question_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setQuestionId(rs.getInt("question_id"));
                reponse.setAnswerText(rs.getString("answer_text"));
                reponse.setScore(rs.getInt("score"));
                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des réponses : " + e.getMessage());
        }

        return reponses;
    }

    // ✅ Affiche le texte de la question avec une jointure SQL
    public List<Reponse> getAll() {
        List<Reponse> reponses = new ArrayList<>();
        String sql = "SELECT r.id, r.answer_text, r.question_id, r.score, q.question_text " +
                "FROM reponse r JOIN question q ON r.question_id = q.id";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setAnswerText(rs.getString("answer_text"));
                reponse.setQuestionId(rs.getInt("question_id"));
                reponse.setScore(rs.getInt("score"));
                reponse.setQuestionText(rs.getString("question_text")); // ✅ Ajout du texte de la question

                reponses.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des réponses avec texte question : " + e.getMessage());
        }

        return reponses;
    }

}
