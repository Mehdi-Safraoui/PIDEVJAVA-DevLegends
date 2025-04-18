package Services.maya;

import Entities.maya.ArticleConseil;
import Interfaces.InterfaceCRUD;
import Utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ArticleConseilService implements InterfaceCRUD<ArticleConseil> {
    private final Connection con;

    public ArticleConseilService() {
        con = MyDB.getInstance().getCon();
    }

    @Override
    public void add(ArticleConseil articleConseil) {
        String req = "INSERT INTO articles_conseils (titre_article, contenu_article, categorie_mental_article, image) " +
                "VALUES (?, ?, ?, ?)";

        // Copier l'image dans le dossier local
        String imagePath = null;
        if (articleConseil.getImage() != null && !articleConseil.getImage().isEmpty()) {
            try {
                // Le chemin où tu veux stocker l'image
                Path storagePath = Path.of("images/", new File(articleConseil.getImage()).getName());

                // Copier l'image dans le répertoire "images/"
                Files.copy(Path.of(articleConseil.getImage()), storagePath, StandardCopyOption.REPLACE_EXISTING);

                // Récupérer le chemin relatif de l'image pour l'enregistrer dans la base de données
                imagePath = storagePath.toString();
            } catch (IOException e) {
                System.out.println("❌ Erreur lors de l'upload de l'image : " + e.getMessage());
            }
        }

        // Enregistrer l'article avec le chemin de l'image dans la base de données
        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, articleConseil.getTitreArticle());
            pst.setString(2, articleConseil.getContenuArticle());
            pst.setString(3, articleConseil.getCategorieMentalArticle());
            pst.setString(4, imagePath);

            pst.executeUpdate();
            System.out.println("✅ Article ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'article : " + e.getMessage());
        }
    }

    @Override
    public void update(ArticleConseil articleConseil) {
        String imagePath = articleConseil.getImage();

        // Si une nouvelle image est sélectionnée (vérifie si le chemin ne commence pas déjà par "images/")
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.startsWith("images/")) {
            try {
                Path storagePath = Path.of("images/", new File(imagePath).getName());

                // Copier l'image dans le répertoire "images/"
                Files.copy(Path.of(imagePath), storagePath, StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour le chemin relatif
                imagePath = storagePath.toString();
            } catch (IOException e) {
                System.out.println("❌ Erreur lors de l'upload de la nouvelle image : " + e.getMessage());
            }
        }

        // Mise à jour de la base
        String req = "UPDATE articles_conseils SET titre_article=?, contenu_article=?, categorie_mental_article=?, image=? WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setString(1, articleConseil.getTitreArticle());
            pst.setString(2, articleConseil.getContenuArticle());
            pst.setString(3, articleConseil.getCategorieMentalArticle());
            pst.setString(4, imagePath);
            pst.setInt(5, articleConseil.getId());

            pst.executeUpdate();
            System.out.println("✅ Article mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'article : " + e.getMessage());
        }
    }

//    @Override
//    public void update(ArticleConseil articleConseil) {
//        String req = "UPDATE articles_conseils SET titre_article=?, contenu_article=?, categorie_mental_article=?, image=? WHERE id=?";
//
//        try {
//            PreparedStatement pst = con.prepareStatement(req);
//            pst.setString(1, articleConseil.getTitreArticle());
//            pst.setString(2, articleConseil.getContenuArticle());
//            pst.setString(3, articleConseil.getCategorieMentalArticle());
//            pst.setString(4, articleConseil.getImage());
//            pst.setInt(5, articleConseil.getId());
//
//            pst.executeUpdate();
//            System.out.println("✅ Article mis à jour !");
//        } catch (SQLException e) {
//            System.out.println("❌ Erreur lors de la mise à jour de l'article : " + e.getMessage());
//        }
//    }

    @Override
    public void delete(ArticleConseil articleConseil) {
        String req = "DELETE FROM articles_conseils WHERE id=?";

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, articleConseil.getId());

            pst.executeUpdate();
            System.out.println("✅ Article supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de l'article : " + e.getMessage());
        }
    }

    @Override
    public List<ArticleConseil> find() {
        String req = "SELECT * FROM articles_conseils";
        List<ArticleConseil> articlesConseils = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                ArticleConseil articleConseil = new ArticleConseil();
                articleConseil.setId(rs.getInt("id"));
                articleConseil.setTitreArticle(rs.getString("titre_article"));
                articleConseil.setContenuArticle(rs.getString("contenu_article"));
                articleConseil.setCategorieMentalArticle(rs.getString("categorie_mental_article"));
                articleConseil.setImage(rs.getString("image"));

                articlesConseils.add(articleConseil);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des articles : " + e.getMessage());
        }

        return articlesConseils;
    }

    public ArticleConseil findById(int id) {
        String req = "SELECT * FROM articles_conseils WHERE id=?";
        ArticleConseil articleConseil = null;

        try {
            PreparedStatement pst = con.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                articleConseil = new ArticleConseil();
                articleConseil.setId(rs.getInt("id"));
                articleConseil.setTitreArticle(rs.getString("titre_article"));
                articleConseil.setContenuArticle(rs.getString("contenu_article"));
                articleConseil.setCategorieMentalArticle(rs.getString("categorie_mental_article"));
                articleConseil.setImage(rs.getString("image"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'article par ID : " + e.getMessage());
        }

        return articleConseil;
    }

    public List<ArticleConseil> recuperer() {
        List<ArticleConseil> articles = new ArrayList<>();
        String req = "SELECT * FROM article_conseil";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                ArticleConseil a = new ArticleConseil();
                a.setId(rs.getInt("id"));
                a.setTitreArticle(rs.getString("titre_article"));
                a.setContenuArticle(rs.getString("contenu_article"));
                a.setCategorieMentalArticle(rs.getString("categorie_mental_article"));
                a.setImage(rs.getString("image"));
                articles.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    public List<ArticleConseil> afficher() {
        List<ArticleConseil> articlesConseils = new ArrayList<>();
        String req = "SELECT * FROM articles_conseils";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                ArticleConseil article = new ArticleConseil();
                article.setId(rs.getInt("id"));
                article.setTitreArticle(rs.getString("titre_article"));
                article.setContenuArticle(rs.getString("contenu_article"));
                article.setCategorieMentalArticle(rs.getString("categorie_mental_article"));
                article.setImage(rs.getString("image"));
                articlesConseils.add(article);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'affichage des articles : " + e.getMessage());
        }

        return articlesConseils;
    }

    public boolean isTitreUnique(String titre) {
        String sql = "SELECT COUNT(*) FROM articleconseil WHERE titre_article = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, titre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Si erreur, mieux vaut empêcher l'ajout
    }


}
