package Services.fatma;

import Entities.fatma.Categorie;
import Entities.fatma.Produit;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService {
    private Connection con;

    public ProduitService() {
        con = MyDB.getInstance().getCon();
    }

    // Ajouter un produit
    public void add(Produit p) {
        if (p.getQuantite() == 0) {
            p.setStatutProduit("Indisponible");
        }


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


    // Mettre à jour un produit
    public void update(Produit p) {
        if (p.getQuantite() == 0) {
            p.setStatutProduit("Indisponible");
        }

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

    // Supprimer un produit
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

    // Chercher tous les produits
    public List<Produit> find() {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, c.id as c_id, c.nom_categorie FROM produit p JOIN categorie c ON p.categorie_produit_id = c.id";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("c_id"), rs.getString("nom_categorie"));
                Produit p = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom_produit"),
                        rs.getInt("prix_produit"),
                        rs.getInt("qte_produit"),
                        rs.getString("statut_produit"),
                        cat
                );
                produits.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll produit : " + e.getMessage());
        }
        return produits;
    }

    // Trouver un produit par ID
    public Produit findById(int id) {
        Produit produit = null;
        String req = "SELECT p.*, c.id as c_id, c.nom_categorie FROM produit p JOIN categorie c ON p.categorie_produit_id = c.id WHERE p.id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("c_id"), rs.getString("nom_categorie"));
                produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom_produit"),
                        rs.getInt("prix_produit"),
                        rs.getInt("qte_produit"),
                        rs.getString("statut_produit"),
                        cat
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur findById produit : " + e.getMessage());
        }
        return produit;
    }

    // Récupérer les produits par catégorie
    public List<Produit> findByCategorie(String categorie) {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, c.id as c_id, c.nom_categorie FROM produit p JOIN categorie c ON p.categorie_produit_id = c.id WHERE c.nom_categorie = ?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, categorie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("c_id"), rs.getString("nom_categorie"));
                Produit p = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom_produit"),
                        rs.getInt("prix_produit"),
                        rs.getInt("qte_produit"),
                        rs.getString("statut_produit"),
                        cat
                );
                produits.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur findByCategorie produit : " + e.getMessage());
        }
        return produits;
    }

    // Récupérer toutes les catégories distinctes
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        String req = "SELECT DISTINCT nom_categorie FROM categorie";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                categories.add(rs.getString("nom_categorie"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAllCategories : " + e.getMessage());
        }
        return categories;
    }
}
