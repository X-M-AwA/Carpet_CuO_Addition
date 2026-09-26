package carpet_cuo.logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.logging.AbstractLogger;
import carpet_cuo.mixins.logger.DisplayInvoker;
import carpet_cuo.utils.Messenger;
import carpet_cuo.utils.NbtManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.Level;
//#if MC < 260200
import net.minecraft.world.entity.EntityType;
//#else
//$$ import net.minecraft.world.entity.EntityTypes;
//#endif
import java.util.concurrent.ConcurrentHashMap;

public class UpdateDepthVisualizeLogger extends AbstractLogger {
    public static final String NAME = "updateDepth";
    private static final UpdateDepthVisualizeLogger INSTANCE = new UpdateDepthVisualizeLogger();
    private static final int SURVIVE_TIME = 50;
    private static final ConcurrentHashMap<ResourceKey<Level>, ConcurrentHashMap<BlockPos, VisualizerEntry>> VISUALIZERS = new ConcurrentHashMap<>();

    private record VisualizerEntry(Display.TextDisplay entity, long expireTick) {}

    private UpdateDepthVisualizeLogger() {
        super(NAME);
    }

    public static UpdateDepthVisualizeLogger getInstance() {
        return INSTANCE;
    }

    public void text(ServerLevel level, BlockPos pos, int updateLimit) {
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) return;

        ConcurrentHashMap<BlockPos, VisualizerEntry> dimMap = VISUALIZERS.computeIfAbsent(level.dimension(), k -> new ConcurrentHashMap<>());

        long expireTick = level.getGameTime() + SURVIVE_TIME;
        String text = "Limit " + updateLimit;

        VisualizerEntry old = dimMap.get(pos);
        if (old != null && old.entity() != null && !old.entity().isRemoved()) {
            ((DisplayInvoker) old.entity()).carpet_cuo$setText(Messenger.f(Messenger.s(text), ChatFormatting.AQUA));
            dimMap.put(pos, new VisualizerEntry(old.entity(), expireTick));
            return;
        }

        Display.TextDisplay entity = createVisualizer(level, pos, text);
        dimMap.put(pos, new VisualizerEntry(entity, expireTick));
    }

    private static Display.TextDisplay createVisualizer(ServerLevel level, BlockPos pos, String text) {
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

        ((DisplayInvoker) entity).carpet_cuo$setText(Messenger.f(Messenger.s(text), ChatFormatting.AQUA));
        entity.setInvisible(true);
        entity.setNoGravity(true);
        //#if MC < 260300
        entity.setInvulnerable(true);
        //#else
        //$$ entity.setPermanentlyInvulnerable(true);
        //#endif
        entity.setPosRaw(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
        entity.addTag("DoNotTick");
        level.addFreshEntity(entity);
        entity.tick();
        return entity;
    }

    public static void tick(ServerLevel level) {
        ConcurrentHashMap<BlockPos, VisualizerEntry> dimMap = VISUALIZERS.get(level.dimension());
        if (dimMap == null || dimMap.isEmpty()) return;

        long currentTick = level.getGameTime();
        dimMap.entrySet().removeIf(entry -> {
            VisualizerEntry value = entry.getValue();
            if (currentTick < value.expireTick()) return false;

            Display.TextDisplay entity = value.entity();
            if (entity != null && !entity.isRemoved()) {
                entity.discard();
            }
            return true;
        });
        VISUALIZERS.computeIfPresent(level.dimension(), (k, v) -> v.isEmpty() ? null : v);
    }
}