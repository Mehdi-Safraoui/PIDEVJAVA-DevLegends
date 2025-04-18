package Services.ali;

import Entities.ali.Evenement;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceEvenement implements InterfaceCRUD<Evenement> {
    private Connection con;

    public ServiceEvenement() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Evenement event) {
        String req = "INSERT INTO evenement (titre_event, date_event, lieu_event, statut_event, formation_id) VALUES (?, ?, ?, ?, ?)";

        try {
            if (event.getDateEvent().before(new Date())) {
                System.out.println("❌ La date de l'événement doit être supérieure à la date actuelle.");
                return;
            }

            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, event.getTitreEvent());
            pst.setDate(2, new java.sql.Date(event.getDateEvent().getTime()));
            pst.setString(3, event.getLieuEvent());
            pst.setString(4, event.getStatutEvent());
            if (event.getFormationId() != null) {
                pst.setInt(5, event.getFormationId());
            } else {
                pst.setNull(5, Types.INTEGER);
            }

            pst.executeUpdate();
            System.out.println("✅ Événement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void update(Evenement event) {
        String req = "UPDATE evenement SET titre_event=?, date_event=?, lieu_event=?, statut_event=?, formation_id=? WHERE id=?";

        try {
            if (event.getDateEvent().before(new Date())) {
                System.out.println("❌ La date de l'événement doit être supérieure à la date actuelle.");
                return;
            }

            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, event.getTitreEvent());
            pst.setDate(2, new java.sql.Date(event.getDateEvent().getTime()));
            pst.setString(3, event.getLieuEvent());
            pst.setString(4, event.getStatutEvent());
            if (event.getFormationId() != null) {
                pst.setInt(5, event.getFormationId());
            } else {
                pst.setNull(5, Types.INTEGER);
            }
            pst.setInt(6, event.getId());

            pst.executeUpdate();
            System.out.println("✅ Événement mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void delete(Evenement evenement) {

    }

    @Override
    public List<Evenement> find() {
        String req = "SELECT * FROM evenement";
        List<Evenement> events = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Evenement event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setTitreEvent(rs.getString("titre_event"));
                event.setDateEvent(rs.getDate("date_event"));
                event.setLieuEvent(rs.getString("lieu_event"));
                event.setStatutEvent(rs.getString("statut_event"));
                event.setFormationId(rs.getInt("formation_id"));

                events.add(event);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des événements : " + e.getMessage());
        }

        return events;
    }

    public Evenement findById(int id) {
        String req = "SELECT * FROM evenement WHERE id=?";
        Evenement event = null;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setTitreEvent(rs.getString("titre_event"));
                event.setDateEvent(rs.getDate("date_event"));
                event.setLieuEvent(rs.getString("lieu_event"));
                event.setStatutEvent(rs.getString("statut_event"));
                event.setFormationId(rs.getInt("formation_id"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'événement : " + e.getMessage());
        }

        return event;
    }
    public List<Evenement> getAll() {
        List<Evenement> list = new ArrayList<>();
        String req = "SELECT * FROM evenement";

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
                list.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur chargement événements : " + ex.getMessage());
        }

        return list;
    }

    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM evenement WHERE id = ?";
            PreparedStatement statement = MyDB.getInstance().getCon().prepareStatement(sql);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
