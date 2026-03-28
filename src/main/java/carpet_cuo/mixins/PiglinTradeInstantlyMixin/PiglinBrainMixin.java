package carpet_cuo.mixins.PiglinTradeInstantlyMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PiglinAi.class)
public abstract class PiglinBrainMixin {
    @ModifyConstant(
            method = "admireGoldItem",
            constant = @Constant(longValue = 119L)
    )
    private static long setExpiry(long original){
        if (Carpet_CuOSettings.piglinTradeInstantly) return -1L;
        else return original;
    }
}
