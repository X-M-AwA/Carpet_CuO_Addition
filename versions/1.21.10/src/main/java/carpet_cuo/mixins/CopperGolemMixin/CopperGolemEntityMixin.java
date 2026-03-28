package carpet_cuo.mixins.CopperGolemMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.entity.animal.coppergolem.CopperGolem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperGolem.class)
public abstract class CopperGolemEntityMixin {
    @Inject(
            method = "turnToStatue",
            at = @At("HEAD"),
            cancellable = true
    )
    private void copperGolem(CallbackInfo ci){
        CopperGolem copperGolemEntity = (CopperGolem) (Object) this;
        BlockPos blockPos = copperGolemEntity.blockPosition().below();
        Level world = copperGolemEntity.level();
        BlockState blockState = world.getBlockState(blockPos);
        if (Carpet_CuOSettings.copperGolemFix && blockState.isAir())ci.cancel();
    }
}
