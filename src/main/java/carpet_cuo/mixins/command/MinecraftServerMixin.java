package carpet_cuo.mixins.command;

import carpet_cuo.command.ChunkCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(
            method = "stopServer",
            at = @At(
                    value = "INVOKE",
                   target = "Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;close()V"
            )
    )
    private void onStop(CallbackInfo ci) {
        for (ServerLevel level : ChunkCommand.levels) {
            for (ChunkPos chunkPos : ChunkCommand.chunks) {
                ChunkCommand.removeChunk(level, chunkPos);
            }
        }
        ChunkCommand.chunks.clear();
    }
}
