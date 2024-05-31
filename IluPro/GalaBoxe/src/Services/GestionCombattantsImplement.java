// GestionCombattantsImplement.java
package Services;

import presentation.Combattant;
import java.util.ArrayList;
import java.util.List;

public class GestionCombattantsImplement implements GestionCombattant {
    private List<Combattant> combattants = new ArrayList<>();

    @Override
    public void ajouterCombattant(Combattant combattant) {
        combattants.add(combattant);
    }

    @Override
    public void supprimerCombattant(Combattant combattant) {
        combattants.remove(combattant);
    }

    @Override
    public void modifierCombattant(Combattant combattant) {
        for (int i = 0; i < combattants.size(); i++) {
            if (combattants.get(i).getNom().equals(combattant.getNom())) {
                combattants.set(i, combattant);
                return;
            }
        }
    }

    @Override
    public List<Combattant> getCombattants() {
        return combattants;
    }
}
