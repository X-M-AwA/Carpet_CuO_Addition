package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import carpet_cuo.Logging.Logger.UpdateDepthVisualizeLogger;
import carpet_cuo.mixins.spongeCanBeDriedNaturallyMixin.WetSpongeBlockMixin;
import carpet_cuo.rule.EntityHighLight;
import carpet_cuo.rule.WetSponge;
import net.minecraft.server.MinecraftServer;
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
    public abstract MinecraftServer getServer();

    @Shadow
    public abstract ServerLevel getLevel();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void removeTeam(BooleanSupplier booleanSupplier, CallbackInfo ci){
        if (Carpet_CuOSettings.entityHighLight) EntityHighLight.tick((ServerLevel) (Object) this);
        if (CuOAdditionLoggerRegistry.__updateDepth) UpdateDepthVisualizeLogger.tick((ServerLevel) (Object) this);
        if (Carpet_CuOSettings.spongeCanBeDriedNaturally) WetSponge.tick(this.getLevel());
    }
}
