package carpet_cuo.mixins;

import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(LevelChunkTicks.class)
public class a<T> {
    @Shadow
    @Final
    private Queue<ScheduledTick<T>> tickQueue;

    @Inject(
            method = "scheduleUnchecked",
            at = @At("HEAD")
    )
    private void boom(ScheduledTick<T> scheduledTick, CallbackInfo ci){
        if (tickQueue.size() >= 20){
            throw new IllegalStateException("Queue full");
        }
    }
}
