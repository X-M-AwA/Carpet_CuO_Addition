package carpet_cuo.logging;

import carpet.logging.LoggerRegistry;
import carpet_cuo.logging.Logger.ScheduleTickHUDLogger;
import carpet_cuo.logging.Logger.TickHUDLogger;

public class CuOAdditionHUDController {
    public static void updateHUD() {
        doHudLogging(CuOAdditionLoggerRegistry.__scheduleTick, ScheduleTickHUDLogger.getInstance());
        doHudLogging(CuOAdditionLoggerRegistry.__tick, TickHUDLogger.getInstance());
    }

    private static void doHudLogging(boolean condition, AbstractHUDLogger logger) {
        if (condition) {
            LoggerRegistry.getLogger(logger.getName()).log(logger::onHudUpdate);
        }
    }
}
