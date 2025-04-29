package Entities.fatma;

public class PanierItem {
    private Produit produit;
    private int quantite;

    public PanierItem(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }

    public Produit getProduit() {
        return produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void incrementer() {
        this.quantite++;
    }

    public void decrementer() {
        if (this.quantite > 1) {
            this.quantite--;
        }
    }

    public double getTotalPrix() {
        double totalPrix = produit.getPrixProduit() * quantite;
        if (totalPrix < 0) {
            totalPrix = 0;  // Prévenir des valeurs négatives
        }
        return totalPrix;
    }
}
