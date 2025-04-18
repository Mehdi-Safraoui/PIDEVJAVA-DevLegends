package Services.salsabil;

import Entities.salsabil.Profil;
import Entities.salsabil.User;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfilService implements InterfaceCRUD<Profil> {
    Connection con;

    public ProfilService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(Profil profil) {
        String req = "INSERT INTO profil (user_id, first_name, last_name, user_role, user_age, user_picture, doc_specialty) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, profil.getUser().getId());
            pst.setString(2, profil.getFirst_name());
            pst.setString(3, profil.getLast_name());
            pst.setString(4, profil.getUser_role());
            pst.setInt(5, profil.getUser_age());
            pst.setString(6, profil.getUser_picture());
            pst.setString(7, profil.getDoc_specialty());

            pst.executeUpdate();
            System.out.println("✅ Profil ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du profil : " + e.getMessage());
        }
    }

    // ✅ NOUVELLE MÉTHODE POUR CRÉER UN PROFIL À PARTIR D’UN UTILISATEUR
    public void addFromUser(User user) {
        Profil profil = new Profil();
        profil.setUser(user);
        profil.setFirst_name(user.getFirst_name());
        profil.setLast_name(user.getLast_name());
        profil.setUser_role(user.getUser_role());
        profil.setUser_age(user.getUser_age());
        profil.setUser_picture(user.getUser_picture());
        profil.setDoc_specialty(user.getDoc_specialty());

        this.add(profil); // On appelle la méthode add(Profil)
    }

    @Override
    public void update(Profil profil) {
        String req = "UPDATE profil SET first_name=?, last_name=?, user_role=?, user_age=?, user_picture=?, doc_specialty=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, profil.getFirst_name());
            pst.setString(2, profil.getLast_name());
            pst.setString(3, profil.getUser_role());
            pst.setInt(4, profil.getUser_age());
            pst.setString(5, profil.getUser_picture());
            pst.setString(6, profil.getDoc_specialty());
            pst.setInt(7, profil.getId());

            pst.executeUpdate();
            System.out.println("✅ Profil mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour du profil : " + e.getMessage());
        }
    }

    @Override
    public void delete(Profil profil) {
        String req = "DELETE FROM profil WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, profil.getId());

            pst.executeUpdate();
            System.out.println("✅ Profil supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }
    public Profil findByUserId(int userId) {
        String req = "SELECT * FROM profil WHERE user_id=?";
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Profil p = new Profil();
                p.setId(rs.getInt("id"));
                p.setFirst_name(rs.getString("first_name"));
                p.setLast_name(rs.getString("last_name"));
                p.setUser_role(rs.getString("user_role"));
                p.setUser_age(rs.getInt("user_age"));
                p.setUser_picture(rs.getString("user_picture"));
                p.setDoc_specialty(rs.getString("doc_specialty"));
                User u = new User(); u.setId(userId); // raccourci
                p.setUser(u);
                return p;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur findByUserId : " + e.getMessage());
        }
        return null;
    }


    @Override
    public List<Profil> find() {
        String req = "SELECT * FROM profil";
        List<Profil> profils = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Profil p = new Profil();
                p.setId(rs.getInt("id"));
                p.setFirst_name(rs.getString("first_name"));
                p.setLast_name(rs.getString("last_name"));
                p.setUser_role(rs.getString("user_role"));
                p.setUser_age(rs.getInt("user_age"));
                p.setUser_picture(rs.getString("user_picture"));
                p.setDoc_specialty(rs.getString("doc_specialty"));

                profils.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des profils : " + e.getMessage());
        }

        return profils;
    }
}
