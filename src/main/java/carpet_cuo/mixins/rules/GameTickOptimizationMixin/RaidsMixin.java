package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raids.class)
public abstract class RaidsMixin {
    //袭击
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (GameTickOptimization.raids) ci.cancel();
    }
}
