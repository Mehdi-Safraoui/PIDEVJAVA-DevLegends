package Entities;

public class Reponse {
    private int id;
    private String answerText;
    private int questionId;
    private int score;
    private String questionText;

    //Constructor

    public Reponse() {
    }

    public Reponse(int id) {
        this.id = id;
    }

    public Reponse(int id, String answerText, int questionId, int score) {
        this.id = id;
        this.answerText = answerText;
        this.questionId = questionId;
        this.score = score;
    }

    public Reponse(String answerText, int questionId, int score, String questionText) {
        this.answerText = answerText;
        this.questionId = questionId;
        this.score = score;
        this.questionText = questionText;
    }

    public Reponse(String answerText, int questionId, int score) {
        this.answerText = answerText;
        this.questionId = questionId;
        this.score = score;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                ", questionId=" + questionId +
                ", score=" + score +
                '}';
    }
}
