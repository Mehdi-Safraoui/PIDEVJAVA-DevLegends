package Entities.fatma;

public class Produit {
    private int id;
    private String nomProduit;
    private int prixProduit;
    private int quantite;
    private String statutProduit;
    private Categorie categorie;

    public Produit() {}

    public Produit(int id, String nomProduit, int prixProduit, int quantite, String statutProduit, Categorie categorie) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.quantite = quantite;
        this.statutProduit = statutProduit;
        this.categorie = categorie;
    }

    public Produit(String nomProduit, int prixProduit, int quantite, String statutProduit, Categorie categorie) {
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.quantite = quantite;
        this.statutProduit = statutProduit;
        this.categorie = categorie;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomProduit() { return nomProduit; }
    public void setNomProduit(String nomProduit) { this.nomProduit = nomProduit; }
    public int getPrixProduit() { return prixProduit; }
    public void setPrixProduit(int prixProduit) { this.prixProduit = prixProduit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public String getStatutProduit() { return statutProduit; }
    public void setStatutProduit(String statutProduit) { this.statutProduit = statutProduit; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    @Override
    public String toString() {
        return nomProduit;
    }
}
