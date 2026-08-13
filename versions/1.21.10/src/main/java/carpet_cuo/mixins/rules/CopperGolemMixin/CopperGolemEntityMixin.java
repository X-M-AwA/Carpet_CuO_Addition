package carpet_cuo.mixins.rules.CopperGolemMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.coppergolem.CopperGolem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperGolem.class)
public abstract class CopperGolemEntityMixin {
    @Inject(
            method = "turnToStatue",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            ),
            cancellable = true
    )
    private void copperGolem(CallbackInfo ci, @Local BlockPos blockPos, @Local(argsOnly = true) ServerLevel serverLevel){
        BlockPos pos = blockPos.below();
        BlockState blockState = serverLevel.getBlockState(pos);
        if (Carpet_CuOSettings.copperGolemFix && blockState.isAir()) ci.cancel();
    }
}
