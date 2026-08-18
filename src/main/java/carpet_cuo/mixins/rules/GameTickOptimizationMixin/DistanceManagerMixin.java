package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization.GameTickOptimization;
import net.minecraft.server.level.DistanceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC > 12104
//$$ import net.minecraft.server.level.ChunkMap;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(DistanceManager.class)
public abstract class DistanceManagerMixin {
    //加载票移除
    //#if MC <= 12104
    @Inject(
            method = "purgeStaleTickets",
            at = @At("HEAD"),
            cancellable = true
    )
    private void purgeStaleTickets(CallbackInfo ci) {
        if (GameTickOptimization.purgeLoadingTickets) ci.cancel();
    }
    //#else
    //$$ @Inject(
    //$$            method = "runAllUpdates",
    //$$            at = @At("HEAD"),
    //$$            cancellable = true
    //$$    )
    //$$    private void purgeStaleTickets(ChunkMap scheduler, CallbackInfoReturnable<Boolean> cir) {
    //$$        if (GameTickOptimization.purgeLoadingTickets) cir.cancel();
    //$$    }
    //#endif
}
