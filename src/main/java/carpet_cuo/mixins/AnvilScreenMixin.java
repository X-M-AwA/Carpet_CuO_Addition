package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin {
    @ModifyConstant(
            method = "drawForeground",
            constant = @Constant(intValue = 40)
    )
    private int clientRendering(int original){
        if (Carpet_CuOSettings.noTooExpensive) return Integer.MAX_VALUE;
        else return original;
    }
}
