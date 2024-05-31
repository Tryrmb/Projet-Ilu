package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import presentation.Combattant;

public class CSVWRITER {

    public static void writeCombattantsToCSV(String fileName, List<Combattant> combattants) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Combattant combattant : combattants) {
                writer.append(combattant.getNom()).append(",");
                writer.append(String.valueOf(combattant.getTaille())).append(",");
                writer.append(String.valueOf(combattant.getPoids())).append(",");
                writer.append(combattant.getPalmares()).append(",");
                writer.append(combattant.getSexe().name()).append(",");
                writer.append(combattant.getClub().name()).append(",");
                writer.append(String.valueOf(combattant.getVitesse())).append(",");
                writer.append(String.valueOf(combattant.getPuissance())).append(",");
                writer.append(String.valueOf(combattant.getEndurance())).append(",");
                writer.append(combattant.getNiveau().name()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
