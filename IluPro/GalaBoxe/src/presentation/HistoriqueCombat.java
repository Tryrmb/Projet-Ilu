package presentation;

import java.util.Date;

public class HistoriqueCombat {
    private Combattant adversaire;
    private Date date;
    private String resultat; // "victoire", "défaite", "nul"
    private String typeVictoire; // "KO", "TKO", "points"

    public HistoriqueCombat(Combattant adversaire, Date date, String resultat, String typeVictoire) {
        this.adversaire = adversaire;
        this.date = date;
        this.resultat = resultat;
        this.typeVictoire = typeVictoire;
    }

    public Combattant getAdversaire() {
        return adversaire;
    }

    public void setAdversaire(Combattant adversaire) {
        this.adversaire = adversaire;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getTypeVictoire() {
        return typeVictoire;
    }

    public void setTypeVictoire(String typeVictoire) {
        this.typeVictoire = typeVictoire;
    }
}

