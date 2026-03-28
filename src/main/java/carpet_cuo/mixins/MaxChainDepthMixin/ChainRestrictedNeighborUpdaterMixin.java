package carpet_cuo.mixins.MaxChainDepthMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CollectingNeighborUpdater.class)
public abstract class ChainRestrictedNeighborUpdaterMixin {
    @Mutable
    @Shadow
    @Final
    private int maxChainedNeighborUpdates;

    @Inject(
            method = "addAndRun",
            at = @At("HEAD")
    )
    private void setMaxChainDepth(CallbackInfo ci) {
        maxChainedNeighborUpdates = Carpet_CuOSettings.maxChainDepth;
    }
}
