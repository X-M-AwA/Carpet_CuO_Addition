package carpet_cuo.mixins.PiglinTradeInstantlyMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.entity.mob.PiglinBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @ModifyConstant(
            method = "setAdmiringItem",
            constant = @Constant(longValue = 119L)
    )
    private static long setExpiry(long original){
        if (Carpet_CuOSettings.piglinTradeInstantly) return -1L;
        else return original;
    }
}
