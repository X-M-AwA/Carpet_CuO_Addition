package carpet_cuo.Logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.Logging.AbstractLogger;
import carpet_cuo.utils.Messenger;
import carpet_cuo.utils.NbtManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
//#if MC < 260200
import net.minecraft.world.entity.EntityType;
//#else
//$$ import net.minecraft.world.entity.EntityTypes;
//#endif
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateDepthVisualizeLogger extends AbstractLogger {
    public static final String NAME = "updateDepth";
    private static final UpdateDepthVisualizeLogger INSTANCE = new UpdateDepthVisualizeLogger();
    private static final int SURVIVE_TIME = 50;
    private static final ConcurrentHashMap<BlockPos, Map.Entry<Display.TextDisplay, Long>> VISUALIZERS = new ConcurrentHashMap<>();

    public UpdateDepthVisualizeLogger() {
        super(NAME);
    }

    public static UpdateDepthVisualizeLogger getInstance() {
        return INSTANCE;
    }

    public void Text(ServerLevel level, BlockPos pos, int UpdateLimit) {
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) return;

        Map.Entry<Display.TextDisplay, Long> oldEntry = VISUALIZERS.remove(pos);
        if (oldEntry != null && oldEntry.getKey() != null) {
            oldEntry.getKey().discard();
        }

        Display.TextDisplay entity = new Display.TextDisplay(
                //#if MC < 260200
                EntityType.TEXT_DISPLAY,
                //#else
                //$$ EntityTypes.TEXT_DISPLAY,
                //#endif
                level);
        CompoundTag nbt = NbtManager.readFromEntity(entity, new CompoundTag());

        nbt.putString("billboard", "center");
        nbt.putByte("see_through", (byte) 1);
        NbtManager.writeToEntity(entity, nbt);

        entity.setText(Messenger.f(Messenger.s("Limit " + UpdateLimit), ChatFormatting.DARK_AQUA));
        entity.setInvisible(true);
        entity.setNoGravity(true);
        entity.setInvulnerable(true);
        entity.setPosRaw(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
        entity.addTag("DoNotTick");
        level.addFreshEntity(entity);

        long expireTick = level.getGameTime() + SURVIVE_TIME;
        VISUALIZERS.put(pos, Map.entry(entity, expireTick));
    }

    public static void tick(ServerLevel level) {
        long currentTick = level.getGameTime();
        VISUALIZERS.entrySet().removeIf(entry -> {
            Map.Entry<Display.TextDisplay, Long> value = entry.getValue();
            if (currentTick > value.getValue()) {
                Display.TextDisplay entity = value.getKey();
                if (entity != null && !entity.isRemoved()) {
                    entity.discard();
                }
                return true;
            }
            return false;
        });
    }
}