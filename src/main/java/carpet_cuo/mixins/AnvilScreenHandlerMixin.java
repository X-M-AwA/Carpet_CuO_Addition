package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public abstract class AnvilScreenHandlerMixin {
    @ModifyExpressionValue(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I",
                    ordinal = 0
            )
    )
    private int MaxEnchantLevel(int original) {
        if (Carpet_CuOSettings.noEnchantmentLevelLimit)return Integer.MAX_VALUE;
        else return original;
    }
    @ModifyConstant(
            method = "createResult",
            constant = @Constant(intValue = 40,ordinal = 2)
    )
    private int setLevelCost(int original){
        if (Carpet_CuOSettings.noTooExpensive)return Integer.MAX_VALUE;
        else return original;
    }
}
