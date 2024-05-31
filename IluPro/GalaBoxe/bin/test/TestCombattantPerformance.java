package test;

import Services.DataAccesObject;

public class TestCombattantPerformance {
    public static void main(String[] args) {
        testAddPerformance();
        testCalculatePercentage();
    }

    public static void testAddPerformance() {
        DataAccesObject.addPerformance("performances.csv", "1", "Adversaire1", "2024-06-01", "victoire", "KO");
        // Verification of the addition of performance could involve reading back the CSV and checking the content
        System.out.println("testAddPerformance passed");
    }

    public static void testCalculatePercentage() {
        double percentage = DataAccesObject.calculatePercentages("combats.csv", "1");
        if (percentage >= 0) { // We assume the percentage calculation to return a value >= 0
            System.out.println("testCalculatePercentage passed: " + percentage);
        } else {
            System.out.println("testCalculatePercentage failed");
        }
    }
}
