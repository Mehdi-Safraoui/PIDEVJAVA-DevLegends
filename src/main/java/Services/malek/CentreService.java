package Services.malek;

import Entities.malek.Centre;
import Interfaces.InterfaceCRUD;
import Utiles.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CentreService implements InterfaceCRUD<Centre> {
    Connection con;

    public CentreService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Centre centre) {
        String req = "INSERT INTO centre (nom_centre, adresse_centre, tel_centre, email_centre, specialite_centre, capacite_centre, photo_centre) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, centre.getNomCentre());
            pst.setString(2, centre.getAdresseCentre());
            pst.setString(3, centre.getTelCentre());
            pst.setString(4, centre.getEmailCentre());
            pst.setString(5, centre.getSpecialiteCentre());
            pst.setInt(6, centre.getCapaciteCentre());
            pst.setString(7, centre.getPhotoCentre());

            pst.executeUpdate();
            System.out.println("✅ Centre ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du centre : " + e.getMessage());
        }
    }

    @Override
    public void update(Centre centre) {
        String req = "UPDATE centre SET nom_centre=?, adresse_centre=?, tel_centre=?, email_centre=?, specialite_centre=?, capacite_centre=?, photo_centre=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, centre.getNomCentre());
            pst.setString(2, centre.getAdresseCentre());
            pst.setString(3, centre.getTelCentre());
            pst.setString(4, centre.getEmailCentre());
            pst.setString(5, centre.getSpecialiteCentre());
            pst.setInt(6, centre.getCapaciteCentre());
            pst.setString(7, centre.getPhotoCentre());
            pst.setInt(8, centre.getId());

            pst.executeUpdate();
            System.out.println("✅ Centre mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de mise à jour du centre : " + e.getMessage());
        }
    }

    @Override
    public void delete(Centre centre) {
        String req = "DELETE FROM centre WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, centre.getId());

            pst.executeUpdate();
            System.out.println("✅ Centre supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Centre> find() {
        String req = "SELECT * FROM centre";
        List<Centre> centres = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Centre c = new Centre();
                c.setId(rs.getInt("id"));
                c.setNomCentre(rs.getString("nom_centre"));
                c.setAdresseCentre(rs.getString("adresse_centre"));
                c.setTelCentre(rs.getString("tel_centre"));
                c.setEmailCentre(rs.getString("email_centre"));
                c.setSpecialiteCentre(rs.getString("specialite_centre"));
                c.setCapaciteCentre(rs.getInt("capacite_centre"));
                c.setPhotoCentre(rs.getString("photo_centre"));

                centres.add(c);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des centres : " + e.getMessage());
        }

        return centres;
    }

    public Centre findById(int id) {
        String req = "SELECT * FROM centre WHERE id=?";
        Centre centre = null;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                centre = new Centre();
                centre.setId(rs.getInt("id"));
                centre.setNomCentre(rs.getString("nom_centre"));
                centre.setAdresseCentre(rs.getString("adresse_centre"));
                centre.setTelCentre(rs.getString("tel_centre"));
                centre.setEmailCentre(rs.getString("email_centre"));
                centre.setSpecialiteCentre(rs.getString("specialite_centre"));
                centre.setCapaciteCentre(rs.getInt("capacite_centre"));
                centre.setPhotoCentre(rs.getString("photo_centre"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du centre par ID : " + e.getMessage());
        }

        return centre;
    }

    public List<String> getAllNomsCentres() {
        List<String> nomsCentres = new ArrayList<>();
        String req = "SELECT nom_centre FROM centre";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                nomsCentres.add(rs.getString("nom_centre"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des noms de centres: " + e.getMessage());
        }

        return nomsCentres;
    }

    public int getCentreIdByName(String nomCentre) {
        String req = "SELECT id FROM centre WHERE nom_centre = ?";
        int id = -1;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, nomCentre);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'ID du centre: " + e.getMessage());
        }

        return id;
    }

    // Méthode supplémentaire pour vérifier si un centre existe déjà
    public boolean centreExists(String nomCentre) {
        String req = "SELECT COUNT(*) FROM centre WHERE nom_centre = ?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, nomCentre);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification de l'existence du centre: " + e.getMessage());
        }

        return false;
    }
}