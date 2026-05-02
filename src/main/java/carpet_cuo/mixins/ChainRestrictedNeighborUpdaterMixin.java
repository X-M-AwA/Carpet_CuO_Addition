package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import carpet_cuo.Logging.Logger.UpdateLogger;
import net.minecraft.core.BlockPos;
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

    @Shadow
    private int count;

    @Inject(
            method = "addAndRun",
            at = @At("HEAD")
    )
    private void setMaxChainDepth(CallbackInfo ci){
        if (this.maxChainedNeighborUpdates != Carpet_CuOSettings.maxChainDepth){
            this.maxChainedNeighborUpdates = Carpet_CuOSettings.maxChainDepth;
        }
    }

    @Inject(
        method = "addAndRun",
        at = @At(
            value = "INVOKE",
            //#if MC < 12110
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;)V",
            //#else
            //$$ target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
            //#endif
            ordinal = 0
        )
    )
    private void Update(BlockPos blockPos, CollectingNeighborUpdater.NeighborUpdates neighborUpdates, CallbackInfo ci){
        if (CuOAdditionLoggerRegistry.__update) {
            UpdateLogger.getInstance().onUpdateSkipping(this.count, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
    }

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

    @Inject(
            method = "addAndRun",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater;count:I"
            )
    )
    private void setCount(CallbackInfo ci){
        if (Carpet_CuOSettings.NSEEUpdateSuppressorThresholds != -1 && this.count - 1 == Carpet_CuOSettings.NSEEUpdateSuppressorThresholds) this.count = Integer.MAX_VALUE;
    }
}
