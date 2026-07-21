package carpet_cuo.mixins.rules.GameTickOptimizationMixin;

import carpet_cuo.rule.GameTickOptimization;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin {
    //方块实体
    @Inject(
            method = "tickBlockEntities",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (GameTickOptimization.blockEntity) ci.cancel();
    }
}
