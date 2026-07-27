package carpet_cuo.mixins.rules.UnloadedEntitiesDoNotAffectLoadedChunkMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelChunk.BoundTickingBlockEntity.class)
public class LevelChunkMixin {
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunk;isTicking(Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean setTicking(boolean original) {
        if (Carpet_CuOSettings.unloadedEntitiesDoNotAffectLoadedChunk) return true;
        return original;
    }
}
