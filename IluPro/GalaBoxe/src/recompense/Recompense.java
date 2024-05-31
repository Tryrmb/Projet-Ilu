package recompense;

public abstract class Recompense {
    private String description;

    public Recompense(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
