package carpet_cuo.Logging;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.MutableComponent;

public abstract class AbstractHUDLogger {
    private final String NAME;

    public AbstractHUDLogger(String name) {
        this.NAME = name;
    }

    public String getName() {
        return this.NAME;
    }

    public abstract MutableComponent[] onHudUpdate(String option, Player playerEntity);
}
