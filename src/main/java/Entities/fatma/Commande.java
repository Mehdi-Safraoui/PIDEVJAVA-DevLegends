package Entities.fatma;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class Commande {
    private int id;
    private String nom_client;
    private String adresse_email;
    private Date date_commande;
    private String adresse;
    private double total_com;
    private String pays;
    private int num_telephone;
    private String paymentId;


    private int userId;  // FK vers User

    // Relation OneToMany pour lier plusieurs produits à une commande
    private List<Produit> produits;

    public Commande() {}

    public Commande(String nom_client, String adresse_email, Date date_commande, String adresse, double total_com, String pays, int num_telephone) {
        this.nom_client = nom_client;
        this.adresse_email = adresse_email;
        this.date_commande = date_commande;
        this.adresse = adresse;
        this.total_com = total_com;
        this.pays = pays;
        this.num_telephone = num_telephone;
    }

    public Commande(int id, String nom_client, String adresse_email, Date date_commande, String adresse, double total_com, String pays, int num_telephone) {
        this.id = id;
        this.nom_client = nom_client;
        this.adresse_email = adresse_email;
        this.date_commande = date_commande;
        this.adresse = adresse;
        this.total_com = total_com;
        this.pays = pays;
        this.num_telephone = num_telephone;
    }

    // Calculer le total de la commande à partir des produits
    public double calculerTotal() {
        double total = 0.0;
        for (Produit produit : produits) {
            total += produit.getPrixProduit() * produit.getQuantite();
        }
        return total;
    }

    // Getters et Setters
    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomClient() {
        return nom_client;
    }

    public void setNomClient(String nomClient) {
        this.nom_client = nomClient;
    }

    public String getAdresseEmail() {
        return adresse_email;
    }

    public void setAdresseEmail(String adresse_email) {
        this.adresse_email = adresse_email;
    }

    public Date getDateCommande() {
        return date_commande;
    }

    public void setDateCommande(Date date_commande) {
        this.date_commande = date_commande;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getTotalCom() {
        return total_com;
    }

    public void setTotalCom(double total_com) {
        this.total_com = total_com;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getNumTelephone() {
        return num_telephone;
    }

    public void setNumTelephone(int numTelephone) {
        this.num_telephone = numTelephone;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", nom_client='" + nom_client + '\'' +
                ", adresse_email='" + adresse_email + '\'' +
                ", date_commande=" + date_commande +
                ", adresse='" + adresse + '\'' +
                ", total_com=" + total_com +
                ", pays='" + pays + '\'' +
                ", num_telephone=" + num_telephone +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commande commande)) return false;
        return id == commande.id &&
                Double.compare(commande.total_com, total_com) == 0 &&
                num_telephone == commande.num_telephone &&
                Objects.equals(nom_client, commande.nom_client) &&
                Objects.equals(adresse_email, commande.adresse_email) &&
                Objects.equals(date_commande, commande.date_commande) &&
                Objects.equals(adresse, commande.adresse) &&
                Objects.equals(pays, commande.pays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom_client, adresse_email, date_commande, adresse, total_com, pays, num_telephone);
    }
}
