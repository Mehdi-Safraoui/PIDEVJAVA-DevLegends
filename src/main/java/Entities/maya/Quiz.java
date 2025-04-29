package Entities.maya;

import java.util.ArrayList;
import java.util.List;

public class Quiz {


    private Integer id;

    private Integer score;

    private String etatMental;

    public Quiz() {}

    public Quiz(Integer score, String etatMental) {
        this.score = score;
        this.etatMental = etatMental;
    }

    public Quiz(Integer id, Integer score, String etatMental) {
        this.id = id;
        this.score = score;
        this.etatMental = etatMental;
    }


    public Integer getId() {
        return id;
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getEtatMental() {
        return etatMental;
    }

    public void setEtatMental(String etatMental) {
        this.etatMental = etatMental;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", score=" + score +
                ", etatMental='" + etatMental + '\'' +
                '}';
    }

    private List<Reponse> reponses = new ArrayList<>();
    public List<Reponse> getReponses() { return reponses; }
    public void setReponses(List<Reponse> r) { this.reponses = r; }

}
