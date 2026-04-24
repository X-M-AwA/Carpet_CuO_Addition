package carpet_cuo.Logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Translations;
import carpet_cuo.Logging.AbstractLogger;
import carpet_cuo.utils.LayOut;
import carpet_cuo.utils.Messenger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class UpdateLogger extends AbstractLogger {
    public static final String NAME = "update";
    private static final UpdateLogger INSTANCE = new UpdateLogger();

    private UpdateLogger() {
        super(NAME);
    }

    public static UpdateLogger getInstance() {
        return INSTANCE;
    }

    public void onUpdateSkipping(int depth, int x, int y, int z) {
    Logger logger = LoggerRegistry.getLogger(NAME);
    if (logger == null) return;

    String pos = String.format("[%d, %d, %d]", x, y, z);
    this.log((option) -> {
        if (option.equals("skipping")) {
            return new Component[]{
                Messenger.c(
                    Messenger.f(tr("carpet.logger.update.depth"), LayOut.WHITE),
                    Messenger.f(Component.literal(String.valueOf(depth)), LayOut.BLUE),
                    Messenger.f(tr("carpet.logger.update.pos"), LayOut.WHITE),
                    Messenger.f(Component.literal(pos), LayOut.GRAY)
                )
            };
        }
        return null;
        });
    }

    public void onDepthReached(int depth) {
    Logger logger = LoggerRegistry.getLogger(NAME);
    if (logger == null) return;

    this.log((option) -> {
        if (option.equals("chain")) {
            return new Component[]{
                Messenger.c(
                    Messenger.f(tr("carpet.logger.update.chainDepth"), LayOut.WHITE),
                    Messenger.f(Component.literal(String.valueOf(depth)), LayOut.BLUE)
                )
            };
        }
        return null;
        });
    }

    private MutableComponent tr(String key) {
        String val = Translations.tr(key, key);
        return Component.literal(val == null ? key : val);
    }
}