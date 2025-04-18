package Services.ali;

import Entities.ali.Evenement;
import Entities.ali.Formation;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements InterfaceCRUD<Formation> {
    private Connection con;

    public ServiceFormation() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Formation f) {
        String req = "INSERT INTO formation (titre_for, date_for, lieu_for, statut_for) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, f.getTitreFor());
            pst.setDate(2, new java.sql.Date(f.getDateFor().getTime()));
            pst.setString(3, f.getLieuFor());
            pst.setString(4, f.getStatutFor());

            pst.executeUpdate();
            System.out.println("✅ Formation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la formation : " + e.getMessage());
        }
    }

    @Override
    public void update(Formation f) {
        String req = "UPDATE formation SET titre_for=?, date_for=?, lieu_for=?, statut_for=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, f.getTitreFor());
            pst.setDate(2, new java.sql.Date(f.getDateFor().getTime()));
            pst.setString(3, f.getLieuFor());
            pst.setString(4, f.getStatutFor());
            pst.setInt(5, f.getId());

            pst.executeUpdate();
            System.out.println("✅ Formation mise à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Formation f) {
        String req = "DELETE FROM formation WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, f.getId());
            pst.executeUpdate();
            System.out.println("✅ Formation supprimée !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Formation> find() {
        String req = "SELECT * FROM formation";
        List<Formation> formations = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Formation f = new Formation();
                f.setId(rs.getInt("id"));
                f.setTitreFor(rs.getString("titre_for"));
                f.setDateFor(rs.getDate("date_for"));
                f.setLieuFor(rs.getString("lieu_for"));
                f.setStatutFor(rs.getString("statut_for"));

                formations.add(f);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }

    public Formation findById(int id) {
        String req = "SELECT * FROM formation WHERE id=?";
        Formation f = null;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                f = new Formation();
                f.setId(rs.getInt("id"));
                f.setTitreFor(rs.getString("titre_for"));
                f.setDateFor(rs.getDate("date_for"));
                f.setLieuFor(rs.getString("lieu_for"));
                f.setStatutFor(rs.getString("statut_for"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de la formation : " + e.getMessage());
        }

        return f;
    }
    public List<Formation> getAll() {
        List<Formation> list = new ArrayList<>();
        String req = "SELECT * FROM formation";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Evenement e = new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre_event"),
                        rs.getDate("date_event"),
                        rs.getString("lieu_event"),
                        rs.getString("statut_event"),
                        rs.getInt("formation_id")
                );
                Formation f = new Formation();
                list.add(f);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur chargement événements : " + ex.getMessage());
        }

        return list;
    }

}
