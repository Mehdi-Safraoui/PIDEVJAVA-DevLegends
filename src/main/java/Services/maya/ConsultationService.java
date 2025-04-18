package Services.maya;

import Entities.maya.Consultation;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ConsultationService implements InterfaceCRUD<Consultation> {
    private Connection con;

    public ConsultationService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Consultation cons) {
        String req = "INSERT INTO consultation (date_cons, lien_visio_cons, notes_cons, nom, prenom, age) VALUES (?, ?, ?, ?, ?, ?)";

        // Vérification que la date choisie est supérieure à la date actuelle
        if (cons.getDateCons().before(new Date())) {
            System.out.println("❌ La date de consultation doit être supérieure à la date actuelle.");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setDate(1, new java.sql.Date(cons.getDateCons().getTime()));
            pst.setString(2, cons.getLienVisioCons());
            pst.setString(3, cons.getNotesCons());
            pst.setString(4, cons.getNom());
            pst.setString(5, cons.getPrenom());
            pst.setInt(6, cons.getAge());

            pst.executeUpdate();
            System.out.println("✅ Consultation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Consultation cons) {
        String req = "UPDATE consultation SET date_cons=?, lien_visio_cons=?, notes_cons=?, nom=?, prenom=?, age=? WHERE id=?";

        // Vérification que la date choisie est supérieure à la date actuelle
        if (cons.getDateCons().before(new Date())) {
            System.out.println("❌ La date de consultation doit être supérieure à la date actuelle.");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setDate(1, new java.sql.Date(cons.getDateCons().getTime()));
            pst.setString(2, cons.getLienVisioCons());
            pst.setString(3, cons.getNotesCons());
            pst.setString(4, cons.getNom());
            pst.setString(5, cons.getPrenom());
            pst.setInt(6, cons.getAge());
            pst.setInt(7, cons.getId());

            pst.executeUpdate();
            System.out.println("✅ Consultation mise à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Consultation cons) {
        String req = "DELETE FROM consultation WHERE id=?";
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, cons.getId());

            pst.executeUpdate();
            System.out.println("✅ Consultation supprimée !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Consultation> find() {
        String req = "SELECT * FROM consultation";
        List<Consultation> consultations = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Consultation cons = new Consultation();
                cons.setId(rs.getInt("id"));
                cons.setDateCons(rs.getDate("date_cons"));
                cons.setLienVisioCons(rs.getString("lien_visio_cons"));
                cons.setNotesCons(rs.getString("notes_cons"));
                cons.setNom(rs.getString("nom"));
                cons.setPrenom(rs.getString("prenom"));
                cons.setAge(rs.getInt("age"));

                consultations.add(cons);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return consultations;
    }

    public Consultation findById(int id) {
        String req = "SELECT * FROM consultation WHERE id=?";
        Consultation cons = null;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                cons = new Consultation();
                cons.setId(rs.getInt("id"));
                cons.setDateCons(rs.getDate("date_cons"));
                cons.setLienVisioCons(rs.getString("lien_visio_cons"));
                cons.setNotesCons(rs.getString("notes_cons"));
                cons.setNom(rs.getString("nom"));
                cons.setPrenom(rs.getString("prenom"));
                cons.setAge(rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de la consultation : " + e.getMessage());
        }

        return cons;
    }



}
