package Services;

import Entities.Commande;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements InterfaceCRUD<Commande> {
    Connection con;

    public CommandeService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Commande c) {
        String req = "INSERT INTO `commande` (`nom_client`, `adresse_email`, `date_commande`, `adresse`, `total_com`, `pays`, `num_telephone`, `payment_id`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, c.getNomClient());
            ps.setString(2, c.getAdresseEmail());
            ps.setDate(3, c.getDateCommande());
            ps.setString(4, c.getAdresse());
            ps.setDouble(5, c.getTotalCom());
            ps.setString(6, c.getPays());
            ps.setInt(7, c.getNumTelephone());
            //ps.setString(8, c.getPaymentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur ajout commande : " + e.getMessage());
        }
    }

    @Override
    public void update(Commande c) {
        String req = "UPDATE `commande` SET `nom_client`=?, `adresse_email`=?, `date_commande`=?, `adresse`=?, `total_com`=?, `pays`=?, `num_telephone`=? WHERE `id`=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, c.getNomClient());
            ps.setString(2, c.getAdresseEmail());
            ps.setDate(3, c.getDateCommande());
            ps.setString(4, c.getAdresse());
            ps.setDouble(5, c.getTotalCom());
            ps.setString(6, c.getPays());
            ps.setInt(7, c.getNumTelephone());

            ps.setInt(8, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur mise à jour commande : " + e.getMessage());
        }
    }

    @Override
    public void delete(Commande c) {
        String req = "DELETE FROM `commande` WHERE `id`=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur suppression commande : " + e.getMessage());
        }
    }

    @Override
    public List<Commande> find() {
        String req = "SELECT * FROM `commande`";
        List<Commande> commandes = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Commande c = new Commande();
                c.setId(rs.getInt("id"));
                c.setNomClient(rs.getString("nom_client"));
                c.setAdresseEmail(rs.getString("adresse_email"));
                c.setDateCommande(rs.getDate("date_commande"));
                c.setAdresse(rs.getString("adresse"));
                c.setTotalCom(rs.getDouble("total_com"));
                c.setPays(rs.getString("pays"));
                c.setNumTelephone(rs.getInt("num_telephone"));

                commandes.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération commandes : " + e.getMessage());
        }
        return commandes;
    }
}
