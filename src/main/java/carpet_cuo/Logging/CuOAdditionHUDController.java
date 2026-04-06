package carpet_cuo.Logging;

import carpet.logging.LoggerRegistry;
import carpet_cuo.Logging.Logger.ScheduleQueueHUDLogger;


public class CuOAdditionHUDController {
    public static void updateHUD() {
        doHudLogging(CuOAdditionLoggerRegistry.__scheduleQueue, ScheduleQueueHUDLogger.getInstance());
    }

    private static void doHudLogging(boolean condition, AbstractHUDLogger logger) {
        if (condition) {
            LoggerRegistry.getLogger(logger.getName()).log(logger::onHudUpdate);
        }
    }
}
