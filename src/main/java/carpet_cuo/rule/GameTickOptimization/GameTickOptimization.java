package carpet_cuo.rule.GameTickOptimization;

public class GameTickOptimization {
    public static boolean weather;
    public static boolean scheduleTick;
    public static boolean raids;
    public static boolean chunkTick;
    public static boolean chunk;
    public static boolean chunkUnlade;
    public static boolean purgeLoadingTickets;
    public static boolean blockEvent;
    public static boolean dragonFight;
    public static boolean entityUpdate;
    public static boolean entityDespawn;
    public static boolean blockEntity;

    public static void setSkipPhases(String config) {
        weather = false;
        scheduleTick = false;
        raids = false;
        chunkTick = false;
        chunk = false;
        chunkUnlade = false;
        purgeLoadingTickets = false;
        blockEvent = false;
        dragonFight = false;
        entityUpdate = false;
        entityDespawn = false;
        blockEntity = false;
        if (config == null || config.trim().isEmpty()) return;

        String[] phases = config.split(",");
        for (String phase : phases) {
            String p = phase.trim();
            switch (p) {
                case "weather" -> weather = true;
                case "scheduleTick", "TT", "NTE" -> scheduleTick = true;
                case "raids" -> raids = true;
                case "chunkTick", "CT" -> chunkTick = true;
                case "chunk" -> chunk = true;
                case "chunkUnlade" -> chunkUnlade = true;
                case "purgeLoadingTickets", "loadingTickets" -> purgeLoadingTickets = true;
                case "blockEvent", "BE" -> blockEvent = true;
                case "dragonFight" -> dragonFight = true;
                case "entityUpdate", "EU" -> entityUpdate = true;
                case "entityDespawn" -> entityDespawn = true;
                case "blockEntity", "TE" -> blockEntity = true;
            }
        }
    }
}
