package carpet_cuo.mixins.Rules.CopperGolemMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.entity.animal.coppergolem.CopperGolem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

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
        BlockState blockState;
        try (Level world = copperGolemEntity.level()) {
            blockState = world.getBlockState(blockPos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Carpet_CuOSettings.copperGolemFix && blockState.isAir())ci.cancel();
    }
}
