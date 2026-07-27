package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization;
import net.minecraft.server.level.DistanceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DistanceManager.class)
public class DistanceManagerMixin {
    //加载票移除
    @Inject(
            method = "purgeStaleTickets",
            at = @At("HEAD"),
            cancellable = true
    )
    private void purgeStaleTickets(CallbackInfo ci) {
        if (GameTickOptimization.purgeLoadingTickets) ci.cancel();
    }
}
