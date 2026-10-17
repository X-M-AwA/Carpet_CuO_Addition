package carpet_cuo.logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.logging.AbstractLogger;
import carpet_cuo.mixins.logger.FeatureMixin.LoggerAccessor;
import carpet_cuo.utils.Messenger;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeatureLogger extends AbstractLogger {
    public static final String NAME = "feature";
    private static final FeatureLogger INSTANCE = new FeatureLogger();
    private final ArrayList<Features> features = new ArrayList<>();

    private FeatureLogger() {
        super(NAME);
    }

    public static FeatureLogger getInstance() {
        return INSTANCE;
    }

    public void print(ChunkPos chunkPos) {
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) {
            this.clear(chunkPos);
            return;
        }

        List<Features> chunkFeatures;
        synchronized (this.features) {
            chunkFeatures = this.features.stream()
                    .filter(f -> f.chunkPos.equals(chunkPos))
                    .toList();
        }

        if (chunkFeatures.isEmpty()) {
            return;
        }

        // 2. 构造日志；lambda 只读快照，不修改共享状态
        this.log((option) -> {
            List<Features> matched = chunkFeatures.stream()
                    .filter(f -> f.type.optionName.equals(option))
                    .toList();
            if (matched.isEmpty()) return null;

            boolean anySuccess = matched.stream().anyMatch(Features::success);
            ChatFormatting color = anySuccess ? ChatFormatting.GREEN : ChatFormatting.GRAY;

            ArrayList<MutableComponent> hoverLines = new ArrayList<>(matched.size() + 1);
            hoverLines.add(Messenger.f(Messenger.s("Chunk pos: " + chunkPos), ChatFormatting.DARK_AQUA));
            for (Features f : matched) {
                String pos = "\n[" + f.blockPos.getX() + ", " + f.blockPos.getY() + ", " + f.blockPos.getZ() + "]";
                hoverLines.add(
                        Messenger.c(
                                Messenger.f(Messenger.s(pos), ChatFormatting.GRAY),
                                f.success ? Messenger.f(Messenger.s("√"), ChatFormatting.GREEN) : Messenger.f(Messenger.s("x"), ChatFormatting.RED)
                        )
                );
            }

            FeatureType type = matched.get(0).type;
            MutableComponent component = Messenger.s(type.displayName);

            return new Component[]{
                    Messenger.c(
                            Messenger.f(Messenger.s("#"), ChatFormatting.GRAY),
                            Messenger.hover(
                                    Messenger.f(component, color),
                                    //#if MC < 12105
                                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Messenger.c(
                                                    hoverLines.toArray()
                                            )
                                    )
                                    //#else
                                    //$$ new HoverEvent.ShowText(
                                    //$$    Messenger.c(
                                    //$$        hoverLines.toArray()
                                    //$$    )
                                    //$$ )
                                    //#endif
                            )
                    )
            };
        });
        this.clear(chunkPos);
    }

    private void clear(ChunkPos chunkPos) {
        synchronized (this.features) {
            this.features.removeIf(f -> f.chunkPos.equals(chunkPos));
        }
    }

    public void cache(BlockPos blockPos, boolean success, FeatureType featureType) {
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) return;

        if (this.hasOnlineSubscriberForOption(logger, featureType.optionName)) {
            ChunkPos chunkPos = new ChunkPos(blockPos);
            synchronized (this.features) {
                this.features.add(new Features(blockPos, success, featureType, chunkPos));
            }
        }
    }

    private boolean hasOnlineSubscriberForOption(Logger logger, String option) {
        try {
            Map<String, String> map = ((LoggerAccessor) logger).getSubscribedOnlinePlayers();
            return map.containsValue(option);
        } catch (Exception e) {
            return true;
        }
    }

    private record Features(BlockPos blockPos, boolean success, FeatureType type, ChunkPos chunkPos) {
    }

    public enum FeatureType {
        MONSTER_ROOM("monsterRoom", "MonsterRoom"),
        AMETHYST_GEODE("amethystGeode", "AmethystGeode"),
        DESERT_WELL("desertWell", "DesertWell");
        public final String optionName;
        public final String displayName;

        FeatureType(String optionName, String displayName) {
            this.optionName = optionName;
            this.displayName = displayName;
        }
    }
}