package Entities.mehdi;

import java.util.Date;
import java.util.Objects;

public class Avis {
    private int id;
    private String sujetAvis;
    private String contenuAvis;
    private int noteAvis;
    private Date dateAvis;
    private String emailAvis;
    private String statutAvis;

    // Constructeur
    public Avis(int id, String sujetAvis, String contenuAvis, int noteAvis, Date dateAvis, String emailAvis, String statutAvis) {
        this.id = id;
        this.sujetAvis = sujetAvis;
        this.contenuAvis = contenuAvis;
        this.noteAvis = noteAvis;
        this.dateAvis = dateAvis;
        this.emailAvis = emailAvis;
        this.statutAvis = statutAvis;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSujetAvis() {
        return sujetAvis;
    }

    public void setSujetAvis(String sujetAvis) {
        this.sujetAvis = sujetAvis;
    }

    public String getContenuAvis() {
        return contenuAvis;
    }

    public void setContenuAvis(String contenuAvis) {
        this.contenuAvis = contenuAvis;
    }

    public int getNoteAvis() {
        return noteAvis;
    }

    public void setNoteAvis(int noteAvis) {
        this.noteAvis = noteAvis;
    }

    public Date getDateAvis() {
        return dateAvis;
    }

    public void setDateAvis(Date dateAvis) {
        this.dateAvis = dateAvis;
    }

    public String getEmailAvis() {
        return emailAvis;
    }

    public void setEmailAvis(String emailAvis) {
        this.emailAvis = emailAvis;
    }

    public String getStatutAvis() {
        return statutAvis;
    }

    public void setStatutAvis(String statutAvis) {
        this.statutAvis = statutAvis;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", sujetAvis='" + sujetAvis + '\'' +
                ", contenuAvis='" + contenuAvis + '\'' +
                ", noteAvis=" + noteAvis +
                ", dateAvis=" + dateAvis +
                ", emailAvis='" + emailAvis + '\'' +
                ", statutAvis='" + statutAvis + '\'' +
                '}';
    }

    // Méthode equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Avis that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getSujetAvis(), that.getSujetAvis()) &&
                Objects.equals(getContenuAvis(), that.getContenuAvis()) &&
                getNoteAvis() == that.getNoteAvis() &&
                Objects.equals(getDateAvis(), that.getDateAvis()) &&
                Objects.equals(getEmailAvis(), that.getEmailAvis()) &&
                Objects.equals(getStatutAvis(), that.getStatutAvis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSujetAvis(), getContenuAvis(), getNoteAvis(), getDateAvis(), getEmailAvis(), getStatutAvis());
    }
}
