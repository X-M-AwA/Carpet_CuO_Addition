package carpet_cuo.mixins.InfinitelyTotemMixin;
//#if MC <=12101
//$$import carpet_cuo.Carpet_CuOSettings;
//$$import net.minecraft.entity.LivingEntity;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.injection.Constant;
//$$import org.spongepowered.asm.mixin.injection.ModifyConstant;
//$$
//$$@Mixin(LivingEntity.class)
//$$public abstract class LivingEntityMixin {
//$$    @ModifyConstant(
//$$            method = "tryUseTotem",
//$$            constant = @Constant(intValue = 1,ordinal = 0)
//$$    )
//$$    private int setAmount(int original){
//$$        if (Carpet_CuOSettings.infinitelyTotem)return 0;
//$$        else return original;
//$$    }
//$$}
//#elseif MC >= 12102
import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyConstant(
            method = "tryUseDeathProtector",
            constant = @Constant(intValue = 1,ordinal = 0)
    )
    private int setAmount(int original){
        if (Carpet_CuOSettings.infinitelyTotem)return 0;
        else return original;
    }
}
//#endif