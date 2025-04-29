package Services.salsabil;

import Entities.salsabil.User;
import Entities.salsabil.Profil;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserService implements InterfaceCRUD<User> {
    Connection con;

    public UserService() {
        con = MyDB.getInstance().getCon();
    }
    public boolean isPhoneExists(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM user WHERE num_tel = ?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, phoneNumber);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification du numéro : " + e.getMessage());
        }
        return false;
    }
    public void bannirUtilisateur(int userId, LocalDateTime bannedUntil) {
        String sql = "UPDATE user SET banned_until = ?, statut_compte = false WHERE id = ?";
        try (Connection conn = MyDB.getInstance().getCon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(bannedUntil));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void liftBan(int userId) {
        String sql = "UPDATE user SET banned_until = NULL WHERE id = " + userId;
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la levée du bannissement : " + e.getMessage());
        }
    }


    @Override
    public void add(User user) {
        String req = "INSERT INTO user (first_name, last_name, user_email, password, user_role, user_age, doc_specialty, num_tel, address, statut_compte, user_picture, roles) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getFirst_name());
            pst.setString(2, user.getLast_name());
            pst.setString(3, user.getUser_email());

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            pst.setString(4, hashedPassword);

            pst.setString(5, user.getUser_role());
            pst.setInt(6, user.getUser_age());
            pst.setString(7, user.getDoc_specialty());
            pst.setString(8, user.getNum_tel());
            pst.setString(9, user.getAddress());
            pst.setBoolean(10, user.isStatut_compte());
            pst.setString(11, user.getUser_picture());
            String role = user.getUser_role().toLowerCase();
            String rolesJson;
            switch (role) {
                case "medecin":
                    rolesJson = "[\"ROLE_USER\", \"ROLE_MEDECIN\"]";
                    break;
                case "patient":
                    rolesJson = "[\"ROLE_USER\", \"ROLE_PATIENT\"]";
                    break;
                case "admin":
                    rolesJson = "[\"ROLE_ADMIN\"]";
                    break;
                default:
                    rolesJson = "[\"ROLE_USER\"]";
            }
            user.setRoles(rolesJson);
            pst.setString(12, rolesJson);

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1)); // Important pour lier le profil à cet utilisateur
            }

            createOrUpdateProfilForUser(user);

            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }
    @Override
    public void update(User user) {
        String updateUserQuery = "UPDATE user SET first_name=?, last_name=?, user_email=?, password=?, user_role=?, user_age=?, doc_specialty=?, num_tel=?, address=?, user_picture=?, statut_compte=? WHERE id=?";
        String updateProfilQuery = "UPDATE profil SET first_name=?, last_name=?, user_role=?, user_age=?, user_picture=?, doc_specialty=? WHERE user_id=?";

        try {
            // Début de la transaction
            con.setAutoCommit(false);

            // Mettre à jour l'utilisateur
            PreparedStatement pstUser = con.prepareStatement(updateUserQuery);
            pstUser.setString(1, user.getFirst_name());
            pstUser.setString(2, user.getLast_name());
            pstUser.setString(3, user.getUser_email());
            pstUser.setString(4, user.getPassword());
            pstUser.setString(5, user.getUser_role());
            pstUser.setInt(6, user.getUser_age());
            pstUser.setString(7, user.getDoc_specialty());
            pstUser.setString(8, user.getNum_tel());
            pstUser.setString(9, user.getAddress());
            pstUser.setString(10, user.getUser_picture());
            pstUser.setBoolean(11, user.isStatut_compte());
            pstUser.setInt(12, user.getId());
            pstUser.executeUpdate();

            // Mettre à jour le profil de l'utilisateur
            PreparedStatement pstProfil = con.prepareStatement(updateProfilQuery);
            pstProfil.setString(1, user.getFirst_name());
            pstProfil.setString(2, user.getLast_name());
            pstProfil.setString(3, user.getUser_role());
            pstProfil.setInt(4, user.getUser_age());
            pstProfil.setString(5, user.getUser_picture());
            pstProfil.setString(6, user.getDoc_specialty());
            pstProfil.setInt(7, user.getId());
            pstProfil.executeUpdate();

            // Commit de la transaction
            con.commit();
            System.out.println("✅ Utilisateur et profil mis à jour avec succès !");
        } catch (SQLException e) {
            try {
                // En cas d'erreur, on annule la transaction
                con.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("❌ Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            System.out.println("❌ Erreur lors de la mise à jour des données : " + e.getMessage());
        } finally {
            try {
                // Restauration du mode auto-commit
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de la restauration du mode auto-commit : " + e.getMessage());
            }
        }
    }
    public User findByEmail(String email) {
        String req = "SELECT * FROM user WHERE user_email = ?";
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("user_email"),
                        rs.getString("password"),
                        rs.getString("user_role"),
                        rs.getInt("user_age"),
                        rs.getString("user_picture"),
                        rs.getString("doc_specialty"),
                        rs.getString("roles"),
                        rs.getBoolean("statut_compte"),
                        rs.getString("num_tel"),
                        rs.getString("discount_code"),
                        rs.getBoolean("is_discount_used"),
                        rs.getString("reset_code"),
                        rs.getTimestamp("banned_until") != null ? rs.getTimestamp("banned_until").toLocalDateTime() : null,
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("address")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    /*
    @Override
    public void update(User user) {
        String req = "UPDATE user SET first_name=?, last_name=?, user_email=?, password=?, user_role=?, user_age=?, doc_specialty=?, num_tel=?, address=?, user_picture=?, statut_compte=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, user.getFirst_name());
            pst.setString(2, user.getLast_name());
            pst.setString(3, user.getUser_email());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getUser_role());
            pst.setInt(6, user.getUser_age());
            pst.setString(7, user.getDoc_specialty());
            pst.setString(8, user.getNum_tel());
            pst.setString(9, user.getAddress());
            pst.setString(10, user.getUser_picture());
            pst.setBoolean(11, user.isStatut_compte());
            pst.setInt(12, user.getId());

            pst.executeUpdate();

            createOrUpdateProfilForUser(user);

            System.out.println("✅ Utilisateur mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de mise à jour de l'utilisateur : " + e.getMessage());
        }
    }
*/
    private void createOrUpdateProfilForUser(User user) {
        ProfilService profilService = new ProfilService();
        Profil profil = new Profil(user);

        Profil existing = profilService.findByUserId(user.getId());
        if (existing != null) {
            profil.setId(existing.getId());
            profilService.update(profil);
        } else {
            profilService.add(profil);
        }

    }

    @Override
    public void delete(User user) {
        String req = "DELETE FROM user WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, user.getId());

            pst.executeUpdate();
            System.out.println("✅ Utilisateur supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<User> find() {
        String req = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                User u = new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("user_email"),
                        rs.getString("password"),
                        rs.getString("user_role"),
                        rs.getInt("user_age"),
                        rs.getString("doc_specialty"),
                        rs.getString("num_tel"),
                        rs.getString("address"),
                        rs.getString("user_picture")
                );
                u.setId(rs.getInt("id"));
                u.setStatut_compte(rs.getBoolean("statut_compte"));
                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }
}
