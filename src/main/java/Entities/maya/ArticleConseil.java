package Entities.maya;

public class ArticleConseil {
    private int id;
    private String titreArticle;
    private String contenuArticle;
    private String categorieMentalArticle;
    private String image;

    //Constructor
    public ArticleConseil() {
    }

    public ArticleConseil(int id, String titreArticle, String contenuArticle, String categorieMentalArticle, String image) {
        this.id = id;
        this.titreArticle = titreArticle;
        this.contenuArticle = contenuArticle;
        this.categorieMentalArticle = categorieMentalArticle;
        this.image = image;
    }

    public ArticleConseil(String titreArticle, String contenuArticle, String categorieMentalArticle, String image) {
        this.titreArticle = titreArticle;
        this.contenuArticle = contenuArticle;
        this.categorieMentalArticle = categorieMentalArticle;
        this.image = image;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitreArticle() {
        return titreArticle;
    }

    public void setTitreArticle(String titreArticle) {
        this.titreArticle = titreArticle;
    }

    public String getContenuArticle() {
        return contenuArticle;
    }

    public void setContenuArticle(String contenuArticle) {
        this.contenuArticle = contenuArticle;
    }

    public String getCategorieMentalArticle() {
        return categorieMentalArticle;
    }

    public void setCategorieMentalArticle(String categorieMentalArticle) {
        this.categorieMentalArticle = categorieMentalArticle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ArticleConseil{" +
                "id=" + id +
                ", titreArticle='" + titreArticle + '\'' +
                ", contenuArticle='" + contenuArticle + '\'' +
                ", categorieMentalArticle='" + categorieMentalArticle + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
