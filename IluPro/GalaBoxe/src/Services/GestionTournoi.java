// GestionTournoi.java
package Services;

import presentation.Niveau;
import presentation.Sexe;
import presentation.Tournoi;

public interface GestionTournoi {
    Tournoi creerTournoi(String categoriePoids, Sexe sexe, Niveau niveau);
    void supprimerTournoi(Tournoi tournoi);
}
