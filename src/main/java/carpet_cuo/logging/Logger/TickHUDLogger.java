package carpet_cuo.logging.Logger;

import carpet_cuo.logging.AbstractHUDLogger;
import carpet_cuo.utils.Messenger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
        ServerLevel level = (ServerLevel) player.level();
        MinecraftServer minecraftServer = level.getServer();
        int tick = minecraftServer.getTickCount();

        return new MutableComponent[]{
                Messenger.c(
                        Messenger.f(Messenger.s("Tick: "), ChatFormatting.WHITE),
                        Messenger.f(Messenger.s(tick), ChatFormatting.WHITE)
                )
        };
    }
}
