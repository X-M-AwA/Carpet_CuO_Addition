package carpet_cuo.mixins.rules.MainThreadChunkLoadingMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ChunkGenerationTask;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.util.thread.BlockableEventLoop;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
    @Shadow
    @Final
    private BlockableEventLoop<Runnable> mainThreadExecutor;

    @Shadow
    protected abstract void runGenerationTask(ChunkGenerationTask chunkGenerationTask);

    @ModifyArg(
            method = "runGenerationTask",
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12103
                    target = "Lnet/minecraft/server/level/ChunkTaskDispatcher;submit(Ljava/lang/Runnable;JLjava/util/function/IntSupplier;)V"
                    //#else
                    //$$ target = "Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter;message(Lnet/minecraft/server/level/GenerationChunkHolder;Ljava/lang/Runnable;)Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter$Message;"
                    //#endif
            )
            //#if MC < 12103
            //$$ ,index = 1
            //#endif
    )
    private Runnable runGenerationTask(Runnable par2, @Local(argsOnly = true) ChunkGenerationTask chunkGenerationTask) {
        if (Carpet_CuOSettings.mainThreadChunkLoading) return () -> {
            CompletableFuture<?> completableFuture = chunkGenerationTask.runUntilWait();
            if (completableFuture != null) {
                completableFuture.thenRunAsync(() -> this.runGenerationTask(chunkGenerationTask), this.mainThreadExecutor);
            }
        };

        return par2;
    }
}
