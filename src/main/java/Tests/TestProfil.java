package Tests;

import Entities.Profil;
import Services.ProfilService;

public class TestProfil {
    public static void main(String[] args) {
        ProfilService ps = new ProfilService();

        // Test : Ajout d'un profil
        Profil nouveauProfil = new Profil("Asma","Ben Youssef","Médecin",45,"asma.jpg","Cardiologue");

        ps.add(nouveauProfil); // ==> Vérifie si ça s'affiche dans la console et dans ta BDD

        // Test : Affichage de tous les profils
        System.out.println(ps.find());

    }
}
