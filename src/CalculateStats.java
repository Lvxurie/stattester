import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateStats {

    // Mapping of growth rate names to their corresponding numerical values
    private static final Map<String, Double> growthRates = new HashMap<>();

    // Mapping of phase names to their corresponding level ranges
    private static final Map<String, int[]> levelRanges = new HashMap<>();

    // Test levels at which stats are calculated
    private static final int[] testLevels = new int[]{1, 30, 70, 110, 150};

    /**
     * Enum representing different XP Curves.
     */
    public enum XP_Curve {
        VERY_SLOW, // For very powerful creatures
        SLOW,      // For powerful creatures
        NEUTRAL,   // For balanced creatures
        FAST,      // For less powerful creatures
        VERY_FAST  // For very easy creatures
    }

    /**
     * Inner class to hold XP data.
     */
    public static class XPData {
        private double xpToNextLevel;
        private double cumulativeXP;

        public XPData(double xpToNextLevel, double cumulativeXP) {
            this.xpToNextLevel = xpToNextLevel;
            this.cumulativeXP = cumulativeXP;
        }

        public double getXpToNextLevel() {
            return xpToNextLevel;
        }

        public double getCumulativeXP() {
            return cumulativeXP;
        }
    }

    /**
     * Initializes the growth rates and level ranges for each phase.
     */
    private static void init() {
        // Define growth rates corresponding to each option
        growthRates.put("Very slow", 2.0);
        growthRates.put("Slow", 1.5);
        growthRates.put("Neutral", 1.0);
        growthRates.put("Fast", 0.5);
        growthRates.put("Very fast", 0.7);

        // Define level ranges for each phase
        levelRanges.put("Phase 1", new int[]{1, 40});
        levelRanges.put("Phase 2", new int[]{41, 80});
        levelRanges.put("Phase 3", new int[]{81, 120});
        levelRanges.put("Phase 4", new int[]{121, 160});
    }

    /**
     * Determines the phase index for a given level.
     *
     * @param level Current level
     * @return Phase index (0-based) or -1 if invalid
     */
    private static int getPhase(int level) {
        for (int i = 0; i < levelRanges.size(); i++) {
            String phase = "Phase " + (i + 1);
            int[] phaseRange = levelRanges.get(phase);
            if (phaseRange == null) {
                continue;
            }
            if (level >= phaseRange[0] && level <= phaseRange[1]) {
                return i;
            }
        }
        // If level is outside all defined ranges, return -1 to indicate invalid phase
        return -1;
    }

    /**
     * Normalizes the current level within its phase.
     *
     * @param currentPhase Index of the current phase (0-based)
     * @param level        Current level
     * @return Normalized value between 0 and 1
     */
    private static double getNormalizedX(int currentPhase, int level) {
        String phaseName = "Phase " + (currentPhase + 1);
        int[] phaseRange = levelRanges.get(phaseName);
        if (phaseRange == null) {
            return 0.0;
        }

        double numerator = (double) (level - phaseRange[0]); // Adjusted to exclude starting level
        double denominator = (double) (phaseRange[1] - phaseRange[0]);

        // Prevent division by zero
        if (denominator == 0) {
            return 0.0;
        }

        return numerator / denominator;
    }

    /**
     * Calculates the total stats for each base stat at specified test levels,
     * incorporating base growths, phase growth rates, and XP curves.
     *
     * @param baseStats   List of base stats (e.g., HP, Attack, Armor, Shield, Special, Speed)
     * @param baseGrowths List of base growths corresponding to each base stat
     * @param phases      List of phase growth rates (e.g., Neutral, Fast, Slow)
     * @param xpCurve     The selected XP_Curve affecting growth rates
     * @return List of total stats calculated for each stat at each test level
     */
    public static List<Double> calculate(List<Double> baseStats, List<Double> baseGrowths, List<String> phases, XP_Curve xpCurve) {
        init(); // Initialize growth rates and level ranges

        List<Double> totalStats = new ArrayList<>();

        // Iterate through each base stat
        for (int statIndex = 0; statIndex < baseStats.size(); statIndex++) {
            double baseStat = baseStats.get(statIndex);
            double baseGrowth = baseGrowths.get(statIndex);

            // Iterate through each test level
            for (int testLevelIndex = 0; testLevelIndex < testLevels.length; testLevelIndex++) {
                int testLevel = testLevels[testLevelIndex];
                double cumulativeGrowth = 0.0; // Reset cumulative growth for each test level

                // Iterate through all levels from level 2 up to and including the current test level
                for (int level = 2; level <= testLevel; level++) {
                    int currentPhase = getPhase(level);
                    if (currentPhase == -1) {
                        // Invalid phase, skip this level
                        continue;
                    }

                    String phaseGrowthRateName = phases.get(currentPhase);
                    double phaseGrowthRate = growthRates.getOrDefault(phaseGrowthRateName, 1.0);

                    double normalizedX = getNormalizedX(currentPhase, level);
                    double growthProfile = Math.pow(normalizedX, phaseGrowthRate);

                    // Adjust growth change based on XP Curve
                    // Since getXPMultiplier is obsolete, we can remove it or replace with appropriate logic
                    // For now, we'll assume no adjustment based on XP Curve in stat growth
                    double growthChange = baseGrowth * (0.5 + growthProfile);

                    cumulativeGrowth += growthChange;
                }

                // Calculate Total Stat: Base Stat + Cumulative Growth
                double totalStat = baseStat + cumulativeGrowth;
                totalStats.add(totalStat);
            }
        }

        return totalStats;
    }

    /**
     * Calculates the XP required to level up from each level to the next, based on the XP Curve.
     *
     * @param bst       The Base Stat Total of the character.
     * @param xpCurve   The selected XP_Curve enum.
     * @param maxLevel  The maximum level to calculate up to.
     * @return A map where the key is the level and the value is the corresponding XPData.
     */
    public static Map<Integer, XPData> calculateXPSystem(double bst, XP_Curve xpCurve, int maxLevel) {
        Map<Integer, XPData> xpRequirements = new HashMap<>();
        double cumulativeXP = 0.0;

        // Calculate Base XP using your formula
        double baseXP = 3 * Math.sqrt(bst);

        for (int level = 1; level <= maxLevel; level++) {
            double exponent = getXPExponent(xpCurve);
            double xpToNextLevel = baseXP * Math.pow(level, exponent);
            cumulativeXP += xpToNextLevel;

            XPData data = new XPData(xpToNextLevel, cumulativeXP);
            xpRequirements.put(level, data);
        }

        return xpRequirements;
    }

    /**
     * Determines the exponent used in the XP formula based on the selected XP Curve.
     *
     * @param xpCurve The selected XP_Curve enum.
     * @return The exponent for the XP formula.
     */
    private static double getXPExponent(XP_Curve xpCurve) {
        switch (xpCurve) {
            case VERY_SLOW:
                return 2.0; // Steeper curve, more XP needed
            case SLOW:
                return 1.8;
            case NEUTRAL:
                return 1.6;
            case FAST:
                return 1.4;
            case VERY_FAST:
                return 1.3; // Shallower curve, less XP needed
            default:
                return 1.6; // Default to neutral if undefined
        }
    }

    /**
     * Main method for testing purposes.
     */
    public static void main(String[] args) {
        init(); // Make sure to initialize growth rates and level ranges

        // Example character with BST of 300
        double bst = 300.0;
        int maxLevel = 10;

        // Test with different XP Curves
        XP_Curve[] xpCurves = XP_Curve.values();

        for (XP_Curve xpCurve : xpCurves) {
            System.out.println("XP Requirements for XP Curve: " + xpCurve);
            Map<Integer, XPData> xpSystem = calculateXPSystem(bst, xpCurve, maxLevel);
            printXPRequirements(xpSystem);
            System.out.println();
        }
    }

    // Helper function to print XP requirements
    private static void printXPRequirements(Map<Integer, XPData> xpSystem) {
        System.out.printf("%-10s %-20s %-20s\n", "Level", "XP to Next Level", "Cumulative XP");
        for (int level = 1; level <= xpSystem.size(); level++) {
            XPData data = xpSystem.get(level);
            System.out.printf("%-10d %-20.2f %-20.2f\n", level, data.getXpToNextLevel(), data.getCumulativeXP());
        }
    }
}