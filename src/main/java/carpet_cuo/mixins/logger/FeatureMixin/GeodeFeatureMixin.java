package carpet_cuo.mixins.logger.FeatureMixin;

//#if MC < 260300
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
//#else
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.WorldGenLevel;
//$$ import net.minecraft.world.level.chunk.ChunkGenerator;
//#endif
import carpet_cuo.logging.CuOAdditionLoggerRegistry;
import carpet_cuo.logging.Logger.FeatureLogger;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeodeFeature.class)
public abstract class GeodeFeatureMixin {
    @Inject(
            method = "place",
            at = @At("RETURN")
    )
    //#if MC < 260300
    private void onPlace(FeaturePlaceContext<GeodeConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> cir) {
        if (CuOAdditionLoggerRegistry.__feature) FeatureLogger.getInstance().cache(featurePlaceContext.origin(), cir.getReturnValue(), FeatureLogger.FeatureType.AMETHYST_GEODE);
        //#else
        //$$ private void onPlace(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        //$$     if (CuOAdditionLoggerRegistry.__feature) FeatureLogger.getInstance().cache(origin, cir.getReturnValue(), FeatureLogger.FeatureType.AMETHYST_GEODE);
        //#endif
    }
}
