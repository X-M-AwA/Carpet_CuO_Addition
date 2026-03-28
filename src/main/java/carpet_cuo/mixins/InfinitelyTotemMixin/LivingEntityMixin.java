package carpet_cuo.mixins.InfinitelyTotemMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyConstant(
            method = "checkTotemDeathProtection",
            constant = @Constant(intValue = 1,ordinal = 0)
    )
    private int setAmount(int original){
        if (Carpet_CuOSettings.infinitelyTotem)return 0;
        else return original;
    }
}