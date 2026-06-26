package carpet_cuo.mixins.rules.PointedDripstoneBlockCanBeRipeningMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SulfurSpikeBlock.class)
public abstract class SulfurSpikeBlockMixin implements BonemealableBlock {
    public boolean isValidBonemealTarget(@NonNull LevelReader levelReader, @NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        if (Carpet_CuOSettings.pointedDripstoneBlockCanBeRipening) {
            Direction direction = blockState.getValue(BlockStateProperties.VERTICAL_DIRECTION);
            BlockPos pos;
            if (direction.equals(Direction.UP)) {
                pos = getTopBlockPos(levelReader, blockPos, blockState.getBlock());
            }else {
                pos = getbottomBlockPos(levelReader, blockPos, blockState.getBlock());
            }
            return levelReader.getBlockState(pos).canBeReplaced();
        }
        return false;
    }

    public boolean isBonemealSuccess(@NonNull Level level, @NonNull RandomSource randomSource, @NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return Carpet_CuOSettings.pointedDripstoneBlockCanBeRipening;
    }

    public void performBonemeal(@NonNull ServerLevel serverLevel, @NonNull RandomSource randomSource, @NonNull BlockPos blockPos, BlockState blockState) {
        if (Carpet_CuOSettings.pointedDripstoneBlockCanBeRipening) {
            Direction direction = blockState.getValue(BlockStateProperties.VERTICAL_DIRECTION);
            BlockPos pos;
            if (direction.equals(Direction.UP)) {
                pos = getTopBlockPos(serverLevel, blockPos, Blocks.SULFUR_SPIKE);
            }else {
                pos = getbottomBlockPos(serverLevel, blockPos, Blocks.SULFUR_SPIKE);
            }
            FluidState fluidState = serverLevel.getFluidState(pos);
            BlockState state = serverLevel.getBlockState(pos);
            if (state.canBeReplaced() && direction.equals(Direction.UP)) {
                serverLevel.setBlock(pos, Blocks.SULFUR_SPIKE.defaultBlockState().setValue(PointedDripstoneBlock.WATERLOGGED, fluidState.isSourceOfType(Fluids.WATER)), 3);
            }else if (state.canBeReplaced() && direction.equals(Direction.DOWN)){
                serverLevel.setBlock(pos, Blocks.SULFUR_SPIKE.defaultBlockState().setValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.DOWN).setValue(PointedDripstoneBlock.WATERLOGGED, fluidState.isSourceOfType(Fluids.WATER)), 3
                );
            }
        }
    }

    @Unique
    private static BlockPos getTopBlockPos(BlockGetter blockGetter, BlockPos blockPos, Block block) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        BlockState blockState;
        do {
            mutableBlockPos.move(Direction.UP);
            blockState = blockGetter.getBlockState(mutableBlockPos);
        } while(blockState.is(block));

        return mutableBlockPos;
    }

    @Unique
    private static BlockPos getbottomBlockPos(BlockGetter blockGetter, BlockPos blockPos, Block block) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        BlockState blockState;
        do {
            mutableBlockPos.move(Direction.DOWN);
            blockState = blockGetter.getBlockState(mutableBlockPos);
        } while(blockState.is(block));

        return mutableBlockPos;
    }
}
