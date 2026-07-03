package carpet_cuo.mixins.command;

import carpet_cuo.command.RemoveChunk;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
    @Inject(
            method = "save",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSave(ChunkAccess chunkAccess, CallbackInfoReturnable<Boolean> cir) {
        for (ChunkPos chunkPos : RemoveChunk.chunks) {
            if (chunkAccess.getPos().equals(chunkPos)) {
                cir.cancel();
            }
        }
    }
}
