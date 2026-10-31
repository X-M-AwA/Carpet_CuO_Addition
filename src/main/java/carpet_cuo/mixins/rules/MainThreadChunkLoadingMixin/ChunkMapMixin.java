package carpet_cuo.mixins.rules.MainThreadChunkLoadingMixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.util.thread.BlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//#if MC >= 12103
import carpet_cuo.rule.MainThreadChunkLoading.SwitchableExecutor;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.concurrent.Executor;
//#elseif MC >= 12100
/*$$
import net.minecraft.server.level.ChunkTaskPriorityQueueSorter;
import net.minecraft.server.level.GenerationChunkHolder;
import org.spongepowered.asm.mixin.injection.Redirect;
$$*/
//#else
/*$$
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.concurrent.Executor;
$$*/
//#endif

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
    //#if MC >= 12103
    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/thread/ConsecutiveExecutor;<init>(Ljava/util/concurrent/Executor;Ljava/lang/String;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private Executor worldgenExecutor(Executor executor, @Local(argsOnly = true) BlockableEventLoop<Runnable> mainThreadExecutor) {
        return new SwitchableExecutor(executor, mainThreadExecutor);
    }
    //#elseif MC >= 12100
    /*$$
    @Redirect(
            method = "runGenerationTask",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter;message(Lnet/minecraft/server/level/GenerationChunkHolder;Ljava/lang/Runnable;)Lnet/minecraft/server/level/ChunkTaskPriorityQueueSorter$Message;"
            )
    )
    private ChunkTaskPriorityQueueSorter.Message<Runnable> runGenerationTask(GenerationChunkHolder center, Runnable task) {
        if (Carpet_CuOSettings.mainThreadChunkLoading) {
            BlockableEventLoop<Runnable> mainThread = this.mainThreadExecutor;
            return ChunkTaskPriorityQueueSorter.message(center, () -> mainThread.execute(task));
        }
        return ChunkTaskPriorityQueueSorter.message(center, task);
    }
    $$*/
    //#else
    /*$$
    @ModifyVariable(
            method = "scheduleChunkGeneration",
            at = @At("STORE"),
            ordinal = 0
    )
    private Executor scheduleChunkGeneration(Executor original) {
        if (Carpet_CuOSettings.mainThreadChunkLoading) {
            return this.mainThreadExecutor;
        }
        return original;
    }
    $$*/
    //#endif
}
