package recompense;

public class RecompenseProfessionnelle extends Recompense {
    private int montant;

    public RecompenseProfessionnelle(String description, int montant) {
        super(description);
        this.montant = montant;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }
}
