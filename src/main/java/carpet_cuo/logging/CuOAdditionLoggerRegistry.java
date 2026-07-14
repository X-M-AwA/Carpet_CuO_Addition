package carpet_cuo.logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.logging.Logger.*;


import java.lang.reflect.Field;

public class CuOAdditionLoggerRegistry {
    public static boolean __scheduleTick;
    public static boolean __tick;
    public static boolean __update;
    public static boolean __updateDepth;
    public static boolean __feature;

    public static void registerLoggers() {
        LoggerRegistry.registerLogger(ScheduleTickHUDLogger.NAME, standardHUDLogger(ScheduleTickHUDLogger.NAME, null, null));
        LoggerRegistry.registerLogger(TickHUDLogger.NAME, standardHUDLogger(TickHUDLogger.NAME, null, null));
        LoggerRegistry.registerLogger(UpdateLogger.NAME, standardLogger(UpdateLogger.NAME, "skipping", new String[]{"skipping", "chain"}));
        LoggerRegistry.registerLogger(UpdateDepthVisualizeLogger.NAME, standardLogger(UpdateDepthVisualizeLogger.NAME, null, null));
        LoggerRegistry.registerLogger(FeatureLogger.NAME, standardLogger(FeatureLogger.NAME, "monsterRoom", new String[]{"monsterRoom", "amethystGeode", "desertWell"}));
    }

    public static Logger standardLogger(String logName, String def, String [] options) {
        return new Logger(getLoggerField(logName), logName, def, options, false);
    }

    public static HUDLogger standardHUDLogger(String logName, String def, String [] options) {
        return new HUDLogger(getLoggerField(logName), logName, def, options, false);
    }

    public static Field getLoggerField(String logName) {
        try {
            return CuOAdditionLoggerRegistry.class.getField("__" + logName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException();
        }
    }
}
