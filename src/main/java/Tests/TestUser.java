package Tests;

import Entities.User;
import Entities.Profil;
import Services.ProfilService;
import Services.UserService;
import Utils.MyDB;

public class TestUser {
    public static void main(String[] args) {
        MyDB db = MyDB.getInstance();
        UserService userService = new UserService();

        User user1 = new User("salsabil", "hafsi","salsa@gmail.com",
                "password","user", 22, null, "55190078", "Ariana","photo de profil");
        User user2 = new User("Asma","Ben Youssef","asma.benyoussef@email.com",
                "mdp","Médecin",45,"Cardiologue","20304050","Tunis","asma.jpg");

        ProfilService ps = new ProfilService();

        userService.add(user2);

        // Test : Ajout d'un profil
        Profil nouveauProfil = new Profil(user2);

        ps.add(nouveauProfil); // ==> Vérifie si ça s'affiche dans la console et dans ta BDD

        // Test : Affichage de tous les profils
        System.out.println(ps.find());



//        user1.setFirst_name("Salsoul");
//        user1.setLast_name("Hafsi");
//        user1.setUser_email("salsabila23@gmail.com");
//        user1.setPassword("password");
//        user1.setAddress("Ariana Tunisie");
//        user1.setNum_tel("55190078");
//        user1.setUser_age(20);
//
//        user1.setId(39);
//
//        // Appel de la méthode update pour mettre à jour le pack
//        userService.update(user1);
//
//        userService.delete(user1);

        System.out.println(userService.find());
        System.out.println(ps.find());
    }
}
