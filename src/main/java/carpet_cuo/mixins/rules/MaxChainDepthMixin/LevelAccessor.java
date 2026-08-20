package carpet_cuo.mixins.rules.MaxChainDepthMixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.NeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Level.class)
public interface LevelAccessor {
    @Accessor("neighborUpdater")
    NeighborUpdater carpet_cuo$getNeighborUpdater();
}
