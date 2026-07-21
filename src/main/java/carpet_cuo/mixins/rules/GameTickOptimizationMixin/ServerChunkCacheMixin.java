package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization;
import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(BooleanSupplier booleanSupplier, boolean bl, CallbackInfo ci) {
        if (GameTickOptimization.chunkTick) ci.cancel();
    }
}
