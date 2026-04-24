package carpet_cuo.Logging;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.Logging.Logger.ScheduleQueueHUDLogger;
import carpet_cuo.Logging.Logger.TickHUDLogger;
import carpet_cuo.Logging.Logger.UpdateLogger;


import java.lang.reflect.Field;

public class CuOAdditionLoggerRegistry {
    public static boolean __scheduleQueue;
    public static boolean __tick;
    public static boolean __update;

    public static void registerLoggers() {
        LoggerRegistry.registerLogger(ScheduleQueueHUDLogger.NAME, standardHUDLogger(ScheduleQueueHUDLogger.NAME, null, null));
        LoggerRegistry.registerLogger(TickHUDLogger.NAME, standardHUDLogger(TickHUDLogger.NAME, null, null));
        LoggerRegistry.registerLogger(UpdateLogger.NAME, standardLogger(UpdateLogger.NAME, "skipping", new String[]{"skipping", "chain"}));
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
