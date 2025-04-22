package Services.mehdi;

import Entities.mehdi.Reclamation;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements InterfaceCRUD<Reclamation> {

    private final Connection con;

    public ReclamationService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Reclamation r) {
        String req = "INSERT INTO reclamation (sujet_rec, contenu_rec, date_rec, email_des, statut_rec) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, r.getSujetRec());
            ps.setString(2, r.getContenuRec());
            ps.setDate(3, new java.sql.Date(r.getDateRec().getTime()));
            ps.setString(4, r.getEmailRec());
            ps.setString(5, r.getStatutRec());
            ps.executeUpdate();
            System.out.println("✅ Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Reclamation r) {
        String req = "UPDATE reclamation SET sujet_rec=?, contenu_rec=?, date_rec=?, email_des=?, statut_rec=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, r.getSujetRec());
            ps.setString(2, r.getContenuRec());
            ps.setDate(3, new java.sql.Date(r.getDateRec().getTime()));
            ps.setString(4, r.getEmailRec());
            ps.setString(5, r.getStatutRec());
            ps.setInt(6, r.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Réclamation mise à jour !");
            } else {
                System.out.println("⚠️ Aucune réclamation trouvée avec l’ID " + r.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Reclamation r) {
        deleteById(r.getId());
    }

    public void deleteById(int id) {
        String req = "DELETE FROM reclamation WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("🗑️ Réclamation avec ID " + id + " supprimée avec succès.");
            } else {
                System.out.println("⚠️ Aucune réclamation trouvée avec ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public void updateSujetById(int id, String nouveauSujet) {
        String req = "UPDATE reclamation SET sujet_rec=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, nouveauSujet);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Sujet mis à jour pour la réclamation ID " + id);
            } else {
                System.out.println("⚠️ Réclamation introuvable pour l’ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour du sujet : " + e.getMessage());
        }
    }

    @Override
    public List<Reclamation> find() {
        List<Reclamation> list = new ArrayList<>();
        String req = "SELECT * FROM reclamation";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id"),
                        rs.getString("sujet_rec"),
                        rs.getString("contenu_rec"),
                        rs.getDate("date_rec"),
                        rs.getString("email_des"),
                        rs.getString("statut_rec")
                );
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des réclamations : " + e.getMessage());
        }
        return list;
    }
}
