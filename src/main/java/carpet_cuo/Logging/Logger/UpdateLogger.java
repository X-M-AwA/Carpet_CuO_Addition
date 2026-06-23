package carpet_cuo.Logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.Logging.AbstractLogger;
import carpet_cuo.utils.Messenger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

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
                    Messenger.f(Messenger.tr("carpet.logger.update.depth"), ChatFormatting.WHITE),
                    Messenger.f(Messenger.s(depth), ChatFormatting.BLUE),
                    Messenger.f(Messenger.tr("carpet.logger.update.pos"), ChatFormatting.WHITE),
                    Messenger.f(Messenger.s(pos), ChatFormatting.GRAY)
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
                    Messenger.f(Messenger.tr("carpet.logger.update.chainDepth"), ChatFormatting.WHITE),
                    Messenger.f(Messenger.s(depth), ChatFormatting.BLUE)
                )
            };
        }
        return null;
        });
    }
}