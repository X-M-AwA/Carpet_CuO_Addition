package carpet_cuo.logging.Logger;

import carpet_cuo.logging.AbstractHUDLogger;

import carpet_cuo.utils.Messenger;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;


public class ScheduleTickHUDLogger extends AbstractHUDLogger {
    public static final String NAME = "scheduleTick";
    private static final ScheduleTickHUDLogger INSTANCE = new ScheduleTickHUDLogger();

    private ScheduleTickHUDLogger() {
        super(NAME);
    }

    public static ScheduleTickHUDLogger getInstance() {
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
                        Messenger.f(Messenger.s("S: "), ChatFormatting.GRAY),
                        Messenger.f(Messenger.s(ScheduleTicks), ChatFormatting.DARK_GREEN),
                        Messenger.f(Messenger.s(" B: "), ChatFormatting.GRAY),
                        Messenger.f(Messenger.s(BlockTicks), ChatFormatting.DARK_AQUA),
                        Messenger.f(Messenger.s(" F: "), ChatFormatting.GRAY),
                        Messenger.f(Messenger.s(FluidTicks), ChatFormatting.DARK_PURPLE)
                )
        };
    }
}
