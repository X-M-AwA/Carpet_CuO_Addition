package carpet_cuo.mixins.rules.MoreReasonableRailsMixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
//#if MC >= 12103
import net.minecraft.world.level.ScheduledTickAccess;
//#else
//$$ import net.minecraft.world.level.LevelAccessor;
//#endif
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockMixin extends Block {

    @Shadow
    public abstract Property<RailShape> getShapeProperty();

    @Shadow
    private static boolean shouldBeRemoved(BlockPos pos, Level level, RailShape shape) {
        if (!canSupportRigidBlock(level, pos.below())) {
            return true;
        } else {
            boolean var10000;
            switch (shape) {
                case ASCENDING_EAST -> var10000 = !canSupportRigidBlock(level, pos.east());
                case ASCENDING_WEST -> var10000 = !canSupportRigidBlock(level, pos.west());
                case ASCENDING_NORTH -> var10000 = !canSupportRigidBlock(level, pos.north());
                case ASCENDING_SOUTH -> var10000 = !canSupportRigidBlock(level, pos.south());
                default -> var10000 = false;
            }

            return var10000;
        }
    }

    public BaseRailBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "updateShape",
            at = @At("HEAD")
    )
    //#if MC >= 12103
    private void updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
        if (!levelReader.isClientSide() && levelReader.getBlockState(blockPos).is(this)) {
            RailShape shape = blockState.getValue(this.getShapeProperty());
            if (shouldBeRemoved(blockPos, (Level) levelReader, shape)) {
                dropResources(blockState, (Level) levelReader, blockPos);
                ((Level) levelReader).removeBlock(blockPos, false);
            }
        }
    }
    //#else
    //$$ private void updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2, CallbackInfoReturnable<BlockState> cir) {
    //$$        if (!levelAccessor.isClientSide() && levelAccessor.getBlockState(blockPos).is(this)) {
    //$$            RailShape shape = blockState.getValue(this.getShapeProperty());
    //$$            if (shouldBeRemoved(blockPos, (Level) levelAccessor, shape)) {
    //$$                dropResources(blockState, (Level) levelAccessor, blockPos);
    //$$                levelAccessor.removeBlock(blockPos, false);
    //$$            }
    //$$        }
    //$$    }
    //#endif
}
