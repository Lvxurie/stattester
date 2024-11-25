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
        private double enemiesNeeded;
        private double xpRequired;

        public XPData(double enemiesNeeded, double xpRequired) {
            this.enemiesNeeded = enemiesNeeded;
            this.xpRequired = xpRequired;
        }

        public double getEnemiesNeeded() {
            return enemiesNeeded;
        }

        public double getXpRequired() {
            return xpRequired;
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

        double numerator = (double) (level - phaseRange[0] + 1); // +1 to include the starting level
        double denominator = (double) (phaseRange[1] - phaseRange[0] + 1); // +1 to include both ends

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
     * @param baseStats   List of base stats (e.g., HP, ATTACK, DEFENSE, SPECIAL, SPEED)
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

                // Iterate through all levels up to and including the current test level
                for (int level = 1; level <= testLevel; level++) {
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
                    double xpMultiplier = getXPMultiplier(xpCurve);
                    double growthChange = baseGrowth * (0.5 + growthProfile) * xpMultiplier;

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
     * Determines the XP multiplier based on the selected XP Curve.
     *
     * @param xpCurve The selected XP_Curve enum.
     * @return The corresponding XP multiplier.
     */
    private static double getXPMultiplier(XP_Curve xpCurve) {
        switch (xpCurve) {
            case VERY_SLOW:
                return 0.8; // Slows down growth
            case SLOW:
                return 0.9; // Slightly slows down growth
            case NEUTRAL:
                return 1.0; // No change
            case FAST:
                return 1.1; // Speeds up growth
            case VERY_FAST:
                return 1.2; // Significantly speeds up growth
            default:
                return 1.0; // Default to neutral if undefined
        }
    }

    /**
     * Calculates the XP required and enemies needed based on the selected XP Curve.
     *
     * @param bst       The Base Stat Total of the character.
     * @param xpCurve   The selected XP_Curve enum.
     * @param maxLevel  The maximum level to calculate up to.
     * @return A map where the key is the level and the value is the corresponding XPData.
     */
    public static Map<Integer, XPData> calculateXPSystem(double bst, XP_Curve xpCurve, int maxLevel) {
        Map<Integer, XPData> xpRequirements = new HashMap<>();

        for (int level = 1; level <= maxLevel; level++) {
            double xpPerEnemy = calculateXPPerEnemy(level, bst, xpCurve);
            double enemiesNeeded = calculateEnemiesNeeded(level, maxLevel);
            double xpRequired = enemiesNeeded * xpPerEnemy;

            XPData data = new XPData(enemiesNeeded, xpRequired);
            xpRequirements.put(level, data);
        }

        return xpRequirements;
    }

    /**
     * Calculates the XP gained per enemy based on level, BST, and XP Curve.
     *
     * @param level    Current level.
     * @param bst      Base Stat Total.
     * @param xpCurve  Selected XP_Curve enum.
     * @return XP gained per enemy.
     */
    private static double calculateXPPerEnemy(int level, double bst, XP_Curve xpCurve) {
        // Base formula
        double baseXP = Math.sqrt(bst) * level;

        // Adjust based on XP Curve
        switch (xpCurve) {
            case VERY_SLOW:
                return baseXP * 0.8; // Reduce XP gains
            case SLOW:
                return baseXP * 0.9;
            case NEUTRAL:
                return baseXP * 1.0;
            case FAST:
                return baseXP * 1.1;
            case VERY_FAST:
                return baseXP * 1.2;
            default:
                return baseXP;
        }
    }

    /**
     * Calculates the number of enemies needed to level up at a given level using quadratic scaling.
     *
     * @param level    Current level
     * @param maxLevel Maximum level
     * @return Number of enemies needed to level up
     */
    private static double calculateEnemiesNeeded(int level, int maxLevel) {
        double minEnemies = 3;
        double maxEnemies = 100;
        double scalingExponent = 2.0; // Quadratic scaling

        return minEnemies + (maxEnemies - minEnemies) * Math.pow(level - 1, scalingExponent) / Math.pow(maxLevel - 1, scalingExponent);
    }

    /**
     * Main method for testing purposes.
     */
    public static void main(String[] args) {
        // Example usage
        double bst = 500.0; // Example Base Stat Total
        XP_Curve selectedCurve = XP_Curve.NEUTRAL; // Example XP Curve
        int maxLevel = 150;

        // Example base stats and growths
        List<Double> baseStats = new ArrayList<>();
        baseStats.add(100.0); // HP
        baseStats.add(100.0); // ATTACK
        baseStats.add(100.0); // DEFENSE
        baseStats.add(100.0); // SPECIAL
        baseStats.add(100.0); // SPEED

        List<Double> baseGrowths = new ArrayList<>();
        baseGrowths.add(10.0); // HP Growth
        baseGrowths.add(10.0); // ATTACK Growth
        baseGrowths.add(10.0); // DEFENSE Growth
        baseGrowths.add(10.0); // SPECIAL Growth
        baseGrowths.add(10.0); // SPEED Growth

        List<String> phases = new ArrayList<>();
        phases.add("Neutral"); // Phase 1
        phases.add("Neutral"); // Phase 2
        phases.add("Neutral"); // Phase 3
        phases.add("Neutral"); // Phase 4

        // Calculate total stats
        List<Double> totalStats = calculate(baseStats, baseGrowths, phases, selectedCurve);

        // Calculate XP System
        Map<Integer, XPData> xpSystem = calculateXPSystem(bst, selectedCurve, maxLevel);

        // Print sample results
        int[] sampleLevels = {1, 30, 70, 110, 150};
        for (int level : sampleLevels) {
            XPData data = xpSystem.get(level);
            System.out.printf("Level %d: Enemies Needed = %.0f, XP Required = %.0f\n", level, data.getEnemiesNeeded(), data.getXpRequired());
        }
    }
}