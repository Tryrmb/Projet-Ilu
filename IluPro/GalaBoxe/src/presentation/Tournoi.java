package presentation;

import recompense.*;
import java.util.ArrayList;
import java.util.List;

public class Tournoi {
    private String categoriePoids;
    private Sexe sexe;
    private Niveau niveau;
    private List<Combattant> combattants;
    private List<Combat> combats;
    private List<Combattant> vainqueurs;
    private List<Combattant> classement;

    public Tournoi(String categoriePoids, Sexe sexe, Niveau niveau) {
        this.categoriePoids = categoriePoids;
        this.sexe = sexe;
        this.niveau = niveau;
        this.combattants = new ArrayList<>();
        this.combats = new ArrayList<>();
        this.vainqueurs = new ArrayList<>();
        this.classement = new ArrayList<>();
    }

    public void ajouterCombattant(Combattant combattant) {
        if (combattant.getNiveau() == this.niveau && combattant.getSexe() == this.sexe) {
            boolean categorieValide = false;
            if (this.sexe == Sexe.HOMME && combattant.getCategorieDePoidsHomme() != null && combattant.getCategorieDePoidsHomme().name().equals(this.categoriePoids)) {
                categorieValide = true;
            } else if (this.sexe == Sexe.FEMME && combattant.getCategorieDePoidsFemme() != null && combattant.getCategorieDePoidsFemme().name().equals(this.categoriePoids)) {
                categorieValide = true;
            }

            if (categorieValide) {
                combattants.add(combattant);
                System.out.println("Combattant ajouté: " + combattant.getNom());
            } else {
                System.out.println("Le combattant ne correspond pas aux critères de la catégorie de poids du tournoi.");
            }
        } else {
            System.out.println("Le combattant ne correspond pas aux critères de sexe ou de niveau du tournoi.");
        }
    }

    
    public void planifierCombats() {
        if (combattants.size() < 2) {
            System.out.println("Pas assez de combattants pour organiser des combats.");
            return;
        }

        List<Combattant> round = new ArrayList<>(combattants);
        while (round.size() > 1) {
            List<Combattant> nextRound = new ArrayList<>();
            for (int i = 0; i < round.size(); i += 2) {
                Combat combat = new Combat(round.get(i), round.get(i + 1));
                combats.add(combat);
                combat.simulerCombat();
                nextRound.add(combat.getVainqueur());
            }
            round = nextRound;
        }
        if (!round.isEmpty()) {
            vainqueurs.add(round.get(0));
        }

        // Ajouter les perdants des demi-finales pour le match pour la 3ème place
        if (combats.size() >= 2) {
            Combattant perdantDemiFinale1 = combats.get(combats.size() - 2).getPerdant();
            Combattant perdantDemiFinale2 = combats.get(combats.size() - 1).getPerdant();
            Combat combatTroisiemePlace = new Combat(perdantDemiFinale1, perdantDemiFinale2);
            combats.add(combatTroisiemePlace);
            combatTroisiemePlace.simulerCombat();
            classement.add(combatTroisiemePlace.getVainqueur());
        }

        // Ajouter les finalistes et le vainqueur au classement
        if (vainqueurs.size() > 0) {
            classement.add(0, vainqueurs.get(0));
            Combattant finaliste = combats.get(combats.size() - 2).getPerdant();
            classement.add(1, finaliste);
        }
    }




    public void afficherTableau() {
        System.out.println("Phase de poule :");
        for (int i = 0; i < combattants.size(); i += 2) {
            Combattant c1 = combattants.get(i);
            Combattant c2 = combattants.get(i + 1);
            System.out.println(c1.getNom() + " vs " + c2.getNom());
        }
    }

    public void afficherResultats() {
        System.out.println("Résultats du tournoi :");

        System.out.println("Phase de poule :");
        for (int i = 0; i < combats.size(); i++) {
            Combat combat = combats.get(i);
            System.out.println(combat.getCombattant1().getNom() + " vs " + combat.getCombattant2().getNom() + " -> " + combat.getVainqueur().getNom());
            if (i == 7) {
                System.out.println("\nQuart de finale :");
            } else if (i == 11) {
                System.out.println("\nDemi Finale :");
            } else if (i == 13) {
                System.out.println("\nFinale :");
            }
        }

        if (!vainqueurs.isEmpty()) {
            System.out.println("Le champion du tournoi est : " + vainqueurs.get(0).getNom());
        }
    }


    public void afficherClassement() {
        System.out.println("\nClassement :");
        if (classement.size() >= 3) {
            System.out.println("1 - " + classement.get(0).getNom());
            System.out.println("2 - " + classement.get(1).getNom());
            System.out.println("3 - " + classement.get(2).getNom());
        } else if (classement.size() == 2) {
            System.out.println("1 - " + classement.get(0).getNom());
            System.out.println("2 - " + classement.get(1).getNom());
        } else if (classement.size() == 1) {
            System.out.println("1 - " + classement.get(0).getNom());
        } else {
            System.out.println("Classement insuffisant.");
        }
    }



    public void attribuerRecompenses() {
        if (classement.size() >= 3) {
            Combattant premier = classement.get(0);
            Combattant deuxieme = classement.get(1);
            Combattant troisieme = classement.get(2);

            if (niveau == Niveau.PROFESSIONNEL) {
                premier.setRecompense(new RecompenseProfessionnelle("Ceinture du Champion d'Occitanie", 4000));
                deuxieme.setRecompense(new RecompenseProfessionnelle("Coupe du Finaliste d'Occitanie", 2000));
                troisieme.setRecompense(new RecompenseProfessionnelle("Prix du troisième place", 1000));
            } else if (niveau == Niveau.AMATEUR) {
                premier.setRecompense(new RecompenseAmateur("Médaille d'or du Championnat Amateur d'Occitanie"));
                deuxieme.setRecompense(new RecompenseAmateur("Médaille d'argent du Championnat Amateur d'Occitanie"));
                troisieme.setRecompense(new RecompenseAmateur("Médaille de bronze du Championnat Amateur d'Occitanie"));
            } else if (niveau == Niveau.DEBUTANT) {
                premier.setRecompense(new RecompenseDebutant("Champion Débutant du Championnat d'Occitanie + Gants Everlast"));
                deuxieme.setRecompense(new RecompenseDebutant("Gants Metal"));
                troisieme.setRecompense(new RecompenseDebutant("Porte-clés du Championnat d'Occitanie"));
            }
        }
    }

    // Nouvelle méthode enregistrerResultats
    public void enregistrerResultats() {
        // Implémentation de l'enregistrement des résultats
        System.out.println("Résultats du tournoi enregistrés.");
        // Ajouter d'autres opérations de sauvegarde si nécessaire
    }

    // Getters and setters for attributes if needed
    public String getCategoriePoids() {
        return categoriePoids;
    }

    public void setCategoriePoids(String categoriePoids) {
        this.categoriePoids = categoriePoids;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public List<Combattant> getCombattants() {
        return combattants;
    }

    public List<Combat> getCombats() {
        return combats;
    }

    public List<Combattant> getVainqueurs() {
        return vainqueurs;
    }

    public List<Combattant> getClassement() {
        return classement;
    }
}
