package carpet_cuo.mixins.ComparatorDupeFixMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin {
    @Inject(
            method = "refreshOutputState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            ),
            cancellable = true
    )
    private void ComparatorDupeFix(Level world, BlockPos pos, BlockState state, CallbackInfo ci){
        if (Carpet_CuOSettings.comparatorDupeFix && world.isEmptyBlock(pos) && !world.isClientSide()) ci.cancel();
    }
}