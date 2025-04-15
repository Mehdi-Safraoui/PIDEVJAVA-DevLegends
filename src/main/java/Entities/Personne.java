package entities;

public class Personne
{
    private int id;
    private String nom;
    private String prenom;

    Personne(){}
    Personne(int id, String nom, String prenom){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }
    Personne( String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }
    public int getId(){
        return id;
    }
    public String getNom(){
        return nom;
    }
    public String getPrenom(){
        return prenom;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    @Override
    public String toString(){
        return "ID: " + id + " Nom: " + nom + " Prenom: " + prenom;
    }

}
