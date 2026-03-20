package carpet_cuo.mixins.MaxChainDepthMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChainRestrictedNeighborUpdater.class)
public abstract class ChainRestrictedNeighborUpdaterMixin {
    @Mutable
    @Shadow
    @Final
    private int maxChainDepth;

    @Inject(
            method = "runQueuedUpdates",
            at = @At("HEAD")
    )
    private void setMaxChainDepth(CallbackInfo ci) {
        maxChainDepth = Carpet_CuOSettings.maxChainDepth;
    }
}