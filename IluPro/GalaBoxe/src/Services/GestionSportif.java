// GestionSportif.java
package Services;

import presentation.Combattant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionSportif {
    private Map<String, String> identifiants;

    public GestionSportif() {
        identifiants = new HashMap<>();
        identifiants.put("john.berger", "password123");
        identifiants.put("maxime.thomas", "password456");
        identifiants.put("brice.morvant","password789");
        identifiants.put("theo.victor", "password101");
        identifiants.put("mamadou.diallo","password102");
        identifiants.put("medhi.bekhal","password103");
        identifiants.put("jonas.michel","password104");
        identifiants.put("victor.mbemba","password105");
        identifiants.put("jerry.allen","password106");
        identifiants.put("arthur.gomez","password107");
        identifiants.put("victor.sainpetrier","password108");
        identifiants.put("oliver.courtois","password109");
        identifiants.put("ali.pasquier","password110");
        identifiants.put("jean.bosco","password111");
        identifiants.put("thomas.guiraud","password112");
        identifiants.put("leo.jallet","password113");
    }

    public boolean verifierIdentite(String nomUtilisateur, String motDePasse) {
        return motDePasse.equals(identifiants.get(nomUtilisateur));
    }

    public void mettreAJourInfo(Combattant combattant, String champ, String nouvelleValeur) {
        combattant.mettreAJourInfo(champ, nouvelleValeur);
    }

    public void sauvergarderCombattants(List<Combattant> combattants) {
        CSVWRITER.writeCombattantsToCSV("combattants.csv", combattants);
    }
}