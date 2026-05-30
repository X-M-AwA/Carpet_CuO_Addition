package carpet_cuo.mixins.Rules.PlaceableNetherPortalMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class onPlaceMixin {
    @Inject(
            method = "placeBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onPurpleStainedGlass(BlockPlaceContext blockPlaceContext, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(Blocks.PURPLE_STAINED_GLASS) && Carpet_CuOSettings.placeableNetherPortal) {
            Direction direction = blockPlaceContext.getClickedFace();
            Direction.Axis axis = getAxis(direction, blockPlaceContext);
            Level level = blockPlaceContext.getLevel();
            level.setBlock(blockPlaceContext.getClickedPos(), Blocks.NETHER_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, axis), 2, 0);
            cir.cancel();
        }
    }

    @Unique
    private static Direction.Axis getAxis(Direction direction, BlockPlaceContext blockPlaceContext) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            Direction.Axis axis = blockPlaceContext.getHorizontalDirection().getAxis();
            return axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        } else {
            return direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        }
    }
}
