package carpet_cuo.mixins.logger.FeatureMixin;

import carpet_cuo.Logging.CuOAdditionLoggerRegistry;
import carpet_cuo.Logging.Logger.FeatureLogger;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Inject(
            method = "applyBiomeDecoration",
            at = @At("RETURN")
    )
    private void applyBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, StructureManager structureManager, CallbackInfo ci) {
        if (CuOAdditionLoggerRegistry.__feature) FeatureLogger.getInstance().print();
    }
}
