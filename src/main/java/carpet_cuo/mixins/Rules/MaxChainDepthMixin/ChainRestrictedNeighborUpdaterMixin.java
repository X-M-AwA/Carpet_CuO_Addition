package carpet_cuo.mixins.Rules.MaxChainDepthMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.*;
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
        if (this.maxChainedNeighborUpdates != Carpet_CuOSettings.maxChainDepth) {
            this.maxChainedNeighborUpdates = Carpet_CuOSettings.maxChainDepth;
        }
    }
}
