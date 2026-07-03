package carpet_cuo.mixins.command;

import carpet_cuo.command.RemoveChunk;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
            method = "stopServer",
            at = @At(
                    value = "INVOKE",
                   target = "Lnet/minecraft/server/level/ServerLevel;close()V"
            )
    )
    private void onStop(CallbackInfo ci) {
        RemoveChunk.chunks.clear();
    }
}
