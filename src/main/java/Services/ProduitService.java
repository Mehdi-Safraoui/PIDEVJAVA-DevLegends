package Services;

import Entities.Categorie;
import Entities.Produit;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService {
    private Connection con;

    public ProduitService() {
        con = MyDB.getInstance().getCon();
    }

    public void add(Produit p) {
        String req = "INSERT INTO produit (nom_produit, prix_produit, qte_produit, statut_produit, categorie_produit_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, p.getNomProduit());
            ps.setInt(2, p.getPrixProduit());
            ps.setInt(3, p.getQuantite());
            ps.setString(4, p.getStatutProduit());
            ps.setInt(5, p.getCategorie().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur ajout produit : " + e.getMessage());
        }
    }

    public void update(Produit p) {
        String req = "UPDATE produit SET nom_produit=?, prix_produit=?, qte_produit=?, statut_produit=?, categorie_produit_id=? WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, p.getNomProduit());
            ps.setInt(2, p.getPrixProduit());
            ps.setInt(3, p.getQuantite());
            ps.setString(4, p.getStatutProduit());
            ps.setInt(5, p.getCategorie().getId());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur update produit : " + e.getMessage());
        }
    }

    public void delete(Produit p) {
        String req = "DELETE FROM produit WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur delete produit : " + e.getMessage());
        }
    }

    public List<Produit> find() {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, c.id as c_id, c.nom_categorie FROM produit p JOIN categorie c ON p.categorie_produit_id = c.id";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("c_id"), rs.getString("nom_categorie"));
                Produit p = new Produit(rs.getInt("id"), rs.getString("nom_produit"), rs.getInt("prix_produit"),
                        rs.getInt("qte_produit"), rs.getString("statut_produit"), cat);
                produits.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll produit : " + e.getMessage());
        }
        return produits;
    }
}
