package Services;

import Entities.Contrat;
import Interfaces.InterfaceCRUD;
import Utiles.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratService implements InterfaceCRUD<Contrat> {
    Connection con;

    public ContratService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Contrat contrat) {
        String req = "INSERT INTO contrat (centre_id, datdeb_cont, datfin_cont, modpaiment_cont, renouv_auto_cont, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, contrat.getCentreId());
            pst.setDate(2, new java.sql.Date(contrat.getDatdebCont().getTime()));
            pst.setDate(3, new java.sql.Date(contrat.getDatfinCont().getTime()));
            pst.setString(4, contrat.getModpaimentCont());
            pst.setBoolean(5, contrat.isRenouvAutoCont());
            if (contrat.getUserId() != null) {
                pst.setInt(6, contrat.getUserId());
            } else {
                pst.setNull(6, Types.INTEGER);
            }

            pst.executeUpdate();
            System.out.println("âœ… Contrat ajoutÃ© avec succÃ¨s !");
        } catch (SQLException e) {
            System.out.println("âŒ Erreur lors de l'ajout du contrat : " + e.getMessage());
        }
    }

    @Override
    public void update(Contrat contrat) {
        String req = "UPDATE contrat SET centre_id=?, datdeb_cont=?, datfin_cont=?, modpaiment_cont=?, renouv_auto_cont=?, user_id=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, contrat.getCentreId());
            pst.setDate(2, new java.sql.Date(contrat.getDatdebCont().getTime()));
            pst.setDate(3, new java.sql.Date(contrat.getDatfinCont().getTime()));
            pst.setString(4, contrat.getModpaimentCont());
            pst.setBoolean(5, contrat.isRenouvAutoCont());
            if (contrat.getUserId() != null) {
                pst.setInt(6, contrat.getUserId());
            } else {
                pst.setNull(6, Types.INTEGER);
            }
            pst.setInt(7, contrat.getId());

            pst.executeUpdate();
            System.out.println("âœ… Contrat mis Ã  jour !");
        } catch (SQLException e) {
            System.out.println("âŒ Erreur de mise Ã  jour du contrat : " + e.getMessage());
        }
    }

    @Override
    public void delete(Contrat contrat) {
        String req = "DELETE FROM contrat WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, contrat.getId());

            pst.executeUpdate();
            System.out.println("âœ… Contrat supprimÃ© !");
        } catch (SQLException e) {
            System.out.println("âŒ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Contrat> find() {
        String req = "SELECT * FROM contrat";
        List<Contrat> contrats = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Contrat c = new Contrat();
                c.setId(rs.getInt("id"));
                c.setCentreId(rs.getInt("centre_id"));
                c.setDatdebCont(rs.getDate("datdeb_cont"));
                c.setDatfinCont(rs.getDate("datfin_cont"));
                c.setModpaimentCont(rs.getString("modpaiment_cont"));
                c.setRenouvAutoCont(rs.getBoolean("renouv_auto_cont"));
                c.setUserId(rs.getInt("user_id"));

                contrats.add(c);
            }
        } catch (SQLException e) {
            System.out.println("âŒ Erreur lors de la rÃ©cupÃ©ration des contrats : " + e.getMessage());
        }

        return contrats;
    }
}
