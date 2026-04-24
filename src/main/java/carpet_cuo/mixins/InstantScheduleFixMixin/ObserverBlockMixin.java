package carpet_cuo.mixins.InstantScheduleFixMixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import carpet_cuo.Carpet_CuOSettings;

@Mixin(ObserverBlock.class)
public abstract class ObserverBlockMixin {
    @Shadow
    protected abstract void updateNeighborsInFront(Level level, BlockPos blockPos, BlockState blockState);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"
            )
    )
    private void b(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci){
        if (Carpet_CuOSettings.instantScheduled) {
            this.updateNeighborsInFront(serverLevel, blockPos, blockState);
        }
    }

}
