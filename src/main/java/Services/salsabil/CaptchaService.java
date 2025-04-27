package Services.salsabil;

import Utils.CaptchaQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaptchaService {
    private List<CaptchaQuestion> questions = new ArrayList<>();

    public CaptchaService() {
        // Ajoute des questions avec leurs réponses
        questions.add(new CaptchaQuestion("Quel est le capital de la France ?", "Paris"));
        questions.add(new CaptchaQuestion("Combien de jours dans une semaine ?", "7"));
        questions.add(new CaptchaQuestion("Quel est le mois après janvier ?", "Février"));
        questions.add(new CaptchaQuestion("Quel est l'animal qui miaule ?", "Chat"));
    }

    // Méthode pour obtenir une question aléatoire
    public CaptchaQuestion getRandomQuestion() {
        Random rand = new Random();
        return questions.get(rand.nextInt(questions.size()));
    }

    // Méthode pour vérifier la réponse
    public boolean checkAnswer(CaptchaQuestion question, String userAnswer) {
        return question.getAnswer().equalsIgnoreCase(userAnswer.trim());
    }
}

