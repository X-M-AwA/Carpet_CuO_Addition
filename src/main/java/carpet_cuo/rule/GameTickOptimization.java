package carpet_cuo.rule;

public class GameTickOptimization {
    public static boolean weather;
    public static boolean scheduleTick;
    public static boolean raids;
    public static boolean chunkTick;
    public static boolean blockEvent;
    public static boolean entityUpdate;
    public static boolean blockEntity;

    public static void setSkipPhases(String config) {
        weather = false;
        scheduleTick = false;
        raids = false;
        chunkTick = false;
        blockEvent = false;
        entityUpdate = false;
        blockEntity = false;
        if (config == null || config.trim().isEmpty()) return;

        String[] phases = config.split(",");
        for (String phase : phases) {
            String p = phase.trim();
            switch (p) {
                case "weather": weather = true; break;
                case "scheduleTick", "TT", "NTE": scheduleTick = true; break;
                case "raids": raids = true; break;
                case "chunk", "CT": chunkTick = true; break;
                case "blockEvent", "BE": blockEvent = true; break;
                case "entityUpdate", "EU": entityUpdate = true; break;
                case "blockEntity", "TE": blockEntity = true; break;
            }
        }
    }
}
