package carpet_cuo.Logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.Logging.AbstractLogger;
import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import carpet_cuo.utils.LayOut;
import carpet_cuo.utils.Messenger;
import net.minecraft.network.chat.Component;

public class UpdateSkippingLogger extends AbstractLogger {
    public static final String NAME = "updateSkipping";
    private static final UpdateSkippingLogger INSTANCE = new UpdateSkippingLogger();

    private UpdateSkippingLogger() {
        super(NAME);
    }

    public static UpdateSkippingLogger getInstance() {
        return INSTANCE;
    }

    public void onDepthReached(int depth, int x, int y, int z) {
        if (!CuOAdditionLoggerRegistry.__updateSkipping) return;

        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) return;

        String pos = String.format("[" + "%d, %d, %d" + "]", x, y, z);

            this.log((playerOption -> new Component[]{
                    Messenger.c(
                            Messenger.f(Messenger.s("#造成了更新跳略,深度为:"), LayOut.WHITE),
                            Messenger.f(Messenger.s(depth), LayOut.RED, LayOut.BOLD),
                            Messenger.f(Messenger.s("在坐标"), LayOut.WHITE),
                            Messenger.f(Messenger.s(pos), LayOut.GRAY, LayOut.BOLD)
                    )
            }));
    }
}
