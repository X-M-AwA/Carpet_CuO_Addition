package carpet_cuo.mixins.logger.FeatureMixin;

import carpet_cuo.Logging.Logger.FeatureLogger;
import net.minecraft.world.level.levelgen.feature.DesertWellFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DesertWellFeature.class)
public abstract class DesertWellFeatureMixin {
    @Inject(
            method = "place",
            at = @At("RETURN")
    )
    private void onPlace(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> cir) {
        FeatureLogger.getInstance().Cache(featurePlaceContext.origin(), cir.getReturnValue(), FeatureLogger.FeatureType.DESERT_WELL);
    }
}
