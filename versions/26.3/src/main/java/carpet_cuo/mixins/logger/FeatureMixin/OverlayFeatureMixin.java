package carpet_cuo.mixins.logger.FeatureMixin;

import carpet_cuo.logging.Logger.FeatureLogger;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.OverlayFeature;
import net.minecraft.world.level.levelgen.placement.FeaturePlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OverlayFeature.class)
public class OverlayFeatureMixin {
    @WrapOperation(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/placement/FeaturePlacer;place(Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean onPlaceSubFeature(FeaturePlacer instance, PlacedFeature placedFeature, RandomSource random, BlockPos origin, Operation<Boolean> original, @Local Holder<PlacedFeature> feature) {
        boolean placed = original.call(instance, placedFeature, random, origin);

        if (feature.is(Identifier.withDefaultNamespace("desert_well"))) {
            FeatureLogger.getInstance().cache(origin, placed, FeatureLogger.FeatureType.DESERT_WELL);
        }
        return placed;
    }
}
