package Services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataAccesObject {

    private static String executeOCaml(String... command) {
        try {
            List<String> cmdList = new ArrayList<>();
            cmdList.add("ocamlrun");
            cmdList.add("crud_csv");
            cmdList.addAll(Arrays.asList(command));
            ProcessBuilder pb = new ProcessBuilder(cmdList);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean verifyUser(String file, String id, String password) {
        String result = executeOCaml("verify_user", file, id, password);
        return Boolean.parseBoolean(result.trim());
    }

    public static void addPerformance(String file, String id, String adversaire, String date, String resultat, String type_victoire) {
        executeOCaml("add_performance", file, id, adversaire, date, resultat, type_victoire);
    }

    public static void updateInfo(String file, String id, String field, String value) {
        executeOCaml("update_info", file, id, field, value);
    }

    public static void addCombatDetails(String file, String id, String coups_reussis, String coups_manques, String types_coups, String zones_touchees) {
        executeOCaml("add_combat_details", file, id, coups_reussis, coups_manques, types_coups, zones_touchees);
    }

    public static double calculatePercentages(String file, String id) {
        String result = executeOCaml("calculate_percentages", file, id);
        return Double.parseDouble(result.trim());
    }
}
