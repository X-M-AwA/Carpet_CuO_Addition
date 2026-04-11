package carpet_cuo.Logging.Logger;

import carpet_cuo.Logging.AbstractHUDLogger;
import carpet_cuo.utils.LayOut;
import carpet_cuo.utils.Messenger;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class TickHUDLogger extends AbstractHUDLogger {
    public static final String NAME = "tick";
    private static final TickHUDLogger INSTANCE = new TickHUDLogger();

    private TickHUDLogger() {
        super(NAME);
    }

    public static TickHUDLogger getInstance() {
        return INSTANCE;
    }

    @Override
    public MutableComponent[] onHudUpdate(String option, Player player) {
        MinecraftServer minecraftServer = player.getServer();
        int tick = 0;
        if (minecraftServer != null) {
            tick = minecraftServer.getTickCount();
        }

        return new MutableComponent[]{
                Messenger.c(
                        Messenger.f(Messenger.s("Tick: "), LayOut.WHITE),
                        Messenger.f(Messenger.s(tick), LayOut.WHITE)
                )
        };
    }
}
