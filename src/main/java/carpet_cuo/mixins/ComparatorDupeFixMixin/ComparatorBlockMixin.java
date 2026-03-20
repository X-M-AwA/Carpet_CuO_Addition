package carpet_cuo.mixins.ComparatorDupeFixMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin {
    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
                    ordinal = 1
            ),
            cancellable = true
    )
    private void ComparatorDupeFix(World world, BlockPos pos, BlockState state, CallbackInfo ci){
        if (Carpet_CuOSettings.comparatorDupeFix && world.isAir(pos))ci.cancel();
    }
}