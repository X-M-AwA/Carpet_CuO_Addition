package carpet_cuo.mixins.rules.LightSuppressionCanAlsoLoadClocksMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Mixin(ThreadedLevelLightEngine.class)
public abstract class ThreadedLevelLightEngineMixin {
    @Shadow
    public abstract void setLightEnabled(ChunkPos pos, boolean enable);

    @Shadow
    public abstract void retainData(ChunkPos pos, boolean retain);

    @ModifyReturnValue(
            method = "initializeLight",
            at = @At("RETURN")
    )
    private CompletableFuture<ChunkAccess> skipInitializeLight(CompletableFuture<ChunkAccess> original, @Local(argsOnly = true) ChunkAccess chunkAccess, @Local(argsOnly = true) boolean bl) {
        if (Carpet_CuOSettings.lightSuppressionCanAlsoLoadClocks) {
            return original.orTimeout(Carpet_CuOSettings.lightingUpdateSkipsThresholds, TimeUnit.MILLISECONDS).exceptionally(throwable -> {
                this.setLightEnabled(chunkAccess.getPos(), bl);
                this.retainData(chunkAccess.getPos(), false);
                return chunkAccess;
            });
        }
        return original;
    }

    @ModifyReturnValue(
            method = "lightChunk",
            at = @At("RETURN")
    )
    private CompletableFuture<ChunkAccess> skipLightChunk(CompletableFuture<ChunkAccess> original, @Local(argsOnly = true) ChunkAccess chunkAccess) {
        if (Carpet_CuOSettings.lightSuppressionCanAlsoLoadClocks) {
            return original.orTimeout(Carpet_CuOSettings.lightingUpdateSkipsThresholds, TimeUnit.MILLISECONDS).exceptionally(throwable -> chunkAccess);
        }
        return original;
    }
}
