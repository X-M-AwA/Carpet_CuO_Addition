package carpet_cuo.mixins.logger.FeatureMixin;

import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import carpet_cuo.Logging.Logger.FeatureLogger;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeodeFeature.class)
public class GeodeFeatureMixin {
    @Inject(
            method = "place",
            at = @At("RETURN")
    )
    private void onPlace(FeaturePlaceContext<GeodeConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> cir) {
        if (CuOAdditionLoggerRegistry.__feature) FeatureLogger.getInstance().Cache(featurePlaceContext.origin(), cir.getReturnValue(), FeatureLogger.FeatureType.AMETHYST_GEODE);
    }
}
