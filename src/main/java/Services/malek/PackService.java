package Services.malek;

import Entities.malek.Pack;
import Interfaces.InterfaceCRUD;
import Utiles.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackService implements InterfaceCRUD<Pack> {
    Connection con;

    public PackService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Pack pack) {
        String req = "INSERT INTO pack (nom_pack, descript_pack, prix_pack, duree_pack, photo_pack, discount_code, is_used) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, pack.getNomPack());
            pst.setString(2, pack.getDescriptPack());
            pst.setDouble(3, pack.getPrixPack());
            pst.setString(4, pack.getDureePack());
            pst.setString(5, pack.getPhotoPack());
            pst.setString(6, pack.getDiscountCode());
            pst.setBoolean(7, pack.isUsed());

            pst.executeUpdate();
            System.out.println("✅ Pack ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du pack : " + e.getMessage());
        }
    }

    @Override
    public void update(Pack pack) {
        String req = "UPDATE pack SET nom_pack=?, descript_pack=?, prix_pack=?, duree_pack=?, photo_pack=?, discount_code=?, is_used=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, pack.getNomPack());
            pst.setString(2, pack.getDescriptPack());
            pst.setDouble(3, pack.getPrixPack());
            pst.setString(4, pack.getDureePack());
            pst.setString(5, pack.getPhotoPack());
            pst.setString(6, pack.getDiscountCode());
            pst.setBoolean(7, pack.isUsed());
            pst.setInt(8, pack.getId());

            pst.executeUpdate();
            System.out.println("✅ Pack mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de mise à jour du pack : " + e.getMessage());
        }
    }

    @Override
    public void delete(Pack pack) {
        String req = "DELETE FROM pack WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, pack.getId());

            pst.executeUpdate();
            System.out.println("✅ Pack supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression du pack : " + e.getMessage());
        }
    }

    @Override
    public List<Pack> find() {
        String req = "SELECT * FROM pack";
        List<Pack> packs = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Pack p = new Pack();
                p.setId(rs.getInt("id"));
                p.setNomPack(rs.getString("nom_pack"));
                p.setDescriptPack(rs.getString("descript_pack"));
                p.setPrixPack(rs.getDouble("prix_pack"));
                p.setDureePack(rs.getString("duree_pack"));
                p.setPhotoPack(rs.getString("photo_pack"));
                p.setDiscountCode(rs.getString("discount_code"));
                p.setUsed(rs.getBoolean("is_used"));

                packs.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des packs : " + e.getMessage());
        }

        return packs;
    }
}
