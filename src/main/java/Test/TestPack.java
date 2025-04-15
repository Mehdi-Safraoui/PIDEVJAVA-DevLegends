package Test;

import Entities.Pack;
import Services.PackService;
import Utiles.MyDB;

public class TestPack {
    public static void main(String[] args) {
        MyDB db = MyDB.getInstance();

        // Création des objets Pack
        Pack p1 = new Pack("LA vie ", "Pack Vitalité", 30.0, "30 jours", "assets/images/67b0f4fd9cd93.jpg", null, false);

        // Initialisation du service PackService
        PackService packService = new PackService();

        // Décommentez ces lignes pour insérer des packs dans la base de données
        // packService.add(p1);


        // Modifier un pack existant
        // Par exemple, mettez à jour le pack avec l'id 1
        p1.setNomPack("Vitalité ");
        p1.setDescriptPack("Pack Vitalité avec plus de services");
        p1.setPrixPack(35.0);
        p1.setDureePack("30");
        p1.setPhotoPack("assets/images/67b0f4fd9cd93_update.jpg");
        p1.setDiscountCode("NEWCODE123");
        p1.setUsed(true);

        p1.setId(1);

        // Appel de la méthode update pour mettre à jour le pack
        packService.update(p1);

        // Décommentez cette ligne pour supprimer un pack
        // packService.delete(p1);

        // Affichage des packs depuis la base
        System.out.println(packService.find());
    }
}
