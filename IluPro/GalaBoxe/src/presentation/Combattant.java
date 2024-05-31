package presentation;

import recompense.Recompense;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Combattant {
    private String nom;
    private int taille;
    private int poids;
    private String palmares;
    private Sexe sexe;
    private Club club;
    private int vitesse;
    private int puissance;
    private int endurance;
    private Niveau niveau;
    private int victoires;
    private int defaites;
    private int nuls;
    private double ratioVictoires;
    private double ratioDefaites;
    private double ratioNuls;
    private CategoriePoidsHomme categorieDePoidsHomme;
    private CategoriePoidsFemme categorieDePoidsFemme;
    private List<Combat> historiqueCombats;
    private List<Map<String, Integer>> detailsCombats;
    private Recompense recompense;

    public Combattant(String nom, int taille, int poids, String palmares, Sexe sexe, Club club, int vitesse, int puissance, int endurance, Niveau niveau, CategoriePoidsHomme categorieDePoidsHomme, CategoriePoidsFemme categorieDePoidsFemme) {
        this.nom = nom;
        this.taille = taille;
        this.poids = poids;
        this.palmares = palmares;
        this.sexe = sexe;
        this.club = club;
        this.vitesse = vitesse;
        this.puissance = puissance;
        this.endurance = endurance;
        this.niveau = niveau;
        this.victoires = 0;
        this.defaites = 0;
        this.nuls = 0;
        this.ratioVictoires = 0.0;
        this.ratioDefaites = 0.0;
        this.ratioNuls = 0.0;
        this.categorieDePoidsHomme = categorieDePoidsHomme;
        this.categorieDePoidsFemme = categorieDePoidsFemme;
        this.historiqueCombats = new ArrayList<>();
        this.detailsCombats = new ArrayList<>();
        this.recompense = null;
    }

    public int calculerScore() {
        return (vitesse + puissance + endurance) / 3;
    }

    public void ajouterCombat(Combat combat, Map<String, Integer> detailsCombat) {
        this.historiqueCombats.add(combat);
        this.detailsCombats.add(detailsCombat);

        if (combat.getVainqueur().equals(this)) {
            this.victoires++;
        } else if (combat.getPerdant().equals(this)) {
            this.defaites++;
        } else {
            this.nuls++;
        }

        this.ratioVictoires = (double) this.victoires / this.historiqueCombats.size();
        this.ratioDefaites = (double) this.defaites / this.historiqueCombats.size();
        this.ratioNuls = (double) this.nuls / this.historiqueCombats.size();
    }

    public void mettreAJourInfo(String champ, String nouvelleValeur) {
        switch (champ.toLowerCase()) {
            case "poids":
                this.poids = Integer.parseInt(nouvelleValeur);
                break;
            case "taille":
                this.taille = Integer.parseInt(nouvelleValeur);
                break;
            case "palmares":
                this.palmares = nouvelleValeur;
                break;
            case "club":
                this.club = Club.valueOf(nouvelleValeur.toUpperCase());
                break;
            default:
                throw new IllegalArgumentException("Champ non reconnu: " + champ);
        }
    }

    public String getNom() {
        return nom;
    }

    public int getTaille() {
        return taille;
    }

    public int getPoids() {
        return poids;
    }

    public String getPalmares() {
        return palmares;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Club getClub() {
        return club;
    }

    public int getVitesse() {
        return vitesse;
    }

    public int getPuissance() {
        return puissance;
    }

    public int getEndurance() {
        return endurance;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public CategoriePoidsHomme getCategorieDePoidsHomme() {
        return categorieDePoidsHomme;
    }

    public CategoriePoidsFemme getCategorieDePoidsFemme() {
        return categorieDePoidsFemme;
    }

    public List<Combat> getHistoriqueCombats() {
        return historiqueCombats;
    }

    public List<Map<String, Integer>> getDetailsCombats() {
        return detailsCombats;
    }

    public int getVictoires() {
        return victoires;
    }

    public int getDefaites() {
        return defaites;
    }

    public int getNuls() {
        return nuls;
    }

    public double getRatioVictoires() {
        return ratioVictoires;
    }

    public double getRatioDefaites() {
        return ratioDefaites;
    }

    public double getRatioNuls() {
        return ratioNuls;
    }

    public void setRatioVictoires(double ratioVictoires) {
        this.ratioVictoires = ratioVictoires;
    }

    public void setRatioDefaites(double ratioDefaites) {
        this.ratioDefaites = ratioDefaites;
    }

    public void setRatioNuls(double ratioNuls) {
        this.ratioNuls = ratioNuls;
    }

    public Recompense getRecompense() {
        return recompense;
    }

    public void setRecompense(Recompense recompense) {
        this.recompense = recompense;
    }

    @Override
    public String toString() {
        return "Combattant{" +
                "nom='" + nom + '\'' +
                ", taille=" + taille +
                ", poids=" + poids +
                ", palmares='" + palmares + '\'' +
                ", sexe=" + sexe +
                ", club=" + club +
                ", vitesse=" + vitesse +
                ", puissance=" + puissance +
                ", endurance=" + endurance +
                ", niveau=" + niveau +
                ", victoires=" + victoires +
                ", defaites=" + defaites +
                ", nuls=" + nuls +
                ", ratioVictoires=" + ratioVictoires +
                ", ratioDefaites=" + ratioDefaites +
                ", ratioNuls=" + ratioNuls +
                ", categorieDePoidsHomme=" + categorieDePoidsHomme +
                ", categorieDePoidsFemme=" + categorieDePoidsFemme +
                ", recompense=" + recompense +
                '}';
    }

    public String afficherCaracteristiques() {
        return String.format("%s : %dm, %d kilos, %s, %s, %s, %s, %s",
                this.nom,
                this.taille,
                this.poids,
                this.palmares,
                this.sexe,
                this.club,
                this.niveau,
                this.categorieDePoidsHomme != null ? this.categorieDePoidsHomme : this.categorieDePoidsFemme);
    }
}
