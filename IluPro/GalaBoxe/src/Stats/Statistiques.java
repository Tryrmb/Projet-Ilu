package Stats;

import presentation.Combattant;

public class Statistiques {
    public static void calculerStatistiques(Combattant combattant) {
        int totalCombats = combattant.getVictoires() + combattant.getDefaites() + combattant.getNuls();
        double ratioVictoires = totalCombats > 0 ? (double) combattant.getVictoires() / totalCombats * 100 : 0;
        double ratioDefaites = totalCombats > 0 ? (double) combattant.getDefaites() / totalCombats * 100 : 0;
        double ratioNuls = totalCombats > 0 ? (double) combattant.getNuls() / totalCombats * 100 : 0;

        combattant.setRatioVictoires(ratioVictoires);
        combattant.setRatioDefaites(ratioDefaites);
        combattant.setRatioNuls(ratioNuls);
    }

    public static void afficherStatistiques(Combattant combattant) {
        System.out.println("Statistiques du combattant " + combattant.getNom() + " :");
        System.out.println("  Taille : " + combattant.getTaille() + " cm");
        System.out.println("  Poids : " + combattant.getPoids() + " kg");
        System.out.println("  Palmares : " + combattant.getPalmares());
        System.out.println("  Victoires : " + combattant.getVictoires());
        System.out.println("  Défaites : " + combattant.getDefaites());
        System.out.println("  Nuls : " + combattant.getNuls());
        System.out.println("  Ratio Victoires : " + String.format("%.2f", combattant.getRatioVictoires()) + " %");
        System.out.println("  Ratio Défaites : " + String.format("%.2f", combattant.getRatioDefaites()) + " %");
        System.out.println("  Ratio Nuls : " + String.format("%.2f", combattant.getRatioNuls()) + " %");
    }
}
