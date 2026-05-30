package carpet_cuo.mixins.Rules.NoEnchantmentLevelLimitMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
}
