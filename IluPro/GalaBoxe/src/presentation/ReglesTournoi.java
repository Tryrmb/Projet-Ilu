package presentation;

public class ReglesTournoi {
    public static void afficherRegles(Niveau niveau) {
        switch (niveau) {
            case PROFESSIONNEL:
                System.out.println("Règles pour les Professionnels:");
                System.out.println("- Un combat de 12 rounds de 3 min");
                System.out.println("- Les KO sont autorisés");
                System.out.println("- Pas le droit de baisser la tête sous la ceinture.");
                System.out.println("- Clinch autorisés (accrochage)");
                System.out.println("- En boxe anglaise les coups interdits sont ceux portés :");
                System.out.println("    • Dans le dos");
                System.out.println("    • Dans la nuque et dans le cou");
                System.out.println("    • Dans les reins");
                System.out.println("    • Sous la ceinture");
                System.out.println("- Vous n’avez pas non plus le droit de :");
                System.out.println("    • Frapper avec l’intérieur de votre gant, votre poignet, le revers ou le côté de votre main.");
                System.out.println("    • Frapper votre adversaire s’il.elle est au sol sur le ring ou s’il.elle a un genou à terre.");
                System.out.println("    • Mordre ou cracher sur votre opposant.e.");
                System.out.println("- Coups autorisés :");
                System.out.println("    • Le cross (direct du bras arrière)");
                System.out.println("    • Les uppercuts");
                System.out.println("    • Les crochets/hooks");
                System.out.println("    • Le jab (direct du bras avant)");
                System.out.println("- Pour la notation des coups :");
                System.out.println("    • Uppercut / crochet visage +2 points");
                System.out.println("    • Crochet foie +2 points");
                System.out.println("    • Uppercut plexus +2 points");
                System.out.println("    • Autres coups dans les autres parties du corps 1 point");
                break;
            case AMATEUR:
                System.out.println("Règles pour les Amateurs:");
                System.out.println("- Un combat de 3 rounds de 3 min");
                System.out.println("- Les KO sont autorisés");
                System.out.println("- Pas le droit de baisser la tête sous la ceinture.");
                System.out.println("- Clinch autorisés (accrochage)");
                System.out.println("- En boxe anglaise les coups interdits sont ceux portés :");
                System.out.println("    • Dans le dos");
                System.out.println("    • Dans la nuque et dans le cou");
                System.out.println("    • Dans les reins");
                System.out.println("    • Sous la ceinture");
                System.out.println("- Vous n’avez pas non plus le droit de :");
                System.out.println("    • Frapper avec l’intérieur de votre gant, votre poignet, le revers ou le côté de votre main.");
                System.out.println("    • Frapper votre adversaire s’il.elle est au sol sur le ring ou s’il.elle a un genou à terre.");
                System.out.println("    • Mordre ou cracher sur votre opposant.e.");
                System.out.println("- Coups autorisés :");
                System.out.println("    • Le cross (direct du bras arrière)");
                System.out.println("    • Les uppercuts");
                System.out.println("    • Les crochets/hooks");
                System.out.println("    • Le jab (direct du bras avant)");
                System.out.println("- Pour la notation des coups :");
                System.out.println("    • Uppercut / crochet visage +2 points");
                System.out.println("    • Crochet foie +2 points");
                System.out.println("    • Uppercut plexus +2 points");
                System.out.println("    • Autres coups dans les autres parties du corps 1 point");
                break;
            case DEBUTANT:
                System.out.println("Règles pour les Débutants:");
                System.out.println("- Un combat de 3 rounds de 2 min");
                System.out.println("- Les KO sont interdits");
                System.out.println("- Pas le droit de baisser la tête sous la ceinture.");
                System.out.println("- Clinch autorisés (accrochage)");
                System.out.println("- En boxe anglaise les coups interdits sont ceux portés :");
                System.out.println("    • Dans le dos");
                System.out.println("    • Dans la nuque et dans le cou");
                System.out.println("    • Dans les reins");
                System.out.println("    • Sous la ceinture");
                System.out.println("- Vous n’avez pas non plus le droit de :");
                System.out.println("    • Frapper avec l’intérieur de votre gant, votre poignet, le revers ou le côté de votre main.");
                System.out.println("    • Frapper votre adversaire s’il.elle est au sol sur le ring ou s’il.elle a un genou à terre.");
                System.out.println("    • Mordre ou cracher sur votre opposant.e.");
                System.out.println("- Coups autorisés :");
                System.out.println("    • Le cross (direct du bras arrière)");
                System.out.println("    • Les uppercuts");
                System.out.println("    • Les crochets/hooks");
                System.out.println("    • Le jab (direct du bras avant)");
                System.out.println("- Pour la notation des coups :");
                System.out.println("    • Uppercut / crochet visage +2 points");
                System.out.println("    • Crochet foie +2 points");
                System.out.println("    • Uppercut plexus +2 points");
                System.out.println("    • Autres coups dans les autres parties du corps 1 point");
                break;
            default:
                System.out.println("Niveau de règles inconnu.");
        }
    }
}
