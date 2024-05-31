package presentation;

public class Coup {
    private String typeCoup;
    private String zone;

    public Coup(String typeCoup, String zone) {
        this.typeCoup = typeCoup;
        this.zone = zone;
    }

    public String getTypeCoup() {
        return typeCoup;
    }

    public String getZone() {
        return zone;
    }
}
