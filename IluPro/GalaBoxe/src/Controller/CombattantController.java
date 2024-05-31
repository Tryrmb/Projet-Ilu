// CombattantController.java
package Controller;

import presentation.Combattant;
import Services.GestionCombattant;
import Services.GestionCombattantsImplement;
import java.util.List;

public class CombattantController {
    private GestionCombattant gestionCombattants = new GestionCombattantsImplement();

    public void ajouterCombattant(Combattant combattant) {
        gestionCombattants.ajouterCombattant(combattant);
    }

    public void supprimerCombattant(Combattant combattant) {
        gestionCombattants.supprimerCombattant(combattant);
    }

    public void modifierCombattant(Combattant combattant) {
        gestionCombattants.modifierCombattant(combattant);
    }

    public List<Combattant> getCombattants() {
        return gestionCombattants.getCombattants();
    }
}
