package carpet_cuo.mixins.rules.RemovePalettedContainerLock;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PalettedContainer.class)
public class PalettedContainerMixin {
    @Inject(
            method = "acquire",
            at = @At("HEAD"),
            cancellable = true
    )
    private void remove(CallbackInfo ci) {
        if (Carpet_CuOSettings.removePalettedContainerLock) ci.cancel();
    }

    @Inject(
            method = "release",
            at = @At("HEAD"),
            cancellable = true
    )
    private void Remove(CallbackInfo ci) {
        if (Carpet_CuOSettings.removePalettedContainerLock) ci.cancel();
    }
}
