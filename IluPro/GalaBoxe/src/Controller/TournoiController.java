// TournoiController.java
package Controller;

import Services.CSVWRITER;
import presentation.Combattant;
import presentation.Niveau;
import presentation.Sexe;
import presentation.Tournoi;
import Services.GestionTournoi;
import Services.GestionTournoiImplement;
import java.util.List;

public class TournoiController {
    private GestionTournoi gestionTournoi = new GestionTournoiImplement();

    public Tournoi creerTournoi(String categoriePoids, Sexe sexe, Niveau niveau) {
        return gestionTournoi.creerTournoi(categoriePoids, sexe, niveau);
    }

    public void sauvegarderCombattants(List<Combattant> combattants) {
        CSVWRITER.writeCombattantsToCSV("combattants.csv", combattants);
    }

    public void supprimerTournoi(Tournoi tournoi) {
        gestionTournoi.supprimerTournoi(tournoi);
    }

    public void planifierCombats(Tournoi tournoi) {
        tournoi.planifierCombats();
    }

    public void enregistrerResultats(Tournoi tournoi) {
        tournoi.enregistrerResultats();
    }
}