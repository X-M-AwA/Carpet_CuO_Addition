package carpet_cuo.mixins.rules.ChunkPopulationCanAffectLoadedChunksMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin {
    @ModifyExpressionValue(
            method = "setBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/WorldGenRegion;ensureCanWrite(Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean setBlock(boolean original) {
        if (Carpet_CuOSettings.chunkPopulationCanAffectLoadedChunks) return true;
        else return original;
    }
}
