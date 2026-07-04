package carpet_cuo.mixins.rules.UnrestrictedPlacingBlockMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(
            method = "canPlace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void canPlace(BlockPlaceContext blockPlaceContext, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (Carpet_CuOSettings.unrestrictedPlacingBlock) cir.setReturnValue(true);
    }
}
