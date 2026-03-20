package carpet_cuo.mixins.MaxUpdateDepthMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(World.class)
public abstract class WorldMixin {
    @ModifyConstant(
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
            constant = @Constant(intValue = 512)
    )
    private int modifyMaxUpdateDepth(int constant){
        return Carpet_CuOSettings.maxUpdateDepth;
    }
}
