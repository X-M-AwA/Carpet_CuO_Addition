package carpet_cuo.mixins.rules.PistonBreakingBlockProducesUpdateMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBlockMixin {
    @ModifyConstant(
            method = "moveBlocks",
            constant = @Constant(intValue = 18)
    )
    private static int setFlags(int constant){
        if (Carpet_CuOSettings.pistonBreakingBlockProducesUpdate) return 3;
        return constant;
    }
}
