package carpet_cuo.mixins.rules.TripwireScheduleTickFixMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TripWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TripWireBlock.class)
public class TripWireBlockMixin {
    @Inject(
            method = "checkPressed(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/util/List;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V",
                    ordinal = 1
            ),
            cancellable = true
    )
    private void checkPressed(Level level, BlockPos pos, List<? extends Entity> entities, CallbackInfo ci) {
        if (Carpet_CuOSettings.tripwireScheduleTickFix) ci.cancel();
    }
}
