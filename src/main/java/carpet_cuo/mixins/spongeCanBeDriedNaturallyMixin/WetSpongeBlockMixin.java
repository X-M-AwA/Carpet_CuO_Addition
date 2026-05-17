package carpet_cuo.mixins.spongeCanBeDriedNaturallyMixin;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.rule.WetSponge;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WetSpongeBlock.class)
public abstract class WetSpongeBlockMixin {
    @Inject(
            method = "onPlace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void SpongesEvaporate(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        if (Carpet_CuOSettings.spongeCanBeDriedNaturally && level instanceof ServerLevel) {
            WetSponge.Evaporate(level, blockPos);
            ci.cancel();
        }
    }
}
