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
    private static final int[] testLevels = new int[]{1, 30, 70, 110, 150}; // Included Level 150

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
     * Calculates the total stats for each base stat at specified test levels.
     *
     * @param baseStats   List of base stats (e.g., HP, ATTACK, DEFENSE, SPECIAL, SPEED)
     * @param baseGrowths List of base growths corresponding to each base stat
     * @param phases      List of phase growth rates (e.g., Neutral, Fast, Slow)
     * @param xpRate      The global XP Rate affecting all stats
     * @return List of total stats calculated for each stat at each test level
     */
    public static List<Double> calculate(List<Double> baseStats, List<Double> baseGrowths, List<String> phases, String xpRate) {
        init(); // Initialize growth rates and level ranges

        List<Double> totalStats = new ArrayList<>();

        // Get the XP Growth Rate value
        double xpGrowthRate = growthRates.getOrDefault(xpRate, 1.0); // Default to 1.0 if not found

        // Iterate through each base stat
        for (int statIndex = 0; statIndex < baseStats.size(); statIndex++) {
            double baseStat = baseStats.get(statIndex);
            double baseGrowth = baseGrowths.get(statIndex);

            // Iterate through each test level
            for (int testLevel : testLevels) {
                double cumulativeGrowth = 0.0; // Reset cumulative growth for each test level

                // Iterate through all levels up to and including the current test level
                for (int level = 1; level <= testLevel; level++) {
                    int currentPhase = getPhase(level);
                    if (currentPhase == -1) {
                        // Invalid phase, skip this level
                        continue;
                    }

                    String phaseName = "Phase " + (currentPhase + 1);
                    String phaseGrowthRateName = phases.get(currentPhase);
                    double phaseGrowthRate = growthRates.getOrDefault(phaseGrowthRateName, 1.0);

                    double normalizedX = getNormalizedX(currentPhase, level);
                    double growthProfile = Math.pow(normalizedX, phaseGrowthRate);
                    double growthChange = baseGrowth * (0.5 + growthProfile) * xpGrowthRate; // Incorporate XP Rate
                    cumulativeGrowth += growthChange;
                }

                // Calculate Total Stat: Base Stat + Cumulative Growth
                double totalStat = baseStat + cumulativeGrowth;
                totalStats.add(totalStat);
            }
        }

        return totalStats;
    }
}