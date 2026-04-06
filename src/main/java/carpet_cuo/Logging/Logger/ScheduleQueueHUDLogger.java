package carpet_cuo.Logging.Logger;

import carpet_cuo.Logging.AbstractHUDLogger;

import carpet_cuo.utils.LayOut;
import carpet_cuo.utils.Messenger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;


public class ScheduleQueueHUDLogger extends AbstractHUDLogger {
    public static final String NAME = "scheduleQueue";
    private static final ScheduleQueueHUDLogger INSTANCE = new ScheduleQueueHUDLogger();

    private ScheduleQueueHUDLogger() {
        super(NAME);
    }

    public static ScheduleQueueHUDLogger getInstance() {
        return INSTANCE;
    }

    @Override
    public MutableComponent[] onHudUpdate(String option, Player player) {
        Level level = player.level();
        int ScheduleTicks;
        int BlockTicks;
        int FluidTicks;
        BlockTicks = (level.getBlockTicks()).count();
        FluidTicks = (level.getFluidTicks()).count();
        ScheduleTicks = BlockTicks + FluidTicks;
        return new MutableComponent[]{
                Messenger.c(
                        Messenger.f(Messenger.s("S: "), LayOut.GRAY),
                        Messenger.f(Messenger.s(ScheduleTicks), LayOut.DARK_AQUA),
                        Messenger.f(Messenger.s(" B: "), LayOut.GRAY),
                        Messenger.f(Messenger.s(BlockTicks), LayOut.DARK_AQUA),
                        Messenger.f(Messenger.s(" F: "), LayOut.GRAY),
                        Messenger.f(Messenger.s(FluidTicks), LayOut.DARK_AQUA)
                )
        };
    }
}
