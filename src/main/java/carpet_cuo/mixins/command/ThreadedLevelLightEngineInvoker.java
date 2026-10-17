package carpet_cuo.mixins.command;

import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ThreadedLevelLightEngine.class)
public interface ThreadedLevelLightEngineInvoker {
    @Invoker("updateChunkStatus")
    void carpet_cuo$updateChunkStatus(ChunkPos chunkPos);
}
