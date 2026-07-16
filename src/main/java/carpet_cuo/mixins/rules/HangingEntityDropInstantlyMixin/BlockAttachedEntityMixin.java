package carpet_cuo.mixins.rules.HangingEntityDropInstantlyMixin;

import carpet_cuo.Carpet_CuOSettings;
//#if MC > 12006
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
//#else
//$$ import net.minecraft.world.entity.Entity;
//#endif
import net.minecraft.world.entity.decoration.HangingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC > 12006
@Mixin(BlockAttachedEntity.class)
//#else
//$$ @Mixin(Entity.class)
//#endif
public abstract class BlockAttachedEntityMixin {
    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (Carpet_CuOSettings.hangingEntitiesDropInstantly) {
            //#if MC > 12006
            BlockAttachedEntity blockAttachedEntity = (BlockAttachedEntity) (Object) this;
            if (blockAttachedEntity instanceof HangingEntity) ci.cancel();
            //#else
            //$$ Entity entity = (Entity) (Object) this;
            //$$ if (entity instanceof HangingEntity) ci.cancel();
            //#endif
        }
    }
}
