package carpet_cuo.mixins.CopperGolemMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.CopperGolemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperGolemEntity.class)
public abstract class CopperGolemEntityMixin {
    @Inject(
            method = "turnIntoStatue",
            at = @At("HEAD"),
            cancellable = true
    )
    private void copperGolem(CallbackInfo ci){
        CopperGolemEntity copperGolemEntity = (CopperGolemEntity) (Object) this;
        BlockPos blockPos = copperGolemEntity.getBlockPos().down();
        World world = copperGolemEntity.getEntityWorld();
        BlockState blockState = world.getBlockState(blockPos);
        if (Carpet_CuOSettings.copperGolemFix && blockState.isAir())ci.cancel();
    }
}
