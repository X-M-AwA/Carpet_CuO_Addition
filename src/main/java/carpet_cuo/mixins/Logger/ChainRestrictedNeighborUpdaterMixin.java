package carpet_cuo.mixins.Logger;

import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import carpet_cuo.Logging.Logger.UpdateLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CollectingNeighborUpdater.class)
public abstract class ChainRestrictedNeighborUpdaterMixin {
    @Shadow
    private int count;

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
}
