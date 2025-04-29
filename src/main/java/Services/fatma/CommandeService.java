package Services.fatma;

import Entities.fatma.Commande;
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
        String req = "INSERT INTO `commande` (`user_id`, `nom_client`, `adresse_email`, `date_commande`, `adresse`, `total_com`, `pays`, `num_telephone`, `payment_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, c.getUserId()); // 👈 Utilisateur lié
            ps.setString(2, c.getNomClient());
            ps.setString(3, c.getAdresseEmail());
            ps.setDate(4, c.getDateCommande());
            ps.setString(5, c.getAdresse());
            ps.setDouble(6, c.getTotalCom());
            ps.setString(7, c.getPays());
            ps.setInt(8, c.getNumTelephone());
            ps.setString(9, c.getPaymentId());
            ps.executeUpdate();
            System.out.println("✅ Commande ajoutée avec succès !");

        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout commande : " + e.getMessage());
        }
    }


    @Override
    public void update(Commande c) {
        String req = "UPDATE `commande` SET `user_id`=?, `nom_client`=?, `adresse_email`=?, `date_commande`=?, `adresse`=?, `total_com`=?, `pays`=?, `num_telephone`=?, `payment_id`=? WHERE `id`=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getNomClient());
            ps.setString(3, c.getAdresseEmail());
            ps.setDate(4, c.getDateCommande());
            ps.setString(5, c.getAdresse());
            ps.setDouble(6, c.getTotalCom());
            ps.setString(7, c.getPays());
            ps.setInt(8, c.getNumTelephone());
            ps.setString(9, c.getPaymentId());
            ps.setInt(10, c.getId());
            ps.executeUpdate();
            System.out.println("✅ Commande mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur mise à jour commande : " + e.getMessage());
        }
    }

    @Override
    public void delete(Commande c) {
        String req = "DELETE FROM `commande` WHERE `id`=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, c.getId());
            ps.executeUpdate();
            System.out.println("✅ Commande supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression commande : " + e.getMessage());
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
                c.setUserId(rs.getInt("user_id"));
                c.setNomClient(rs.getString("nom_client"));
                c.setAdresseEmail(rs.getString("adresse_email"));
                c.setDateCommande(rs.getDate("date_commande"));
                c.setAdresse(rs.getString("adresse"));
                c.setTotalCom(rs.getDouble("total_com"));
                c.setPays(rs.getString("pays"));
                c.setNumTelephone(rs.getInt("num_telephone"));
                c.setPaymentId(rs.getString("payment_id"));
                commandes.add(c);
            }
            System.out.println("✅ Récupération des commandes terminée.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur récupération commandes : " + e.getMessage());
        }
        return commandes;
    }

    public List<Commande> findByNomClient(String nomClient) {
        String req = "SELECT * FROM `commande` WHERE `nom_client` LIKE ?";
        List<Commande> commandes = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, "%" + nomClient + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Commande c = new Commande();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setNomClient(rs.getString("nom_client"));
                c.setAdresseEmail(rs.getString("adresse_email"));
                c.setDateCommande(rs.getDate("date_commande"));
                c.setAdresse(rs.getString("adresse"));
                c.setTotalCom(rs.getDouble("total_com"));
                c.setPays(rs.getString("pays"));
                c.setNumTelephone(rs.getInt("num_telephone"));
                c.setPaymentId(rs.getString("payment_id"));
                commandes.add(c);
            }
            System.out.println("✅ Recherche des commandes du client '" + nomClient + "' réussie !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche commande : " + e.getMessage());
        }
        return commandes;
    }
}
