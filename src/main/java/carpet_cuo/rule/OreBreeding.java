package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.NeighborUpdater;

public class OreBreeding {
    public static void init() {
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) -> {
            if (player.isShiftKeyDown() || !Carpet_CuOSettings.oreBreeding) return InteractionResult.PASS;

            ItemStack stack = player.getMainHandItem();
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (blockTags(level.getBlockState(blockPos)) && stack.is(Items.BONE_MEAL)) {
                if (!player.isCreative() & breeding(level, blockPos)) stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    private static boolean blockTags(BlockState blockState) {
        //#if MC < 260200
        return blockState.is(BlockTags.COAL_ORES) ||
                blockState.is(BlockTags.COPPER_ORES) ||
                blockState.is(BlockTags.IRON_ORES) ||
                blockState.is(BlockTags.DIAMOND_ORES) ||
                blockState.is(BlockTags.EMERALD_ORES) ||
                blockState.is(BlockTags.GOLD_ORES) ||
                blockState.is(BlockTags.LAPIS_ORES) ||
                blockState.is(BlockTags.REDSTONE_ORES);
        //#else
        //$$ String path = BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).getPath();
        //$$        return path.endsWith("_ore");
        //#endif
    }

    private static boolean breeding(Level level, BlockPos blockPos) {
        boolean bl = false;
        for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
            BlockState blockState = level.getBlockState(blockPos);
            BlockPos pos = blockPos.relative(direction);
            BlockState blockState1 = level.getBlockState(pos);
            if (blockState1.is(Blocks.STONE) || blockState1.is(Blocks.DEEPSLATE)) {
                bl = level.setBlock(pos, state(blockState.getBlock(), blockState1), 3);
            }
        }
        return bl;
    }

    private static BlockState state(Block block, BlockState blockState) {
        String ore = BuiltInRegistries.BLOCK.getKey(block).getPath().replace("deepslate_", "");
        ResourceLocation blockId = null;
        if (blockState.is(Blocks.STONE)) {
            blockId = ResourceLocation.tryParse("minecraft:" + ore);
        } else if (blockState.is(Blocks.DEEPSLATE)) {
            blockId = ResourceLocation.tryParse("minecraft:deepslate_" + ore);
        }
        //#if MC >= 12103
        Block targetBlock = BuiltInRegistries.BLOCK.getValue(blockId);
        //#else
        //$$ Block targetBlock = BuiltInRegistries.BLOCK.get(blockId);
        //#endif
        return targetBlock.defaultBlockState();
    }
}
