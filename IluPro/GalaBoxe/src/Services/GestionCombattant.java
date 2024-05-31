// GestionCombattant.java
package Services;

import presentation.Combattant;
import java.util.List;

public interface GestionCombattant {
    void ajouterCombattant(Combattant combattant);
    void supprimerCombattant(Combattant combattant);
    void modifierCombattant(Combattant combattant);
    List<Combattant> getCombattants();
}
