package carpet_cuo.Logging.Logger;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet_cuo.Logging.AbstractLogger;
import carpet_cuo.utils.Messenger;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ChunkPos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class FeatureLogger extends AbstractLogger {
    public static final String NAME = "feature";
    private static final FeatureLogger INSTANCE = new FeatureLogger();
    private final ArrayList<Features> features = new ArrayList<>();
    private boolean bl = false;

    public FeatureLogger() {
        super(NAME);
    }

    public static FeatureLogger getInstance() {
        return INSTANCE;
    }

    public void print(ChunkPos chunkPos) {
        synchronized (features) {
            if (features.isEmpty()) return;
        }
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) {
            synchronized (features) {
                features.clear();
            }
            return;
        }

        this.log((option) -> {
            ArrayList<Features> matched = new ArrayList<>();
            synchronized (features) {
                for (Features f : features) {
                    if (f.type.optionName.equals(option)) {
                        matched.add(f);
                    }
                }
            }
            if (matched.isEmpty()) return null;

            ChatFormatting color = bl ? ChatFormatting.GREEN : ChatFormatting.GRAY;
            bl = false;
            ArrayList<MutableComponent> hoverLines = new ArrayList<>(matched.size() + 1);
            hoverLines.add(Messenger.f(Messenger.s("ChunkPos: " + chunkPos), ChatFormatting.DARK_AQUA));
            for (Features f : matched) {
                String pos = "\n[" + f.blockPos.getX() + ", " + f.blockPos.getY() + ", " + f.blockPos.getZ() + "]";
                hoverLines.add(
                        Messenger.c(
                                Messenger.f(Messenger.s(pos), ChatFormatting.GRAY),
                                f.success ? Messenger.f(Messenger.s("√"), ChatFormatting.GREEN) : Messenger.f(Messenger.s("x"), ChatFormatting.RED)
                        )
                );
            }

            FeatureType type = matched.getFirst().type;
            MutableComponent component = Messenger.s(type.displayName);
            synchronized (features) {
                features.removeAll(matched);
            }

            return new Component[]{
                    Messenger.c(
                            Messenger.f(Messenger.s("#"), ChatFormatting.DARK_GREEN),
                            Messenger.hover(
                                    Messenger.f(component, color),
                                    new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Messenger.c(
                                                    hoverLines.toArray()
                                            )
                                    )
                            )
                    )
            };
        });
    }

    public void Cache(BlockPos blockPos, boolean success, FeatureType featureType) {
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) return;

        if (hasOnlineSubscriberForOption(logger, featureType.optionName)) {
            if (!this.bl) this.bl = success;
            this.features.add(new Features(blockPos, success, featureType));
        }
    }

    private boolean hasOnlineSubscriberForOption(Logger logger, String option) {
        try {
            Field field = Logger.class.getDeclaredField("subscribedOnlinePlayers");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) field.get(logger);
            return map.containsValue(option);
        } catch (Exception e) {
            return true;
        }
    }

    private record Features(BlockPos blockPos, boolean success, FeatureType type) {
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