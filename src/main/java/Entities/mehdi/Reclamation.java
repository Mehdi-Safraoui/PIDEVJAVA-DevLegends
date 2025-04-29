package Entities.mehdi;

import java.util.Date;
import java.util.Objects;

public class Reclamation {
    private int id;
    private String sujetRec;
    private String contenuRec;
    private Date dateRec;
    private String emailRec;
    private String statutRec;

    public Reclamation(int id, String sujetRec, String contenuRec, Date dateRec, String emailRec, String statutRec) {
        this.id = id;
        this.sujetRec = sujetRec;
        this.contenuRec = contenuRec;
        this.dateRec = dateRec;
        this.emailRec = emailRec;
        this.statutRec = statutRec;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSujetRec() {
        return sujetRec;
    }

    public void setSujetRec(String sujetRec) {
        this.sujetRec = sujetRec;
    }

    public String getContenuRec() {
        return contenuRec;
    }

    public void setContenuRec(String contenuRec) {
        this.contenuRec = contenuRec;
    }

    public Date getDateRec() {
        return dateRec;
    }

    public void setDateRec(Date dateRec) {
        this.dateRec = dateRec;
    }

    public String getEmailRec() {
        return emailRec;
    }

    public void setEmailRec(String emailRec) {
        this.emailRec = emailRec;
    }

    public String getStatutRec() {
        return statutRec;
    }

    public void setStatutRec(String statutRec) {
        this.statutRec = statutRec;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", sujetRec='" + sujetRec + '\'' +
                ", contenuRec='" + contenuRec + '\'' +
                ", dateRec=" + dateRec +
                ", emailRec='" + emailRec + '\'' +
                ", statutRec='" + statutRec + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reclamation that)) return false;
        return getId() == that.getId() && Objects.equals(getSujetRec(), that.getSujetRec()) && Objects.equals(getContenuRec(), that.getContenuRec()) && Objects.equals(getDateRec(), that.getDateRec()) && Objects.equals(getEmailRec(), that.getEmailRec()) && Objects.equals(getStatutRec(), that.getStatutRec());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSujetRec(), getContenuRec(), getDateRec(), getEmailRec(), getStatutRec());
    }

}
