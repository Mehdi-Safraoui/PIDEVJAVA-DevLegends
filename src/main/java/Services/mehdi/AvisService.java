package Services.mehdi;

import Entities.mehdi.Avis;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements InterfaceCRUD<Avis> {

    private final Connection con;

    public AvisService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Avis a) {
        String req = "INSERT INTO avis (sujet_avis, contenu_avis, note_avis, date_avis, email_avis, statut_avis) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, a.getSujetAvis());
            ps.setString(2, a.getContenuAvis());
            ps.setInt(3, a.getNoteAvis());
            ps.setDate(4, new java.sql.Date(a.getDateAvis().getTime()));
            ps.setString(5, a.getEmailAvis());
            ps.setString(6, a.getStatutAvis());
            ps.executeUpdate();
            System.out.println("✅ Avis ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Avis a) {
        String req = "UPDATE avis SET sujet_avis=?, contenu_avis=?, note_avis=?, date_avis=?, email_avis=?, statut_avis=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, a.getSujetAvis());
            ps.setString(2, a.getContenuAvis());
            ps.setInt(3, a.getNoteAvis());
            ps.setDate(4, new java.sql.Date(a.getDateAvis().getTime()));
            ps.setString(5, a.getEmailAvis());
            ps.setString(6, a.getStatutAvis());
            ps.setInt(7, a.getId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Avis mis à jour !");
            } else {
                System.out.println("⚠️ Aucun avis trouvé avec l’ID " + a.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Avis a) {
        deleteById(a.getId());
    }

    public void deleteById(int id) {
        String req = "DELETE FROM avis WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("🗑️ Avis avec ID " + id + " supprimé avec succès.");
            } else {
                System.out.println("⚠️ Aucun avis trouvé avec ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Avis> find() {
        List<Avis> list = new ArrayList<>();
        String req = "SELECT * FROM avis";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Avis a = new Avis(
                        rs.getInt("id"),
                        rs.getString("sujet_avis"),
                        rs.getString("contenu_avis"),
                        rs.getInt("note_avis"),
                        rs.getDate("date_avis"),
                        rs.getString("email_avis"),
                        rs.getString("statut_avis")
                );
                list.add(a);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des avis : " + e.getMessage());
        }
        return list;
    }
}
