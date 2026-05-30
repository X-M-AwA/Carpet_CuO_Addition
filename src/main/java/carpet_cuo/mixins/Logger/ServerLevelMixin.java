package carpet_cuo.mixins.Logger;

import carpet_cuo.Logging.Logger.UpdateDepthVisualizeLogger;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin{
    @Shadow
    public abstract ServerLevel getLevel();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void removeTeam(BooleanSupplier booleanSupplier, CallbackInfo ci){
        UpdateDepthVisualizeLogger.tick(this.getLevel());
    }
}
