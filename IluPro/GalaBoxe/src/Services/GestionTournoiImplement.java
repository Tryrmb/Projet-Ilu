// GestionTournoiImplement.java
package Services;

import presentation.Niveau;
import presentation.Sexe;
import presentation.Tournoi;
import java.util.ArrayList;
import java.util.List;

public class GestionTournoiImplement implements GestionTournoi {
    private List<Tournoi> tournois;

    public GestionTournoiImplement() {
        this.tournois = new ArrayList<>();
    }

    @Override
    public Tournoi creerTournoi(String categoriePoids, Sexe sexe, Niveau niveau) {
        Tournoi tournoi = new Tournoi(categoriePoids, sexe, niveau);
        tournois.add(tournoi);
        System.out.println("Tournoi créé: " + categoriePoids + " " + sexe + " " + niveau);
        return tournoi;
    }

    @Override
    public void supprimerTournoi(Tournoi tournoi) {
        if (tournois.remove(tournoi)) {
            System.out.println("Tournoi supprimé: " + tournoi.getCategoriePoids() + " " + tournoi.getSexe() + " " + tournoi.getNiveau());
        } else {
            System.out.println("Le tournoi n'existe pas.");
        }
    }

    public List<Tournoi> getTournois() {
        return tournois;
    }

    public Tournoi trouverTournoi(String categoriePoids, Sexe sexe, Niveau niveau) {
        for (Tournoi tournoi : tournois) {
            if (tournoi.getCategoriePoids().equalsIgnoreCase(categoriePoids) &&
                tournoi.getSexe() == sexe &&
                tournoi.getNiveau() == niveau) {
                return tournoi;
            }
        }
        return null;
    }
}