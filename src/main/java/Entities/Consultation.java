package Entities;

import java.util.Date;

public class Consultation {
    private int id;
    private Date dateCons;
    private String lienVisioCons;
    private String notesCons;
    private String nom;
    private String prenom;
    private int age;

    // Constructor
    public Consultation() {
    }

    public Consultation(int id, Date dateCons, String lienVisioCons, String notesCons, String nom, String prenom, int age) {
        this.id = id;
        this.dateCons = dateCons;
        this.lienVisioCons = lienVisioCons;
        this.notesCons = notesCons;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    public Consultation(Date dateCons, String lienVisioCons, String notesCons, String nom, String prenom, int age) {
        this.dateCons = dateCons;
        this.lienVisioCons = lienVisioCons;
        this.notesCons = notesCons;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCons() {
        return dateCons;
    }

    public void setDateCons(Date dateCons) {
        this.dateCons = dateCons;
    }

    public String getLienVisioCons() {
        return lienVisioCons;
    }

    public void setLienVisioCons(String lienVisioCons) {
        this.lienVisioCons = lienVisioCons;
    }

    public String getNotesCons() {
        return notesCons;
    }

    public void setNotesCons(String notesCons) {
        this.notesCons = notesCons;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", dateCons=" + dateCons +
                ", lienVisioCons='" + lienVisioCons + '\'' +
                ", notesCons='" + notesCons + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                '}';
    }
}
