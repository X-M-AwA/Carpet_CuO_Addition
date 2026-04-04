package carpet_cuo.mixins.BambooBlockCrashFixMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(BambooStalkBlock.class)
public abstract class BambooBlockMixin {
    @Shadow
    protected abstract int getHeightAboveUpToMax(BlockGetter world, BlockPos pos);

    @Inject(
            method = "performBonemeal",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isFertilizable(ServerLevel world, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci){
        if (Carpet_CuOSettings.bambooBlockCrashFix && !world.isClientSide()){
            int i = this.getHeightAboveUpToMax(world, pos);
            BlockPos blockPos = pos.above(i);
            if (blockPos.getY() >= world.getMaxY()) {
                ci.cancel();
            }
        }
    }
}
