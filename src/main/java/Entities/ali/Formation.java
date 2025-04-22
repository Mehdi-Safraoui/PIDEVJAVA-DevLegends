package Entities.ali;

import java.util.Date;

public class Formation {
    private int id;
    private String titreFor;
    private Date dateFor;
    private String lieuFor;
    private String statutFor;

    // Constructeurs
    public Formation() {
    }

    public Formation(String titreFor, Date dateFor, String lieuFor, String statutFor) {
        this.titreFor = titreFor;
        this.dateFor = dateFor;
        this.lieuFor = lieuFor;
        this.statutFor = statutFor;
    }

    public Formation(int id, String titreFor, Date dateFor, String lieuFor, String statutFor) {
        this.id = id;
        this.titreFor = titreFor;
        this.dateFor = dateFor;
        this.lieuFor = lieuFor;
        this.statutFor = statutFor;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitreFor() {
        return titreFor;
    }

    public void setTitreFor(String titreFor) {
        this.titreFor = titreFor;
    }

    public Date getDateFor() {
        return dateFor;
    }

    public void setDateFor(Date dateFor) {
        this.dateFor = dateFor;
    }

    public String getLieuFor() {
        return lieuFor;
    }

    public void setLieuFor(String lieuFor) {
        this.lieuFor = lieuFor;
    }

    public String getStatutFor() {
        return statutFor;
    }

    public void setStatutFor(String statutFor) {
        this.statutFor = statutFor;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", titreFor='" + titreFor + '\'' +
                ", dateFor=" + dateFor +
                ", lieuFor='" + lieuFor + '\'' +
                ", statutFor='" + statutFor + '\'' +
                '}';
    }
}
