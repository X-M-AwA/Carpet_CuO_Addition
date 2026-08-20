package carpet_cuo.mixins.rules.MaxChainDepthMixin;

import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CollectingNeighborUpdater.class)
public interface ChainRestrictedNeighborUpdaterAccessor {
    @Mutable
    @Accessor("maxChainedNeighborUpdates")
    void carpet_cuo$setMaxChainedNeighborUpdates(int newValue);
}
