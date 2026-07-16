package carpet_cuo.mixins.rules.HangingEntityDropInstantlyMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockAttachedEntity.class)
public abstract class BlockAttachedEntityMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (Carpet_CuOSettings.hangingEntitiesDropInstantly) {
            BlockAttachedEntity blockAttachedEntity = (BlockAttachedEntity) (Object) this;
            if (blockAttachedEntity instanceof HangingEntity) ci.cancel();
        }
    }
}
