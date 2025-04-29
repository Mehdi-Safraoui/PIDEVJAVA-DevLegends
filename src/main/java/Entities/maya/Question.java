package Entities.maya;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private Integer id;

    private String questionText;

    private String answerType;

    private Integer points;

//    private Quiz quiz;

    private List<Reponse> reponses; // Liste de réponses associées à la question


    // Constructors
    public Question() {
    }

    public Question(int id) {
        this.id = id;
    }

    public Question(Integer id, String questionText, String answerType, Integer points) {
        this.id = id;
        this.questionText = questionText;
        this.answerType = answerType;
        this.points = points;
    }

    public Question(String questionText, String answerType, Integer points) {
        this.questionText = questionText;
        this.answerType = answerType;
        this.points = points;
//        this.quiz = quiz;
    }

    public Question(int id, String questionText) {
        this.id = id;
        this.questionText = questionText;
        this.reponses = new ArrayList<>();
    }

    // Constructeur avec texte de question
    public Question(String questionText) {
        this.questionText = questionText;
    }

    public void addReponse(Reponse reponse) {
        this.reponses.add(reponse);
    }

    // Getters & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

//    public Quiz getQuiz() {
//        return quiz;
//    }
//
//    public void setQuiz(Quiz quiz) {
//        this.quiz = quiz;
//    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", answerType='" + answerType + '\'' +
                ", points=" + points +
                '}';
    }
}

