package carpet_cuo.mixins.rules.ChainUpdateLoggerThresholds;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.logging.CuOAdditionLoggerRegistry;
import carpet_cuo.logging.Logger.UpdateLogger;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CollectingNeighborUpdater.class)
public abstract class ChainRestrictedNeighborUpdaterMixin {
    @Shadow
    private int count;

    @Inject(
        method = "runUpdates",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/ArrayDeque;clear()V"
        )
    )
    private void runUpdates(CallbackInfo ci){
        if (CuOAdditionLoggerRegistry.__update && this.count - 1 != 0 && this.count - 1 >= Carpet_CuOSettings.chainUpdateLoggerThresholds) {
            UpdateLogger.getInstance().onDepthReached(this.count);
        }
    }
}
