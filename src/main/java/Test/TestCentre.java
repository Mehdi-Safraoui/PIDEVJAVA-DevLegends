package Test;

import Entities.Centre;
import Services.CentreService;
import Utiles.MyDB;

public class TestCentre {
    public static void main(String[] args) {
        MyDB db = MyDB.getInstance();

        Centre c1 = new Centre("Centre Santé Plus", "Tunis", "22112233", "santeplus@centre.tn", "Généraliste", 50, "photo1.jpg");
        Centre c2 = new Centre("Centre Vision", "Sfax", "33445566", "vision@centre.tn", "Ophtalmologie", 30, "photo2.jpg");
        Centre c3 = new Centre("Centre Dentaire El Amen", "Ariana", "55443322", "elamen@centre.tn", "Dentiste", 25, "photo3.jpg");

        CentreService centreService = new CentreService();

        // Décommente ces lignes pour insérer dans la base
        //centreService.add(c1);
        //centreService.add(c2);
        //centreService.add(c3);

        // Modifier un centre existant
        // Par exemple, mettez à jour le centre avec l'id 1
        c1.setNomCentre("Centre Santé malek");
        c1.setAdresseCentre("Nouvelle adresse à Tunis");
        c1.setTelCentre("22334455");
        c1.setEmailCentre("nouvelemail@centre.tn");
        c1.setSpecialiteCentre("Généraliste - Mise à jour");
        c1.setCapaciteCentre(100);
        c1.setPhotoCentre("photo_update.jpg");

        c1.setId(3);

        // Appel de la méthode update pour mettre à jour le centre
         centreService.update(c1);


        //centreService.delete(c1);

        // Affichage des centres depuis la base
        System.out.println(centreService.find());
    }
}
