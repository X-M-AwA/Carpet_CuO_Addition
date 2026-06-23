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

import java.util.ArrayList;

public class FeatureLogger extends AbstractLogger {
    public static final String NAME = "feature";
    private static final FeatureLogger INSTANCE = new FeatureLogger();
    private final ArrayList<BlockPos> features = new ArrayList<>();
    private final ArrayList<Boolean> successes = new ArrayList<>();
    private boolean bl = false;
    private ChunkPos chunkPos;

    public FeatureLogger() {
        super(NAME);
    }

    public static FeatureLogger getInstance() {
        return INSTANCE;
    }

    public void print() {
        Logger logger = LoggerRegistry.getLogger(NAME);
        if (logger == null) return;

        ChatFormatting color = bl ? ChatFormatting.GREEN : ChatFormatting.GRAY;
        bl = false;

        BlockPos[] positions = features.toArray(new BlockPos[0]);
        Boolean[] results = successes.toArray(new Boolean[0]);
        int size = features.size();
        features.clear();
        successes.clear();

        ArrayList<MutableComponent> hoverLines = new ArrayList<>();
        hoverLines.add(
                Messenger.f(Messenger.s("ChunkPos: " + chunkPos.toString()), ChatFormatting.DARK_AQUA)
        );
        for (int i = 0; i < size; i++) {
            BlockPos pos = positions[i];
            boolean success = results[i];
            String posStr = "\n[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
            hoverLines.add(
                    Messenger.c(
                            Messenger.f(Messenger.s(posStr), ChatFormatting.GRAY),
                            success ? Messenger.f(Messenger.s("√"), ChatFormatting.GREEN) : Messenger.f(Messenger.s("x"), ChatFormatting.RED)
                    )
            );
        }

        this.log((option) -> {
            if (option.equals("monsterRoom")) {
                return new Component[]{
                        Messenger.c(
                                Messenger.f(Messenger.s("#"), ChatFormatting.DARK_GREEN),
                                Messenger.hover(
                                        Messenger.f(Messenger.s("MonsterRoom"), color),
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                Messenger.c(
                                                        hoverLines.toArray()
                                                )
                                        )
                                )
                        )
                };
            } else if (option.equals("endGateway")) {
                return new Component[]{Messenger.c(

                )};
            }
            return null;
        });
    }

    public void Cache(BlockPos blockPos, boolean success) {
        if (!this.bl) this.bl = success;
        chunkPos = new ChunkPos(blockPos);
        this.features.add(blockPos);
        this.successes.add(success);
    }
}
