package Services.fatma;

import Entities.fatma.Categorie;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements InterfaceCRUD<Categorie> {
    Connection con;

    public CategorieService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Categorie c) {
        String req = "INSERT INTO `categorie` (`nom_categorie`) VALUES (?)";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, c.getNomCategorie());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur ajout catégorie : " + e.getMessage());
        }
    }

    @Override
    public void update(Categorie c) {
        String req = "UPDATE `categorie` SET `nom_categorie`=? WHERE `id`=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, c.getNomCategorie());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur mise à jour catégorie : " + e.getMessage());
        }
    }

    @Override
    public void delete(Categorie c) {
        String req = "DELETE FROM `categorie` WHERE `id`=?";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur suppression catégorie : " + e.getMessage());
        }
    }

    @Override
    public List<Categorie> find() {
        List<Categorie> categories = new ArrayList<>();
        String req = "SELECT * FROM `categorie`";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Categorie c = new Categorie();
                c.setId(rs.getInt("id"));
                c.setNomCategorie(rs.getString("nom_categorie"));
                categories.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération catégories : " + e.getMessage());
        }

        return categories;
    }


}
