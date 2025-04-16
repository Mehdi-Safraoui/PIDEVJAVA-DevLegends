package Entities;
import java.util.Date;

public class Evenement {


    private int id;


    private String titreEvent;

    private Date dateEvent;

    private String lieuEvent;

    private String statutEvent;

    private Integer formationId;

    // Constructeurs
    public Evenement() {
    }

    public Evenement(String titreEvent, Date dateEvent, String lieuEvent, String statutEvent, Integer formationId) {
        this.titreEvent = titreEvent;
        this.dateEvent = dateEvent;
        this.lieuEvent = lieuEvent;
        this.statutEvent = statutEvent;
        this.formationId = formationId;
    }

    public Evenement(int id, String titreEvent, Date dateEvent, String lieuEvent, String statutEvent, Integer formationId) {
        this.id = id;
        this.titreEvent = titreEvent;
        this.dateEvent = dateEvent;
        this.lieuEvent = lieuEvent;
        this.statutEvent = statutEvent;
        this.formationId = formationId;
    }
    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitreEvent() {
        return titreEvent;
    }

    public void setTitreEvent(String titreEvent) {
        this.titreEvent = titreEvent;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getLieuEvent() {
        return lieuEvent;
    }

    public void setLieuEvent(String lieuEvent) {
        this.lieuEvent = lieuEvent;
    }

    public String getStatutEvent() {
        return statutEvent;
    }

    public void setStatutEvent(String statutEvent) {
        this.statutEvent = statutEvent;
    }

    public Integer getFormationId() {
        return formationId;
    }

    public void setFormationId(Integer formationId) {
        this.formationId = formationId;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", titreEvent='" + titreEvent + '\'' +
                ", dateEvent=" + dateEvent +
                ", lieuEvent='" + lieuEvent + '\'' +
                ", statutEvent='" + statutEvent + '\'' +
                ", formationId=" + formationId +
                '}';
    }
}
