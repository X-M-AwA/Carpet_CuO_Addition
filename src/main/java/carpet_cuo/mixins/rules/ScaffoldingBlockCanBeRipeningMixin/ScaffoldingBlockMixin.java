package carpet_cuo.mixins.rules.ScaffoldingBlockCanBeRipeningMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ScaffoldingBlock.class)
public abstract class ScaffoldingBlockMixin implements BonemealableBlock {
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if (Carpet_CuOSettings.scaffoldingBlockCanBeRipening) {
            BlockPos pos = getBlockPos(levelReader, blockPos, blockState.getBlock());
            return levelReader.getBlockState(pos).canBeReplaced();
        }
        return false;
    }

    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return Carpet_CuOSettings.scaffoldingBlockCanBeRipening;
    }

    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (Carpet_CuOSettings.scaffoldingBlockCanBeRipening) {
            BlockPos pos = getBlockPos(serverLevel, blockPos, Blocks.SCAFFOLDING);
            FluidState fluidState = serverLevel.getFluidState(pos);
            BlockState state = serverLevel.getBlockState(pos);
            if (state.canBeReplaced()) {
                serverLevel.setBlock(pos, Blocks.SCAFFOLDING.defaultBlockState().setValue(ScaffoldingBlock.WATERLOGGED, fluidState.isSourceOfType(Fluids.WATER)), 3);
            }
        }
    }

    @Unique
    private static BlockPos getBlockPos(BlockGetter blockGetter, BlockPos blockPos, Block block) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        BlockState blockState;
        do {
            mutableBlockPos.move(Direction.UP);
            blockState = blockGetter.getBlockState(mutableBlockPos);
        } while(blockState.is(block));

        return mutableBlockPos;
    }
}
