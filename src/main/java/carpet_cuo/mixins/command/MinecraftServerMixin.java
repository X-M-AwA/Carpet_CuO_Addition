package carpet_cuo.mixins.command;

import carpet_cuo.command.deletedChunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import static carpet_cuo.Carpet_CuOServer.LOGGER;

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
        for (ServerLevel level : deletedChunk.levels) {
            for (ChunkPos chunkPos : deletedChunk.chunks) {
                removeChunk(level, chunkPos);
            }
        }
        deletedChunk.chunks.clear();
    }

    @Unique
    private static void removeChunk(ServerLevel level, ChunkPos chunkPos) {
        int regionX = chunkPos.getRegionX();
        int regionZ = chunkPos.getRegionZ();
        int localX = chunkPos.getRegionLocalX();
        int localZ = chunkPos.getRegionLocalZ();
        int index = localX + localZ * 32;

        Path worldRoot = level.getServer().getWorldPath(LevelResource.ROOT);
        Path dimensionDir = DimensionType.getStorageFolder(level.dimension(), worldRoot);
        Path regionDir = dimensionDir.resolve("region");
        Path regionPath = regionDir.resolve(String.format("r.%d.%d.mca", regionX, regionZ));

        if (Files.exists(regionPath)) {
            try (RandomAccessFile raf = new RandomAccessFile(regionPath.toFile(), "rw")) {
                raf.seek((long) index * 4);
                raf.writeInt(0);

                raf.seek(4096L + (long) index * 4);
                raf.writeInt(0);
            } catch (IOException e) {
                LOGGER.error("Failed to remove chunk {}: ", chunkPos, e);
            }
        }
    }
}
