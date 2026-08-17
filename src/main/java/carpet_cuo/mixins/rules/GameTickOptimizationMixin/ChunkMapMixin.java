package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization.GameTickOptimization;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
    //区块
    @Inject(
            method = "tick()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chunkTick(CallbackInfo ci) {
        if (GameTickOptimization.chunk) ci.cancel();
    }

    //区块卸载
    @Inject(
            method = "tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chunkUnlade(CallbackInfo ci) {
        if (GameTickOptimization.chunkUnlade) ci.cancel();
    }
}
