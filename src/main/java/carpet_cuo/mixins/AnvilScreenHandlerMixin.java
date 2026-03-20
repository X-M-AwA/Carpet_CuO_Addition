package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {
    @ModifyExpressionValue(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I",
                    ordinal = 0
            )
    )
    private int MaxEnchantLevel(int original) {
        if (Carpet_CuOSettings.noEnchantmentLevelLimit)return Integer.MAX_VALUE;
        else return original;
    }
    @ModifyConstant(
            method = "updateResult",
            constant = @Constant(intValue = 40,ordinal = 2)
    )
    private int setLevelCost(int original){
        if (Carpet_CuOSettings.noTooExpensive)return Integer.MAX_VALUE;
        else return original;
    }
}
