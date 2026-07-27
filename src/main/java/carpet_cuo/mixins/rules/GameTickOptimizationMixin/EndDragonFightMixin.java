package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization;
import org.spongepowered.asm.mixin.Mixin;
//#if MC < 260102
import net.minecraft.world.level.dimension.end.EndDragonFight;
//#else
//$$ import net.minecraft.world.level.dimension.end.EnderDragonFight;
//#endif
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC < 260102
@Mixin(EndDragonFight.class)
//#else
//$$ @Mixin(EnderDragonFight.class)
//#endif
public abstract class EndDragonFightMixin {
    //龙战
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (GameTickOptimization.dragonFight) ci.cancel();
    }
}
