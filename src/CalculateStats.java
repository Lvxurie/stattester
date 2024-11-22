import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateStats {
    private static final Map<String, Double> growthRates = new HashMap<String, Double>();
    private static final Map<String, int[]> levelRanges = new HashMap<String, int[]>();
    private static final int[] testLevels = new int[]{1,30,70,110,150};
    public static void calculate(List<Double> stats, List<String> phases){
     init();
     //For each level we need stats for do the following
     for (int level : testLevels){
         //What phase is the current level in
         String currentPhase = getPhase(phases, level);
         int[] thisPhase = levelRanges.get(currentPhase);
         //Where in the growth curve are we?
         double normalisedX = (double) (level - thisPhase[0]) /(thisPhase[1]-thisPhase[2]);
         for(double stat : stats){
             //basestat/2 + (basestat * growthprofile)
         }
     }
    }
    private static void init(){
        growthRates.put("Very Slow", 2.0);
        growthRates.put("Slow", 1.5);
        growthRates.put("Neutral", 1.0);
        growthRates.put("Fast", 0.5);
        growthRates.put("Very Fast", 0.7);

        levelRanges.put("Phase 1",new int[] {1, 40});
        levelRanges.put("Phase 2",new int[] {41, 80});
        levelRanges.put("Phase 3",new int[] {81, 120});
        levelRanges.put("Phase 4",new int[] {121, 150});
    }
    private static String getPhase(List<String> phases, int level){
        if(phases.get(0)!= null){
            for (String phase : phases){
                int[] thisPhase = levelRanges.get(phase);
                if(level >= thisPhase[0] && level <= thisPhase[1]){
                    return phase;
                }
            }
        }
        return "err";
    }
}
