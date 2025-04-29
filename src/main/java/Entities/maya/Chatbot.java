package Entities.maya;

import java.util.Arrays;
import java.util.List;

public class Chatbot {

//    // Méthode qui prend le message de l'utilisateur et retourne une réponse
//    public String getResponse(String message) {
//        message = message.toLowerCase(); // Transforme le message en minuscule pour une comparaison facile
//
//        // Réponses basées sur des mots-clés
//        if (message.contains("stress") || message.contains("anxiété")) {
//            return "Voici un conseil pour le stress : essaye des exercices de respiration ou écoute de la musique apaisante.";
//        } else if (message.contains("film") || message.contains("réconfort")) {
//            return "Voici un film réconfortant que tu pourrais apprécier : 'The Pursuit of Happyness'.";
//        } else if (message.contains("méditation")) {
//            return "Voici une playlist de méditation sur YouTube : [lien].";
//        } else if (message.contains("yoga")) {
//            return "Je te recommande de faire ces exercices de yoga simples : la posture de l'enfant et le chien tête en bas.";
//        } else {
//            return "Désolé, je n'ai pas compris. Peux-tu reformuler ?";
//        }
//    }
    // Méthode pour fournir une réponse appropriée en fonction du message de l'utilisateur
    public String getResponse(String message) {
        message = message.toLowerCase(); // Transformer en minuscule pour rendre la comparaison insensible à la casse

        // 1. Réponses basées sur des mots-clés liés à la santé mentale
        if (containsKeywords(message, "stress", "anxiété", "inquiet", "nerveux")) {
            return "Le stress et l'anxiété peuvent être difficiles à gérer. Voici quelques conseils pour soulager ces sentiments : \n" +
                    "- Pratique la respiration profonde. Inhale profondément par le nez pendant 4 secondes, tiens pendant 4 secondes, puis expire lentement pendant 6 secondes.\n" +
                    "- Essaie de pratiquer la pleine conscience ou la méditation guidée.\n" +
                    "- Parler à un proche ou consulter un thérapeute peut aussi être une excellente solution.";
        }
        else if (containsKeywords(message, "dépression", "triste", "abattu", "solitaire")) {
            return "Je ressens que tu traverses des moments difficiles. La dépression peut être très pesante, mais sache que tu n'es pas seul. Voici quelques conseils :\n" +
                    "- Prends soin de toi en faisant des activités que tu apprécies, même si cela te semble difficile.\n" +
                    "- Essaie de sortir, même une courte promenade, pour prendre un peu de soleil.\n" +
                    "- Consulter un professionnel peut vraiment aider à traverser ces périodes sombres.";
        }

        // 2. Conseils de relaxation (méditation, exercices de respiration, etc.)
        else if (containsKeywords(message, "méditation", "relaxation", "calme", "respiration")) {
            return "La méditation est un excellent moyen de réduire le stress et de se recentrer. Voici un exercice simple :\n" +
                    "- Assieds-toi confortablement et ferme les yeux.\n" +
                    "- Respire profondément et concentre-toi sur ta respiration. Si ton esprit s'égare, ramène doucement ton attention à ta respiration.\n" +
                    "- Tu peux aussi essayer des applications comme Headspace ou Calm pour des méditations guidées.";
        }

        // 3. Suggestions de films réconfortants
        else if (containsKeywords(message, "film", "réconfort", "détente", "rire")) {
            return "Voici quelques films réconfortants qui pourraient te plaire :\n" +
                    "- *Le Cercle des Poètes Disparus* – Une histoire inspirante sur la vie et la passion.\n" +
                    "- *Le Fabuleux Destin d'Amélie Poulain* – Un film léger et joyeux, parfait pour remonter le moral.\n" +
                    "- *Intouchables* – Un film émouvant et drôle sur l'amitié et le soutien.";
        }

        // 4. Suggestions de musique de méditation
        else if (containsKeywords(message, "musique", "méditation", "calme", "détente", "zen")) {
            return "Voici une playlist de méditation apaisante que tu pourrais apprécier : [lien vers YouTube ou Spotify].\n" +
                    "- Tu peux aussi rechercher des playlists comme 'Calm Meditation Music' ou 'Relaxing Piano Music' pour trouver des morceaux adaptés.";
        }

        // 5. Suggestions d'exercices de yoga
        else if (containsKeywords(message, "yoga", "étirements", "exercice", "souplesse", "postures")) {
            return "Le yoga est un excellent moyen de se détendre et de soulager la tension corporelle. Voici quelques postures simples :\n" +
                    "- La *Posture de l'enfant* (Balasana) : Assieds-toi sur tes genoux, puis penche-toi en avant, bras étendus, pour étirer ton dos.\n" +
                    "- Le *chien tête en bas* (Adho Mukha Svanasana) : Une posture classique pour étirer toute la chaîne postérieure de ton corps.\n" +
                    "- Le *guerrier* (Virabhadrasana) : Idéale pour renforcer les jambes et ouvrir la poitrine.";
        }

        // Réponse par défaut si aucune correspondance n'est trouvée
        return "Désolé, je n'ai pas compris ta demande. Peux-tu reformuler ? Je suis là pour t'aider.";
    }

    // Méthode utilitaire pour vérifier si le message contient certains mots-clés
    private boolean containsKeywords(String message, String... keywords) {
        List<String> keywordList = Arrays.asList(keywords);
        for (String keyword : keywordList) {
            if (message.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
