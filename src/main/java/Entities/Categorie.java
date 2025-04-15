package Entities;

import java.util.Objects;

public class Categorie {
    private int id;
    private String nom_categorie;

    public Categorie() {}

    public Categorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    public Categorie(int id, String nom_categorie) {
        this.id = id;
        this.nom_categorie = nom_categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nom_categorie;
    }

    public void setNomCategorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom_categorie='" + nom_categorie + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categorie categorie)) return false;
        return id == categorie.id && Objects.equals(nom_categorie, categorie.nom_categorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom_categorie);
    }
}

