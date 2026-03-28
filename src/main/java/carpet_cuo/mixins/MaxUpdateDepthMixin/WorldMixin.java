package carpet_cuo.mixins.MaxUpdateDepthMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Level.class)
public abstract class WorldMixin {
    @ModifyConstant(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            constant = @Constant(intValue = 512)
    )
    private int modifyMaxUpdateDepth(int constant){
        return Carpet_CuOSettings.maxUpdateDepth;
    }
}
