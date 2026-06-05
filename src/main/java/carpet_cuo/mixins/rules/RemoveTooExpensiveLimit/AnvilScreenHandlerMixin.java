package carpet_cuo.mixins.rules.RemoveTooExpensiveLimit;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public abstract class AnvilScreenHandlerMixin {
    @ModifyConstant(
            method = "createResult",
            constant = @Constant(intValue = 40,ordinal = 2)
    )
    private int setLevelCost(int original){
        if (Carpet_CuOSettings.removeTooExpensiveLimit)return Integer.MAX_VALUE;
        else return original;
    }
}
