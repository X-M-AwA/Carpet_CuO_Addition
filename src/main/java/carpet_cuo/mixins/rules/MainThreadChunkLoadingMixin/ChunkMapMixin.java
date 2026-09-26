package carpet_cuo.mixins.rules.MainThreadChunkLoadingMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.server.level.ChunkMap;
//#if MC > 12004
import net.minecraft.server.level.ChunkTaskDispatcher;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.function.IntSupplier;
//#else
//$$ import java.util.concurrent.Executor;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#endif
import net.minecraft.util.thread.BlockableEventLoop;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
    @Shadow
    @Final
    private BlockableEventLoop<Runnable> mainThreadExecutor;

    //#if MC > 12004
    @Redirect(
            method = "runGenerationTask",
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12103
                    target = "Lnet/minecraft/server/level/ChunkTaskDispatcher;submit(Ljava/lang/Runnable;JLjava/util/function/IntSupplier;)V"
                    //#elseif MC > 12004
                    //$$ target = "Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter;message(Lnet/minecraft/server/level/GenerationChunkHolder;Ljava/lang/Runnable;)Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter$Message;"
                    //#endif
            )
    )
    private void runGenerationTask(ChunkTaskDispatcher dispatcher, Runnable task, long pos, IntSupplier level) {
        if (Carpet_CuOSettings.mainThreadChunkLoading) {
            this.mainThreadExecutor.execute(task);
        } else {
            dispatcher.submit(task, pos, level);
        }
    }
    //#else
    //$$ @ModifyVariable(
    //$$         method = "scheduleChunkGeneration",
    //$$         at = @At("STORE"),
    //$$         ordinal = 0
    //$$ )
    //$$ private Executor scheduleChunkGeneration(Executor original) {
    //$$     if (Carpet_CuOSettings.mainThreadChunkLoading) {
    //$$         return this.mainThreadExecutor;
    //$$     }
    //$$     return original;
    //$$ }
    //#endif
}
