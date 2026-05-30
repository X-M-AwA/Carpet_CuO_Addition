package carpet_cuo.mixins.rules.NSEEUpdateSuppressorThresholds;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import org.objectweb.asm.Opcodes;
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
            method = "addAndRun",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater;count:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void setCount(CallbackInfo ci){
        if (Carpet_CuOSettings.NSEEUpdateSuppressorThresholds != -1 && this.count - 1 == Carpet_CuOSettings.NSEEUpdateSuppressorThresholds) {
            this.count = Integer.MAX_VALUE;
        }
    }
}
