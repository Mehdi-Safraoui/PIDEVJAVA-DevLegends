package Test;

import Entities.Contrat;
import Services.ContratService;
import Utiles.MyDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestContrat {
    public static void main(String[] args) throws ParseException {
        MyDB db = MyDB.getInstance();

        // Création des objets Contrat
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Contrat c1 = new Contrat(1, sdf.parse("2025-02-15"), sdf.parse("2025-02-15"), "cheque", false, null);
        Contrat c2 = new Contrat(1, sdf.parse("2025-02-16"), sdf.parse("2025-02-16"), "cheque", false, null);
        Contrat c3 = new Contrat(1, sdf.parse("2025-02-16"), sdf.parse("2025-02-16"), "cheque", false, null);

        ContratService contratService = new ContratService();

        // Décommentez ces lignes pour insérer des contrats dans la base de données
        // contratService.add(c1);
        // contratService.add(c2);
        // contratService.add(c3);

        // Modifier un contrat existant
        // Par exemple, mettez à jour le contrat avec l'id 1
        c1.setModpaimentCont("carte");
        c1.setRenouvAutoCont(true);
        c1.setId(14);

        // Appel de la méthode update pour mettre à jour le contrat
        //contratService.update(c1);

        // Décommentez cette ligne pour supprimer un contrat
         contratService.delete(c1);

        // Affichage des contrats depuis la base
        System.out.println(contratService.find());
    }
}
