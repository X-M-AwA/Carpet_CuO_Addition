package carpet_cuo.mixins.rules.RemoveThreadChecksMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow
    @Final
    private Thread thread;

    @Redirect(
            method = "getBlockEntity",
            at = @At(value = "INVOKE",
                    target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"
            )
    )
    private Thread redirectCurrentThread() {
        if (Carpet_CuOSettings.removedThreadChecks) return this.thread;
        else return Thread.currentThread();
    }
}
