package test;

import presentation.Combattant;
import presentation.Sexe;
import presentation.Club;
import presentation.Niveau;
import Controller.CombattantController;
import Services.GestionSportif;

public class TestGestionSportif {
    public static void main(String[] args) {
        CombattantController combattantController = new CombattantController();
        GestionSportif gestionSportif = new GestionSportif();

        testVerifierIdentite(gestionSportif);
        testMettreAJourInfo(combattantController, gestionSportif);
    }

    public static void testVerifierIdentite(GestionSportif gestionSportif) {
        boolean result = gestionSportif.verifierIdentite("john.berger", "password123");
        if (result) {
            System.out.println("testVerifierIdentite passed");
        } else {
            System.out.println("testVerifierIdentite failed");
        }
    }

    public static void testMettreAJourInfo(CombattantController combattantController, GestionSportif gestionSportif) {
        Combattant combattant = new Combattant("John Berger", 180, 75, "10-2-0", Sexe.HOMME, Club.NOBLEARTPEPIEUXOIS, 90, 85, 80, Niveau.PROFESSIONNEL, null, null);
        combattantController.ajouterCombattant(combattant);
        
        gestionSportif.mettreAJourInfo(combattant, "poids", "80");
        Combattant updatedCombattant = combattantController.getCombattants().get(0);
        if (updatedCombattant.getPoids() == 80) {
            System.out.println("testMettreAJourInfo passed");
        } else {
            System.out.println("testMettreAJourInfo failed");
        }
    }
}
