package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooBlock.class)
public abstract class BambooBlockMixin {
    @Shadow
    protected abstract int countBambooAbove(BlockView world, BlockPos pos);

    @Inject(
            method = "grow",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isFertilizable(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci){
        if (Carpet_CuOSettings.bambooBlockCrashFix && !world.isClient()){
            int i = this.countBambooAbove(world, pos);
            BlockPos blockPos = pos.up(i);
            if (blockPos.getY() >= world.getTopYInclusive()) {
                ci.cancel();
            }
        }
    }
}
