// Combat.java
package presentation;

import java.util.HashMap;
import java.util.Map;

public class Combat {
    private Combattant combattant1;
    private Combattant combattant2;
    private Combattant vainqueur;
    private Combattant perdant;
    private Map<String, Integer> detailsCombatCombattant1;
    private Map<String, Integer> detailsCombatCombattant2;

    public Combat(Combattant combattant1, Combattant combattant2) {
        this.combattant1 = combattant1;
        this.combattant2 = combattant2;
        this.detailsCombatCombattant1 = new HashMap<>();
        this.detailsCombatCombattant2 = new HashMap<>();
    }

    public void simulerCombat() {
        int score1 = combattant1.calculerScore();
        int score2 = combattant2.calculerScore();
        if (score1 > score2) {
            vainqueur = combattant1;
            perdant = combattant2;
        } else {
            vainqueur = combattant2;
            perdant = combattant1;
        }

        // Ajouter des détails de combat fictifs pour les deux combattants
        detailsCombatCombattant1.put("coups_reussis", 30);
        detailsCombatCombattant1.put("coups_manques", 10);
        detailsCombatCombattant1.put("coups_visage", 15);
        detailsCombatCombattant1.put("coups_corps", 10);
        detailsCombatCombattant1.put("coups_foie", 5);

        detailsCombatCombattant2.put("coups_reussis", 25);
        detailsCombatCombattant2.put("coups_manques", 15);
        detailsCombatCombattant2.put("coups_visage", 10);
        detailsCombatCombattant2.put("coups_corps", 10);
        detailsCombatCombattant2.put("coups_foie", 5);

        // Mettre à jour les statistiques des combattants
        combattant1.ajouterCombat(this, detailsCombatCombattant1);
        combattant2.ajouterCombat(this, detailsCombatCombattant2);
    }

    public Combattant getCombattant1() {
        return combattant1;
    }

    public void setCombattant1(Combattant combattant1) {
        this.combattant1 = combattant1;
    }

    public Combattant getCombattant2() {
        return combattant2;
    }

    public void setCombattant2(Combattant combattant2) {
        this.combattant2 = combattant2;
    }

    public Combattant getVainqueur() {
        return vainqueur;
    }

    public void setVainqueur(Combattant vainqueur) {
        this.vainqueur = vainqueur;
    }

    public Combattant getPerdant() {
        return perdant;
    }

    public void setPerdant(Combattant perdant) {
        this.perdant = perdant;
    }

    public Map<String, Integer> getDetailsCombatCombattant1() {
        return detailsCombatCombattant1;
    }

    public Map<String, Integer> getDetailsCombatCombattant2() {
        return detailsCombatCombattant2;
    }
}