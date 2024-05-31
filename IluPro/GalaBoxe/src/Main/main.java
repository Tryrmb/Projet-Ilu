package Main;

import Controller.CombattantController;
import Services.CSVWRITER;
import Controller.TournoiController;
import Services.GestionSportif;
import presentation.*;
import Services.DataAccesObject;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        CombattantController combattantController = new CombattantController();
        TournoiController tournoiController = new TournoiController();
        GestionSportif gestionSportif = new GestionSportif();

        // Message d'accueil
        System.out.println("*************************************");
        System.out.println("*  Bienvenue au Championnat         *");
        System.out.println("*          d'Occitanie !            *");
        System.out.println("*************************************");

        // Ajouter les combattants (code existant)
        ajouterTousLesCombattants(combattantController);

        List<Combattant> combattants = combattantController.getCombattants();
        tournoiController.sauvegarderCombattants(combattants);

        // Boucle de menu principal
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.println("-------------------------------------");
            System.out.println("Menu:");
            System.out.println("1. Créer un tournoi");
            System.out.println("2. Voir le contenu d'un combattant");
            System.out.println("3. Voir l'avancement du tournoi");
            System.out.println("4. Quitter");
            System.out.println("5. Voir les règles");
            System.out.println("6. Mettre à jour les informations personnelles");
            System.out.println("7. Ajouter une performance");
            System.out.println("8. Ajouter les détails d'un combat");
            System.out.println("9. Calculer le pourcentage de réussite");
            System.out.print("Choisissez une option: ");
            int choix = scanner.nextInt();
            scanner.nextLine();  // Consommer le saut de ligne

            switch (choix) {
                case 1:
                    creerTournoi(scanner, tournoiController, combattantController);
                    break;
                case 2:
                    voirContenuCombattant(scanner, combattantController);
                    break;
                case 3:
                    voirAvancementTournoi(scanner, tournoiController, combattantController);
                    break;
                case 4:
                    continuer = false;
                    break;
                case 5:
                    afficherRegles(scanner);
                    break;
                case 6:
                    mettreAJourInfosPersonnelles(scanner, gestionSportif, combattantController);
                    break;
                case 7:
                    ajouterPerformance(scanner);
                    break;
                case 8:
                    ajouterDetailsCombat(scanner);
                    break;
                case 9:
                    calculerPourcentageReussite(scanner);
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
    }

    private static void mettreAJourInfosPersonnelles(Scanner scanner, GestionSportif gestionSportif, CombattantController combattantController) {
        boolean retour = false;
        while (!retour) {
            System.out.println("Entrez votre nom d'utilisateur:");
            String nomUtilisateur = scanner.nextLine();
            System.out.println("Entrez votre mot de passe:");
            String motDePasse = scanner.nextLine();

            if (gestionSportif.verifierIdentite(nomUtilisateur, motDePasse)) {
                System.out.println("Entrez le nom du combattant:");
                String nomCombattant = scanner.nextLine();
                Combattant combattant = combattantController.getCombattants().stream()
                        .filter(c -> c.getNom().equalsIgnoreCase(nomCombattant))
                        .findFirst()
                        .orElse(null);

                if (combattant != null) {
                    System.out.println("Quel champ voulez-vous mettre à jour? (poids, taille, palmares, club)");
                    String champ = scanner.nextLine();
                    System.out.println("Entrez la nouvelle valeur:");
                    String nouvelleValeur = scanner.nextLine();
                    gestionSportif.mettreAJourInfo(combattant, champ, nouvelleValeur);
                    System.out.println("Informations mises à jour avec succès.");
                } else {
                    System.out.println("Combattant non trouvé.");
                }
                retour = true;
            } else {
                System.out.println("Identifiants incorrects. Veuillez réessayer.");
            }
        }
    }

    private static void creerTournoi(Scanner scanner, TournoiController tournoiController, CombattantController combattantController) {
        boolean retour = false;
        while (!retour) {
            System.out.println("Entrez la catégorie de poids:");
            String categoriePoids = scanner.nextLine().toUpperCase().replace(" ", "_");

            System.out.println("Entrez le sexe (HOMME/FEMME):");
            Sexe sexe = Sexe.valueOf(scanner.nextLine().toUpperCase());

            System.out.println("Entrez le niveau (AMATEUR/DEBUTANT/PROFESSIONNEL):");
            Niveau niveau = Niveau.valueOf(scanner.nextLine().toUpperCase());

            Tournoi tournoi = tournoiController.creerTournoi(categoriePoids, sexe, niveau);

            for (Combattant combattant : combattantController.getCombattants()) {
                // Vérification des critères avant d'ajouter le combattant au tournoi
                if (combattant.getNiveau() == niveau && combattant.getSexe() == sexe && 
                    ((sexe == Sexe.HOMME && combattant.getCategorieDePoidsHomme() != null && combattant.getCategorieDePoidsHomme().name().equals(categoriePoids)) ||
                     (sexe == Sexe.FEMME && combattant.getCategorieDePoidsFemme() != null && combattant.getCategorieDePoidsFemme().name().equals(categoriePoids)))) {
                    tournoi.ajouterCombattant(combattant);
                }
            }

            tournoi.planifierCombats();
            tournoi.afficherTableau();
            tournoiController.enregistrerResultats(tournoi);
            tournoi.afficherResultats();
            tournoi.afficherClassement();
            tournoi.attribuerRecompenses();

            retour = true;
        }
    }


    private static void voirContenuCombattant(Scanner scanner, CombattantController combattantController) {
        boolean retour = false;
        while (!retour) {
            System.out.println("Entrez le nom du combattant (ou 'retour' pour revenir):");
            String nomCombattant = scanner.nextLine();

            if (nomCombattant.equalsIgnoreCase("retour")) {
                retour = true;
            } else {
                Combattant combattant = combattantController.getCombattants().stream()
                        .filter(c -> c.getNom().equalsIgnoreCase(nomCombattant))
                        .findFirst()
                        .orElse(null);

                if (combattant != null) {
                    System.out.println(combattant.afficherCaracteristiques());
                } else {
                    System.out.println("Combattant non trouvé.");
                }
            }
        }
    }

    private static void voirAvancementTournoi(Scanner scanner, TournoiController tournoiController, CombattantController combattantController) {
        boolean retour = false;
        while (!retour) {
            System.out.println("Entrez la catégorie de poids, le sexe et le niveau du tournoi (ou 'retour' pour revenir):");
            String tournoiInfo = scanner.nextLine().toUpperCase().replace(" ", "_");

            if (tournoiInfo.equalsIgnoreCase("RETOUR")) {
                retour = true;
            } else {
                String[] parts = tournoiInfo.split("_");
                if (parts.length == 3) {
                    String categoriePoids = parts[0];
                    Sexe sexe = Sexe.valueOf(parts[1]);
                    Niveau niveau = Niveau.valueOf(parts[2]);

                    Tournoi tournoiEnCours = tournoiController.creerTournoi(categoriePoids, sexe, niveau);
                    for (Combattant c : combattantController.getCombattants()) {
                        tournoiEnCours.ajouterCombattant(c);
                    }
                    tournoiEnCours.planifierCombats();
                    tournoiEnCours.afficherTableau();
                    tournoiEnCours.afficherResultats();
                    tournoiEnCours.afficherClassement();
                } else {
                    System.out.println("Format de tournoi invalide. Veuillez réessayer.");
                }
            }
        }
    }

    private static void afficherRegles(Scanner scanner) {
        boolean retour = false;

        while (!retour) {
            System.out.println("Choisissez le niveau des règles à afficher :");
            System.out.println("1. Professionnelle");
            System.out.println("2. Amateur");
            System.out.println("3. Débutant");
            System.out.println("4. Retour");
            System.out.print("Votre choix: ");
            int choix = scanner.nextInt();
            scanner.nextLine();  // Consommer le saut de ligne

            switch (choix) {
                case 1:
                    ReglesTournoi.afficherRegles(Niveau.PROFESSIONNEL);
                    break;
                case 2:
                    ReglesTournoi.afficherRegles(Niveau.AMATEUR);
                    break;
                case 3:
                    ReglesTournoi.afficherRegles(Niveau.DEBUTANT);
                    break;
                case 4:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void ajouterPerformance(Scanner scanner) {
        System.out.println("Entrez l'ID du combattant:");
        String id = scanner.nextLine();
        System.out.println("Entrez le nom de l'adversaire:");
        String adversaire = scanner.nextLine();
        System.out.println("Entrez la date du combat (YYYY-MM-DD):");
        String date = scanner.nextLine();
        System.out.println("Entrez le résultat (victoire, défaite, nul):");
        String resultat = scanner.nextLine();
        System.out.println("Entrez le type de victoire (KO, TKO, points):");
        String typeVictoire = scanner.nextLine();
        DataAccesObject.addPerformance("performances.csv", id, adversaire, date, resultat, typeVictoire);
    }

    private static void ajouterDetailsCombat(Scanner scanner) {
        System.out.println("Entrez l'ID du combattant:");
        String id = scanner.nextLine();
        System.out.println("Entrez le nombre de coups réussis:");
        String coupsReussis = scanner.nextLine();
        System.out.println("Entrez le nombre de coups manqués:");
        String coupsManques = scanner.nextLine();
        System.out.println("Entrez les types de coups (séparés par des virgules):");
        String typesCoups = scanner.nextLine();
        System.out.println("Entrez les zones touchées (séparées par des virgules):");
        String zonesTouchees = scanner.nextLine();
        DataAccesObject.addCombatDetails("combats.csv", id, coupsReussis, coupsManques, typesCoups, zonesTouchees);
    }

    private static void calculerPourcentageReussite(Scanner scanner) {
        System.out.println("Entrez l'ID du combattant:");
        String id = scanner.nextLine();
        double percentage = DataAccesObject.calculatePercentages("combats.csv", id);
        System.out.println("Pourcentage de coups réussis: " + percentage + "%");
    }

    private static void ajouterTousLesCombattants(CombattantController combattantController) {
        ajouterCombattantsPoidsMoyensHommeAmateur(combattantController);
        ajouterCombattantsPoidsLourdsHommeAmateur(combattantController);
        ajouterCombattantsPoidsMoyensFemmeAmateur(combattantController);
        ajouterCombattantsPoidsPlumeFemmeAmateur(combattantController);
        ajouterCombattantsPoidsMiLourdsFemmeAmateur(combattantController);
        ajouterCombattantsPoidsMouchesFemmeAmateur(combattantController);
        ajouterCombattantsPoidsCoqDebutantHomme(combattantController);
        ajouterCombattantsPoidsLegersHommeDebutant(combattantController);
        ajouterCombattantsPoidsMoyensHommeDebutant(combattantController);
        ajouterCombattantsPoidsLegersFemmeDebutant(combattantController);
        ajouterCombattantsPoidsCoqFemmeDebutant(combattantController);
        ajouterCombattantsSuperLegersAmateurHomme(combattantController);
        ajouterCombattantsPoidsLourdHommePro(combattantController);
        ajouterCombattantsPoidsLegerHommePro(combattantController);
        ajouterCombattantsPoidsWeltersHommePro(combattantController);
        ajouterCombattantsPoidsMoucheFemmePro(combattantController);
        ajouterCombattantsPoidsPlumeFemmePro(combattantController);
        ajouterCombattantsPoidsMoyensFemmePro(combattantController);
    }
    
    private static void ajouterCombattantsPoidsMoyensHommeAmateur(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("John Berger", 180, 75, "10-2-0", Sexe.HOMME, Club.NOBLEARTPEPIEUXOIS, 90, 85, 80, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant2 = new Combattant("Maxime Thomas", 175, 70, "8-3-1", Sexe.HOMME, Club.BCNARBONNAIS, 85, 80, 90, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant3 = new Combattant("Brice Morvant", 185, 80, "20-0-0", Sexe.HOMME, Club.NIMESBOXE, 95, 90, 88, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant4 = new Combattant("Theo Victor", 178, 72, "15-1-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 90, 85, 85, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant5 = new Combattant("Mamadou Diallo", 182, 78, "12-2-0", Sexe.HOMME, Club.BCARIEGEOIS, 88, 84, 82, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant6 = new Combattant("Medhi Bekhal", 180, 75, "10-3-1", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 92, 88, 87, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant7 = new Combattant("Jonas Michel", 175, 70, "9-4-0", Sexe.HOMME, Club.CAHORSBOXE, 85, 82, 80, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant8 = new Combattant("Victor Mbemba", 185, 80, "14-2-0", Sexe.HOMME, Club.BCTOULOUSAIN, 91, 89, 85, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant9 = new Combattant("Jerry Allen", 178, 72, "11-3-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 90, 86, 84, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant10 = new Combattant("Arthur Gomez", 182, 78, "13-1-0", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 89, 87, 83, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant11 = new Combattant("Victor SainPetrier", 180, 75, "10-4-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 88, 85, 82, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant12 = new Combattant("Oliver Courtois", 175, 70, "9-3-1", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 87, 84, 81, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant13 = new Combattant("Ali Pasquier", 185, 80, "15-1-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 92, 89, 88, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant14 = new Combattant("Jean Bosco", 178, 72, "12-3-0", Sexe.HOMME, Club.BOXINGCLUBMURET, 89, 86, 85, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant15 = new Combattant("Thomas Guiraud", 182, 78, "14-2-0", Sexe.HOMME, Club.SAINTLYSOLYMPIQUEBOXE, 91, 87, 84, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant16 = new Combattant("Leo Jallet", 180, 75, "11-4-0", Sexe.HOMME, Club.BOXINGCENTERTOULOUSE, 90, 85, 82, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_MOYENS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }


    private static void ajouterCombattantsPoidsMouchesFemmeAmateur(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Marie Dubois", 160, 48, "5-2-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 85, 80, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante2 = new Combattant("Sophie Lambert", 162, 49, "6-1-0", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 88, 82, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante3 = new Combattant("Claire Martin", 158, 50, "7-0-1", Sexe.FEMME, Club.CAHORSBOXE, 90, 85, 80, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante4 = new Combattant("Julie Verne", 161, 51, "8-2-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 87, 83, 79, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante5 = new Combattant("Anne Dupont", 163, 52, "9-1-0", Sexe.FEMME, Club.BCNARBONNAIS, 89, 84, 81, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante6 = new Combattant("Isabelle Leroy", 159, 50, "6-3-0", Sexe.FEMME, Club.BCARIEGEOIS, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante7 = new Combattant("Emilie Petit", 160, 49, "4-2-1", Sexe.FEMME, Club.CARCASSONNEBOXING, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante8 = new Combattant("Hélène Fournier", 161, 50, "5-3-0", Sexe.FEMME, Club.BCTOULOUSAIN, 86, 81, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante9 = new Combattant("Alice Brun", 159, 51, "3-4-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 83, 78, 74, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante10 = new Combattant("Valerie Blanc", 162, 50, "6-2-1", Sexe.FEMME, Club.BOXINGCLUBMURET, 88, 82, 79, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante11 = new Combattant("Catherine Durant", 160, 48, "5-1-0", Sexe.FEMME, Club.NIMESBOXE, 87, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante12 = new Combattant("Julie Dupre", 158, 49, "7-2-0", Sexe.FEMME, Club.BOXINGCENTERTOULOUSE, 89, 84, 80, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante13 = new Combattant("Lucie Dupuis", 161, 50, "6-1-1", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 86, 81, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante14 = new Combattant("Sandrine Marchand", 163, 52, "8-0-0", Sexe.FEMME, Club.BOXINGCLUBEBAM, 90, 85, 81, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante15 = new Combattant("Elodie Laurent", 160, 48, "4-2-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante16 = new Combattant("Sophie Faure", 162, 51, "5-3-1", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 87, 82, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOUCHES);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }

    private static void ajouterCombattantsPoidsLourdsHommeAmateur(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Marc Durand", 190, 95, "15-3-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 85, 90, 88, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant2 = new Combattant("Pierre Lefevre", 192, 98, "12-2-1", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 84, 89, 87, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant3 = new Combattant("Jean Martin", 195, 100, "18-1-0", Sexe.HOMME, Club.CAHORSBOXE, 86, 88, 89, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant4 = new Combattant("Luc Dupont", 193, 97, "13-4-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 83, 87, 85, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant5 = new Combattant("Nicolas Moreau", 191, 96, "14-2-1", Sexe.HOMME, Club.BCNARBONNAIS, 85, 86, 88, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant6 = new Combattant("Henri Robert", 194, 99, "10-5-0", Sexe.HOMME, Club.BCARIEGEOIS, 82, 85, 84, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant7 = new Combattant("Antoine Petit", 190, 95, "17-1-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 87, 89, 86, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant8 = new Combattant("Louis Dubois", 192, 98, "16-2-0", Sexe.HOMME, Club.BCTOULOUSAIN, 85, 88, 87, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant9 = new Combattant("Alexandre Fournier", 195, 100, "11-3-1", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 84, 86, 85, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant10 = new Combattant("Victor Bernard", 193, 97, "13-4-0", Sexe.HOMME, Club.BOXINGCLUBMURET, 86, 87, 88, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant11 = new Combattant("Gérard Rousseau", 191, 96, "14-2-1", Sexe.HOMME, Club.NIMESBOXE, 85, 86, 84, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant12 = new Combattant("Philippe Girard", 194, 99, "12-3-0", Sexe.HOMME, Club.BOXINGCENTERTOULOUSE, 83, 85, 86, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant13 = new Combattant("Hugo Leroy", 190, 95, "16-1-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 87, 88, 89, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant14 = new Combattant("Andre Morel", 192, 98, "15-2-0", Sexe.HOMME, Club.BOXINGCLUBEBAM, 84, 87, 88, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant15 = new Combattant("Emmanuel Laurent", 195, 100, "17-3-1", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 85, 89, 87, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant16 = new Combattant("Charles Lefebvre", 193, 97, "12-4-0", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 83, 86, 85, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_LOURDS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }

    private static void ajouterCombattantsPoidsMoyensFemmeAmateur(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Emma Girard", 165, 72, "10-2-0", Sexe.FEMME, Club.NOBLEARTPEPIEUXOIS, 85, 80, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante2 = new Combattant("Olivia Durand", 168, 73, "9-3-1", Sexe.FEMME, Club.BCNARBONNAIS, 88, 82, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante3 = new Combattant("Sophia Moreau", 170, 74, "12-1-0", Sexe.FEMME, Club.NIMESBOXE, 90, 85, 80, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante4 = new Combattant("Ava Martin", 169, 75, "11-2-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 87, 83, 79, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante5 = new Combattant("Isabella Petit", 167, 72, "8-4-0", Sexe.FEMME, Club.BCARIEGEOIS, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante6 = new Combattant("Mia Fournier", 166, 73, "10-3-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 86, 81, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante7 = new Combattant("Amelia Blanc", 164, 72, "7-4-1", Sexe.FEMME, Club.CAHORSBOXE, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante8 = new Combattant("Charlotte Dupont", 168, 74, "9-3-0", Sexe.FEMME, Club.BCTOULOUSAIN, 87, 82, 79, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante9 = new Combattant("Harper Faure", 165, 75, "8-2-1", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 86, 81, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante10 = new Combattant("Evelyn Richard", 167, 73, "10-1-1", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 89, 84, 80, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante11 = new Combattant("Abigail Bernard", 169, 74, "11-2-0", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 88, 83, 79, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante12 = new Combattant("Ella Lefevre", 166, 72, "7-3-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante13 = new Combattant("Chloe Laurent", 165, 75, "9-4-0", Sexe.FEMME, Club.BCNARBONNAIS, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante14 = new Combattant("Grace Dupuis", 168, 73, "10-3-1", Sexe.FEMME, Club.BOXINGCLUBEBAM, 87, 82, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante15 = new Combattant("Zoe Marchand", 170, 74, "11-1-0", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 89, 84, 80, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante16 = new Combattant("Lily Morel", 164, 72, "6-4-0", Sexe.FEMME, Club.BOXINGCENTERTOULOUSE, 83, 78, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MOYENS);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }


    private static void ajouterCombattantsPoidsPlumeFemmeAmateur(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Louise Durand", 165, 54, "9-2-0", Sexe.FEMME, Club.NOBLEARTPEPIEUXOIS, 85, 80, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante2 = new Combattant("Chloé Richard", 163, 53, "10-1-0", Sexe.FEMME, Club.BCNARBONNAIS, 87, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante3 = new Combattant("Camille Moreau", 164, 55, "8-3-1", Sexe.FEMME, Club.NIMESBOXE, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante4 = new Combattant("Jade Lefevre", 162, 54, "7-4-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 82, 78, 74, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante5 = new Combattant("Alice Petit", 165, 54, "9-2-1", Sexe.FEMME, Club.BCARIEGEOIS, 86, 81, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante6 = new Combattant("Léa Dubois", 163, 55, "10-3-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 87, 82, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante7 = new Combattant("Emma Martin", 164, 54, "8-2-1", Sexe.FEMME, Club.CAHORSBOXE, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante8 = new Combattant("Sophie Girard", 165, 54, "7-3-0", Sexe.FEMME, Club.BCTOULOUSAIN, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante9 = new Combattant("Elise Leroy", 162, 55, "9-1-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 88, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante10 = new Combattant("Manon Blanc", 163, 54, "10-2-1", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 87, 82, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante11 = new Combattant("Juliette Dupuis", 164, 54, "8-3-0", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante12 = new Combattant("Claire Morel", 165, 55, "9-2-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 86, 81, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante13 = new Combattant("Gabrielle Durand", 163, 54, "7-4-1", Sexe.FEMME, Club.BCNARBONNAIS, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante14 = new Combattant("Mathilde Faure", 164, 55, "10-1-0", Sexe.FEMME, Club.BOXINGCLUBEBAM, 88, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante15 = new Combattant("Lola Laurent", 165, 54, "8-2-0", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante16 = new Combattant("Sarah Marchand", 162, 55, "9-3-1", Sexe.FEMME, Club.BOXINGCENTERTOULOUSE, 87, 82, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_PLUMES);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }

    private static void ajouterCombattantsPoidsMiLourdsFemmeAmateur(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Catherine Durand", 175, 79, "10-2-0", Sexe.FEMME, Club.NOBLEARTPEPIEUXOIS, 85, 80, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante2 = new Combattant("Amandine Richard", 177, 80, "11-1-0", Sexe.FEMME, Club.BCNARBONNAIS, 87, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante3 = new Combattant("Nathalie Moreau", 176, 78, "9-3-1", Sexe.FEMME, Club.NIMESBOXE, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante4 = new Combattant("Isabelle Lefevre", 174, 77, "8-4-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 82, 78, 74, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante5 = new Combattant("Sandrine Petit", 175, 79, "10-2-1", Sexe.FEMME, Club.BCARIEGEOIS, 86, 81, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante6 = new Combattant("Amélie Dubois", 177, 80, "11-3-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 87, 82, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante7 = new Combattant("Sophie Martin", 174, 78, "9-2-0", Sexe.FEMME, Club.CAHORSBOXE, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante8 = new Combattant("Delphine Girard", 176, 77, "10-1-0", Sexe.FEMME, Club.BCTOULOUSAIN, 86, 81, 77, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante9 = new Combattant("Cécile Leroy", 175, 79, "8-3-1", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante10 = new Combattant("Nadine Blanc", 177, 80, "11-2-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 87, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante11 = new Combattant("Corinne Dupuis", 174, 77, "10-3-0", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante12 = new Combattant("Sabrina Morel", 175, 79, "9-4-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante13 = new Combattant("Chantal Faure", 177, 80, "11-1-0", Sexe.FEMME, Club.BCNARBONNAIS, 88, 83, 78, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante14 = new Combattant("Lucie Laurent", 174, 77, "10-2-0", Sexe.FEMME, Club.BOXINGCLUBEBAM, 85, 80, 76, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante15 = new Combattant("Emilie Marchand", 176, 78, "9-3-0", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 84, 79, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);
        Combattant combattante16 = new Combattant("Caroline Morel", 175, 79, "8-4-0", Sexe.FEMME, Club.BOXINGCENTERTOULOUSE, 83, 78, 75, Niveau.AMATEUR, null, CategoriePoidsFemme.POIDS_MI_LOURDS);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }
    
    
    private static void ajouterCombattantsPoidsCoqDebutantHomme(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Julien Martin", 165, 53, "5-0-0", Sexe.HOMME, Club.NOBLEARTPEPIEUXOIS, 75, 70, 65, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant2 = new Combattant("Antoine Dubois", 167, 54, "4-1-0", Sexe.HOMME, Club.BCNARBONNAIS, 76, 71, 66, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant3 = new Combattant("Marc Lefevre", 168, 55, "6-0-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 78, 72, 67, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant4 = new Combattant("Luc Dupont", 164, 53, "3-2-0", Sexe.HOMME, Club.BCARIEGEOIS, 74, 69, 64, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant5 = new Combattant("Paul Moreau", 166, 54, "5-1-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 77, 73, 68, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant6 = new Combattant("Henri Lambert", 169, 56, "4-2-0", Sexe.HOMME, Club.CAHORSBOXE, 76, 71, 67, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant7 = new Combattant("Louis Girard", 167, 55, "3-3-0", Sexe.HOMME, Club.BCTOULOUSAIN, 75, 70, 65, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant8 = new Combattant("Charles Bernard", 168, 54, "6-1-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 78, 72, 67, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant9 = new Combattant("Victor Petit", 165, 53, "5-2-0", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 76, 71, 66, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant10 = new Combattant("Georges Roy", 166, 54, "4-3-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 75, 70, 65, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant11 = new Combattant("Francis Blanc", 169, 55, "6-0-0", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 78, 73, 68, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant12 = new Combattant("Rene Durand", 167, 54, "5-1-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 76, 71, 67, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant13 = new Combattant("Albert Masson", 164, 53, "4-2-0", Sexe.HOMME, Club.BCNARBONNAIS, 75, 70, 65, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant14 = new Combattant("Michel Leroy", 165, 54, "6-1-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 78, 73, 68, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant15 = new Combattant("Claude Dupuis", 168, 55, "5-2-0", Sexe.HOMME, Club.BCARIEGEOIS, 76, 71, 66, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);
        Combattant combattant16 = new Combattant("Andre Faure", 167, 54, "4-3-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 75, 70, 65, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_COQS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }
    
    
    private static void ajouterCombattantsPoidsLegersHommeDebutant(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Jean-Paul Durand", 168, 60, "3-2-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 78, 72, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant2 = new Combattant("Pierre Lambert", 170, 61, "6-1-0", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 80, 75, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant3 = new Combattant("Mathieu Perrot", 169, 60, "5-2-0", Sexe.HOMME, Club.CAHORSBOXE, 79, 74, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant4 = new Combattant("Adrien Moulin", 171, 61, "4-3-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 77, 73, 69, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant5 = new Combattant("Olivier Guerin", 172, 62, "6-1-0", Sexe.HOMME, Club.BCARIEGEOIS, 81, 76, 72, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant6 = new Combattant("Sebastien Faure", 168, 60, "5-2-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 78, 73, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant7 = new Combattant("Gregory Lefevre", 170, 61, "4-3-0", Sexe.HOMME, Club.BCTOULOUSAIN, 80, 75, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant8 = new Combattant("David Perrin", 171, 62, "6-1-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 81, 77, 72, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant9 = new Combattant("Romain Marechal", 172, 63, "5-2-0", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 82, 78, 73, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant10 = new Combattant("Jerome Simon", 168, 60, "4-3-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 78, 73, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant11 = new Combattant("Julien Blanc", 170, 61, "6-1-0", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 80, 75, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant12 = new Combattant("Damien Michel", 171, 62, "5-2-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 81, 76, 72, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant13 = new Combattant("Frederic Masson", 172, 63, "4-3-0", Sexe.HOMME, Club.BCNARBONNAIS, 82, 78, 73, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant14 = new Combattant("Nicolas Robert", 168, 60, "6-1-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 78, 73, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant15 = new Combattant("Jean-Louis Dubois", 170, 61, "5-2-0", Sexe.HOMME, Club.BCARIEGEOIS, 80, 75, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant16 = new Combattant("Laurent Henry", 171, 62, "4-3-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 81, 76, 72, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_LEGERS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }
    
    private static void ajouterCombattantsPoidsMoyensHommeDebutant(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Philippe Bernard", 175, 72, "4-3-0", Sexe.HOMME, Club.CAHORSBOXE, 77, 74, 69, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant2 = new Combattant("Lucas Moreau", 177, 73, "5-2-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 79, 76, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant3 = new Combattant("Thierry Richard", 176, 72, "3-3-0", Sexe.HOMME, Club.BCARIEGEOIS, 78, 75, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant4 = new Combattant("Christophe Lambert", 178, 74, "6-1-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 80, 77, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant5 = new Combattant("Pascal Lefevre", 175, 73, "4-2-0", Sexe.HOMME, Club.BCNARBONNAIS, 77, 74, 69, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant6 = new Combattant("Jean-Pierre Roy", 176, 72, "5-1-0", Sexe.HOMME, Club.BCTOULOUSAIN, 78, 75, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant7 = new Combattant("Didier Petit", 178, 74, "6-0-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 80, 77, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant8 = new Combattant("Gerard Faure", 177, 73, "3-3-0", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 79, 76, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant9 = new Combattant("Patrick Masson", 176, 72, "4-2-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 77, 74, 69, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant10 = new Combattant("Bernard Lambert", 175, 73, "5-1-0", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 78, 75, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant11 = new Combattant("Yves Simon", 177, 74, "6-0-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 80, 77, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant12 = new Combattant("Robert Girard", 176, 72, "3-3-0", Sexe.HOMME, Club.BCNARBONNAIS, 78, 75, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant13 = new Combattant("Roland Henry", 175, 73, "4-2-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 77, 74, 69, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant14 = new Combattant("Albert Robert", 178, 74, "5-1-0", Sexe.HOMME, Club.BCARIEGEOIS, 79, 76, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant15 = new Combattant("Maurice Perrot", 177, 73, "6-0-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 80, 77, 71, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);
        Combattant combattant16 = new Combattant("Jacques Blanc", 176, 72, "4-3-0", Sexe.HOMME, Club.BCTOULOUSAIN, 78, 75, 70, Niveau.DEBUTANT, CategoriePoidsHomme.POIDS_MOYENS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }
    
    
    private static void ajouterCombattantsPoidsLegersFemmeDebutant(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Clara Lefevre", 162, 59, "4-2-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante2 = new Combattant("Eva Laurent", 160, 58, "3-3-0", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 76, 71, 66, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante3 = new Combattant("Sophie Dubois", 163, 60, "5-1-0", Sexe.FEMME, Club.BCNARBONNAIS, 77, 72, 67, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante4 = new Combattant("Emma Bernard", 161, 59, "4-2-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante5 = new Combattant("Julie Richard", 162, 58, "3-3-0", Sexe.FEMME, Club.BCARIEGEOIS, 76, 71, 66, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante6 = new Combattant("Laura Moreau", 163, 60, "5-1-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 77, 72, 67, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante7 = new Combattant("Aline Lefevre", 160, 59, "4-2-0", Sexe.FEMME, Club.BCTOULOUSAIN, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante8 = new Combattant("Nathalie Dubois", 162, 58, "3-3-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 76, 71, 66, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante9 = new Combattant("Elodie Simon", 163, 60, "5-1-0", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 77, 72, 67, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante10 = new Combattant("Delphine Roy", 160, 59, "4-2-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante11 = new Combattant("Amandine Martin", 161, 58, "3-3-0", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 76, 71, 66, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante12 = new Combattant("Isabelle Bernard", 163, 60, "5-1-0", Sexe.FEMME, Club.BCNARBONNAIS, 77, 72, 67, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante13 = new Combattant("Audrey Dubois", 162, 59, "4-2-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante14 = new Combattant("Christelle Richard", 160, 58, "3-3-0", Sexe.FEMME, Club.BCARIEGEOIS, 76, 71, 66, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante15 = new Combattant("Valerie Moreau", 163, 60, "5-1-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 77, 72, 67, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);
        Combattant combattante16 = new Combattant("Stephanie Lefevre", 160, 59, "4-2-0", Sexe.FEMME, Club.BCTOULOUSAIN, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_LEGERS);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }

    private static void ajouterCombattantsPoidsCoqFemmeDebutant(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Marie Dupont", 160, 51, "5-1-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 73, 68, 63, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante2 = new Combattant("Nina Simon", 162, 52, "4-2-0", Sexe.FEMME, Club.BCNARBONNAIS, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante3 = new Combattant("Alice Martin", 161, 51, "3-3-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 74, 69, 64, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante4 = new Combattant("Charlotte Dubois", 160, 52, "5-1-0", Sexe.FEMME, Club.BCARIEGEOIS, 73, 68, 63, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante5 = new Combattant("Louise Richard", 162, 51, "4-2-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante6 = new Combattant("Camille Lefevre", 161, 52, "3-3-0", Sexe.FEMME, Club.BCTOULOUSAIN, 74, 69, 64, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante7 = new Combattant("Julie Moreau", 160, 51, "5-1-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 73, 68, 63, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante8 = new Combattant("Amelie Girard", 162, 52, "4-2-0", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante9 = new Combattant("Lucie Petit", 161, 51, "3-3-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 74, 69, 64, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante10 = new Combattant("Elise Dupont", 160, 52, "5-1-0", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 73, 68, 63, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante11 = new Combattant("Claire Simon", 162, 51, "4-2-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante12 = new Combattant("Sophie Martin", 161, 52, "3-3-0", Sexe.FEMME, Club.BCNARBONNAIS, 74, 69, 64, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante13 = new Combattant("Eva Bernard", 160, 51, "5-1-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 73, 68, 63, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante14 = new Combattant("Laure Dubois", 162, 52, "4-2-0", Sexe.FEMME, Club.BCARIEGEOIS, 75, 70, 65, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante15 = new Combattant("Helene Richard", 161, 51, "3-3-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 74, 69, 64, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);
        Combattant combattante16 = new Combattant("Margaux Lefevre", 160, 52, "5-1-0", Sexe.FEMME, Club.BCTOULOUSAIN, 73, 68, 63, Niveau.DEBUTANT, null, CategoriePoidsFemme.POIDS_COQS);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }

    private static void ajouterCombattantsSuperLegersAmateurHomme(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Hugo Girard", 170, 63, "12-2-0", Sexe.HOMME, Club.NOBLEARTPEPIEUXOIS, 87, 83, 78, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant2 = new Combattant("Antoine Leroy", 172, 64, "11-3-1", Sexe.HOMME, Club.BCNARBONNAIS, 85, 81, 77, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant3 = new Combattant("Thierry Dubois", 171, 63, "10-4-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 86, 82, 76, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant4 = new Combattant("Christophe Perrot", 173, 64, "9-5-1", Sexe.HOMME, Club.BCARIEGEOIS, 87, 83, 78, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant5 = new Combattant("Pascal Lefevre", 170, 63, "12-3-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 88, 84, 79, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant6 = new Combattant("Jean-Pierre Masson", 172, 64, "11-4-1", Sexe.HOMME, Club.BCTOULOUSAIN, 86, 82, 77, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant7 = new Combattant("Didier Girard", 171, 63, "10-5-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 85, 81, 76, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant8 = new Combattant("Gerard Lefevre", 173, 64, "9-6-1", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 87, 83, 78, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant9 = new Combattant("Patrick Dubois", 170, 63, "12-4-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 88, 84, 79, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant10 = new Combattant("Bernard Perrot", 172, 64, "11-5-1", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 86, 82, 77, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant11 = new Combattant("Yves Bernard", 171, 63, "10-6-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 85, 81, 76, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant12 = new Combattant("Robert Faure", 170, 63, "12-5-0", Sexe.HOMME, Club.BCNARBONNAIS, 87, 83, 78, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant13 = new Combattant("Roland Henry", 172, 64, "11-6-1", Sexe.HOMME, Club.CARCASSONNEBOXING, 86, 82, 77, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant14 = new Combattant("Albert Simon", 173, 64, "10-7-0", Sexe.HOMME, Club.BCARIEGEOIS, 85, 81, 76, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant15 = new Combattant("Maurice Moreau", 171, 63, "9-8-1", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 87, 83, 78, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);
        Combattant combattant16 = new Combattant("Jacques Blanc", 170, 63, "12-6-0", Sexe.HOMME, Club.BCTOULOUSAIN, 88, 84, 79, Niveau.AMATEUR, CategoriePoidsHomme.POIDS_SUPER_LEGERS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }


    private static void ajouterCombattantsPoidsLourdHommePro(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("André Dupont", 190, 105, "25-2-1", Sexe.HOMME, Club.NOBLEARTPEPIEUXOIS, 92, 90, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant2 = new Combattant("Luc Dubois", 192, 110, "27-3-1", Sexe.HOMME, Club.BCNARBONNAIS, 90, 88, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant3 = new Combattant("Marc Lefevre", 193, 108, "30-2-0", Sexe.HOMME, Club.CAHORSBOXE, 95, 92, 90, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant4 = new Combattant("Pierre Martin", 189, 107, "28-3-1", Sexe.HOMME, Club.CARCASSONNEBOXING, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant5 = new Combattant("Henri Dupuis", 191, 109, "26-4-0", Sexe.HOMME, Club.BCARIEGEOIS, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant6 = new Combattant("Louis Petit", 194, 111, "29-2-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 94, 91, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant7 = new Combattant("Jean Moreau", 188, 106, "27-3-1", Sexe.HOMME, Club.BCTOULOUSAIN, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant8 = new Combattant("Paul Laurent", 190, 105, "24-5-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 90, 88, 86, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant9 = new Combattant("Claude Girard", 192, 109, "26-4-1", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 93, 91, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant10 = new Combattant("Hugo Leroy", 194, 112, "29-2-1", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 95, 92, 90, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant11 = new Combattant("Emile Morel", 189, 107, "27-3-0", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 92, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant12 = new Combattant("Albert Dupuis", 191, 110, "28-3-1", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant13 = new Combattant("Victor Blanc", 193, 108, "30-2-0", Sexe.HOMME, Club.BCNARBONNAIS, 94, 91, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant14 = new Combattant("Olivier Petit", 195, 113, "29-3-1", Sexe.HOMME, Club.BCNARBONNAIS, 95, 92, 90, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant15 = new Combattant("Julien Michel", 189, 106, "28-4-0", Sexe.HOMME, Club.BCARIEGEOIS, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);
        Combattant combattant16 = new Combattant("Francois Lefevre", 191, 109, "27-3-1", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 94, 91, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LOURDS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }

    


    private static void ajouterCombattantsPoidsLegerHommePro(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Thomas Petit", 175, 61, "32-2-1", Sexe.HOMME, Club.BCARIEGEOIS, 94, 90, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant2 = new Combattant("Paul Durand", 173, 60, "31-3-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 92, 88, 86, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant3 = new Combattant("Henri Lefevre", 174, 62, "30-4-1", Sexe.HOMME, Club.BCNARBONNAIS, 93, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant4 = new Combattant("Marc Moreau", 175, 63, "29-5-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 91, 88, 86, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant5 = new Combattant("Luc Dubois", 172, 60, "28-6-1", Sexe.HOMME, Club.BCARIEGEOIS, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant6 = new Combattant("Julien Petit", 174, 61, "27-7-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 93, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant7 = new Combattant("Emile Morel", 175, 63, "26-8-1", Sexe.HOMME, Club.BCTOULOUSAIN, 91, 88, 86, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant8 = new Combattant("Victor Girard", 173, 62, "25-9-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant9 = new Combattant("Claude Leroy", 175, 61, "24-10-1", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 93, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant10 = new Combattant("Jean Michel", 174, 60, "23-11-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 91, 88, 86, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant11 = new Combattant("Pierre Laurent", 175, 63, "22-12-1", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant12 = new Combattant("Hugo Lefevre", 173, 62, "21-13-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 93, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant13 = new Combattant("Maxime Dupont", 175, 61, "20-14-1", Sexe.HOMME, Club.BCNARBONNAIS, 91, 88, 86, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant14 = new Combattant("Alexandre Durand", 174, 60, "19-15-0", Sexe.HOMME, Club.BCARIEGEOIS, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant15 = new Combattant("Julien Moreau", 175, 63, "18-16-1", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);
        Combattant combattant16 = new Combattant("Philippe Petit", 173, 62, "17-17-0", Sexe.HOMME, Club.BCTOULOUSAIN, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_LEGERS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }

    private static void ajouterCombattantsPoidsWeltersHommePro(CombattantController combattantController) {
        Combattant combattant1 = new Combattant("Victor Blanc", 180, 67, "28-2-1", Sexe.HOMME, Club.BCTOULOUSAIN, 93, 91, 89, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant2 = new Combattant("Julien Moreau", 178, 68, "30-3-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant3 = new Combattant("Alexandre Petit", 179, 69, "29-4-1", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant4 = new Combattant("Louis Martin", 177, 67, "27-5-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant5 = new Combattant("Emmanuel Dupuis", 180, 68, "26-6-1", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant6 = new Combattant("Paul Girard", 178, 69, "25-7-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant7 = new Combattant("Claude Lefevre", 179, 67, "24-8-1", Sexe.HOMME, Club.BCNARBONNAIS, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant8 = new Combattant("Pierre Durand", 177, 68, "23-9-0", Sexe.HOMME, Club.CARCASSONNEBOXING, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant9 = new Combattant("Georges Blanc", 180, 69, "22-10-1", Sexe.HOMME, Club.BCARIEGEOIS, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant10 = new Combattant("Thomas Moreau", 178, 67, "21-11-0", Sexe.HOMME, Club.BOXINGCLUBALESIEN, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant11 = new Combattant("Henri Petit", 179, 68, "20-12-1", Sexe.HOMME, Club.BCTOULOUSAIN, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant12 = new Combattant("Victor Michel", 177, 69, "19-13-0", Sexe.HOMME, Club.SPORTINGBOXECUGNAUX, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant13 = new Combattant("Jacques Girard", 180, 67, "18-14-1", Sexe.HOMME, Club.BLAGNACBOXINGCLUB, 92, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant14 = new Combattant("Julien Dupuis", 179, 68, "17-15-0", Sexe.HOMME, Club.BOXINGCLUBBAGATELLE, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant15 = new Combattant("Philippe Lefevre", 177, 69, "16-16-1", Sexe.HOMME, Club.BOXINGCLUBROQUETTOIS, 93, 90, 88, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);
        Combattant combattant16 = new Combattant("Benoit Durand", 180, 67, "15-17-0", Sexe.HOMME, Club.BOXINGCLUBAUTERIVAIN, 91, 89, 87, Niveau.PROFESSIONNEL, CategoriePoidsHomme.POIDS_WELTERS, null);

        combattantController.ajouterCombattant(combattant1);
        combattantController.ajouterCombattant(combattant2);
        combattantController.ajouterCombattant(combattant3);
        combattantController.ajouterCombattant(combattant4);
        combattantController.ajouterCombattant(combattant5);
        combattantController.ajouterCombattant(combattant6);
        combattantController.ajouterCombattant(combattant7);
        combattantController.ajouterCombattant(combattant8);
        combattantController.ajouterCombattant(combattant9);
        combattantController.ajouterCombattant(combattant10);
        combattantController.ajouterCombattant(combattant11);
        combattantController.ajouterCombattant(combattant12);
        combattantController.ajouterCombattant(combattant13);
        combattantController.ajouterCombattant(combattant14);
        combattantController.ajouterCombattant(combattant15);
        combattantController.ajouterCombattant(combattant16);
    }

    private static void ajouterCombattantsPoidsMoucheFemmePro(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Anna Martin", 155, 48, "22-1-1", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante2 = new Combattant("Emma Dubois", 154, 49, "23-2-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 92, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante3 = new Combattant("Sophie Lefevre", 156, 48, "21-3-1", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 91, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante4 = new Combattant("Marie Petit", 157, 49, "20-4-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante5 = new Combattant("Clara Laurent", 155, 48, "19-5-1", Sexe.FEMME, Club.BCNARBONNAIS, 92, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante6 = new Combattant("Eva Michel", 154, 49, "18-6-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 91, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante7 = new Combattant("Alice Girard", 156, 48, "17-7-1", Sexe.FEMME, Club.BCARIEGEOIS, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante8 = new Combattant("Julie Moreau", 157, 49, "16-8-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 92, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante9 = new Combattant("Sarah Dupont", 155, 48, "15-9-1", Sexe.FEMME, Club.BCTOULOUSAIN, 91, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante10 = new Combattant("Laura Lefevre", 154, 49, "14-10-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante11 = new Combattant("Hélène Laurent", 156, 48, "13-11-1", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 92, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante12 = new Combattant("Camille Michel", 157, 49, "12-12-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 91, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante13 = new Combattant("Charlotte Petit", 155, 48, "11-13-1", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante14 = new Combattant("Mathilde Durand", 154, 49, "10-14-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 92, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante15 = new Combattant("Isabelle Blanc", 156, 48, "9-15-1", Sexe.FEMME, Club.BCNARBONNAIS, 91, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);
        Combattant combattante16 = new Combattant("Lucie Morel", 157, 49, "8-16-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOUCHES);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }

    private static void ajouterCombattantsPoidsPlumeFemmePro(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Léa Lefevre", 160, 57, "20-2-1", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 89, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante2 = new Combattant("Isabelle Bernard", 159, 56, "21-3-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante3 = new Combattant("Clara Martin", 161, 58, "19-4-1", Sexe.FEMME, Club.BCNARBONNAIS, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante4 = new Combattant("Emma Dubois", 162, 57, "18-5-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 89, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante5 = new Combattant("Sophie Lefevre", 160, 56, "17-6-1", Sexe.FEMME, Club.BCARIEGEOIS, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante6 = new Combattant("Marie Petit", 161, 58, "16-7-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante7 = new Combattant("Julie Moreau", 159, 57, "15-8-1", Sexe.FEMME, Club.BCTOULOUSAIN, 89, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante8 = new Combattant("Alice Girard", 160, 56, "14-9-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante9 = new Combattant("Clara Laurent", 162, 58, "13-10-1", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante10 = new Combattant("Eva Michel", 161, 57, "12-11-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 89, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante11 = new Combattant("Sarah Dupont", 160, 56, "11-12-1", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante12 = new Combattant("Anna Martin", 162, 58, "10-13-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante13 = new Combattant("Laura Lefevre", 159, 57, "9-14-1", Sexe.FEMME, Club.BCNARBONNAIS, 89, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante14 = new Combattant("Camille Petit", 161, 56, "8-15-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante15 = new Combattant("Charlotte Laurent", 160, 58, "7-16-1", Sexe.FEMME, Club.BCARIEGEOIS, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);
        Combattant combattante16 = new Combattant("Mathilde Durand", 162, 57, "6-17-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 89, 87, 85, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_PLUMES);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }

    private static void ajouterCombattantsPoidsMoyensFemmePro(CombattantController combattantController) {
        Combattant combattante1 = new Combattant("Camille Moreau", 170, 73, "24-2-1", Sexe.FEMME, Club.BCNARBONNAIS, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante2 = new Combattant("Sophie Petit", 168, 74, "25-3-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 93, 90, 88, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante3 = new Combattant("Chloé Dubois", 171, 72, "23-4-1", Sexe.FEMME, Club.BCARIEGEOIS, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante4 = new Combattant("Julie Laurent", 169, 73, "22-5-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante5 = new Combattant("Eva Girard", 170, 74, "21-6-1", Sexe.FEMME, Club.BCTOULOUSAIN, 93, 90, 88, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante6 = new Combattant("Alice Petit", 168, 73, "20-7-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante7 = new Combattant("Laura Lefevre", 171, 72, "19-8-1", Sexe.FEMME, Club.BLAGNACBOXINGCLUB, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante8 = new Combattant("Lucie Martin", 169, 74, "18-9-0", Sexe.FEMME, Club.BOXINGCLUBBAGATELLE, 93, 90, 88, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante9 = new Combattant("Charlotte Michel", 170, 73, "17-10-1", Sexe.FEMME, Club.BOXINGCLUBROQUETTOIS, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante10 = new Combattant("Sarah Dupont", 168, 72, "16-11-0", Sexe.FEMME, Club.BOXINGCLUBAUTERIVAIN, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante11 = new Combattant("Hélène Laurent", 171, 74, "15-12-1", Sexe.FEMME, Club.BCNARBONNAIS, 93, 90, 88, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante12 = new Combattant("Camille Petit", 170, 73, "14-13-0", Sexe.FEMME, Club.CARCASSONNEBOXING, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante13 = new Combattant("Mathilde Girard", 168, 74, "13-14-1", Sexe.FEMME, Club.BCARIEGEOIS, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante14 = new Combattant("Clara Moreau", 171, 72, "12-15-0", Sexe.FEMME, Club.BOXINGCLUBALESIEN, 93, 90, 88, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante15 = new Combattant("Julie Dubois", 170, 73, "11-16-1", Sexe.FEMME, Club.BCTOULOUSAIN, 90, 88, 86, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);
        Combattant combattante16 = new Combattant("Alice Lefevre", 169, 74, "10-17-0", Sexe.FEMME, Club.SPORTINGBOXECUGNAUX, 91, 89, 87, Niveau.PROFESSIONNEL, null, CategoriePoidsFemme.POIDS_MOYENS);

        combattantController.ajouterCombattant(combattante1);
        combattantController.ajouterCombattant(combattante2);
        combattantController.ajouterCombattant(combattante3);
        combattantController.ajouterCombattant(combattante4);
        combattantController.ajouterCombattant(combattante5);
        combattantController.ajouterCombattant(combattante6);
        combattantController.ajouterCombattant(combattante7);
        combattantController.ajouterCombattant(combattante8);
        combattantController.ajouterCombattant(combattante9);
        combattantController.ajouterCombattant(combattante10);
        combattantController.ajouterCombattant(combattante11);
        combattantController.ajouterCombattant(combattante12);
        combattantController.ajouterCombattant(combattante13);
        combattantController.ajouterCombattant(combattante14);
        combattantController.ajouterCombattant(combattante15);
        combattantController.ajouterCombattant(combattante16);
    }
    
}
