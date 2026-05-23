package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
//#if MC >= 260100
//$$ import net.minecraft.core.component.DataComponents;
//#endif
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionResult;
//#if MC >= 12108
//$$ import net.minecraft.util.ProblemReporter;
//$$ import com.mojang.logging.LogUtils;
//$$ import net.minecraft.world.level.storage.TagValueInput;
//#endif

import java.util.HashMap;
import java.util.Map;

public class BlockDyeing {
    private static final Map<TagKey<Block>, String> TAG_SUFFIX_MAP = new HashMap<>() {{
        put(BlockTags.WOOL, "_wool");
        put(BlockTags.WOOL_CARPETS, "_carpet");
        put(BlockTags.CANDLES, "_candle");
        put(BlockTags.TERRACOTTA, "_terracotta");
        put(BlockTags.SHULKER_BOXES, "_shulker_box");
        //#if MC > 12001
        put(BlockTags.CONCRETE_POWDER, "_concrete_powder");
        //#endif
    }};

    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.isShiftKeyDown()) return InteractionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            ItemStack stack = player.getMainHandItem();
            BlockState state = world.getBlockState(pos);

            if (Carpet_CuOSettings.blockDyeing && stack.getItem() instanceof DyeItem dyeItem && isDyeableBlock(state) && !player.isSpectator()) {
                Block targetBlock = getDyedBlock(state, dyeItem
                        //#if MC >= 260100
                        //$$ , stack
                        //#endif
                );
                if (targetBlock != null) {
                    BlockState newState = inheritBlockProperties(state, targetBlock.defaultBlockState());
                    CompoundTag Nbt = readNbtFromBlockEntity(world, state, pos);

                    world.setBlock(pos, newState, 3);

                    writeNbtFromBlockEntity(world, world.getBlockEntity(pos), Nbt);
                    if (!player.isCreative() && newState != state) stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    private static boolean isDyeableBlock(BlockState state) {
        String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        return TAG_SUFFIX_MAP.keySet().stream().anyMatch(state::is)
                || path.endsWith("_glazed_terracotta")
                || path.endsWith("_concrete")
                || path.endsWith("_stained_glass")
                //#if MC <=12001
                //$$ || path.endsWith("_concrete_powder")
                //#endif
                || path.endsWith("_stained_glass_pane");
    }

    private static Block getDyedBlock(BlockState state, DyeItem dye
                                      //#if MC >= 260100
                                      //$$ , ItemStack itemStack
                                      //#endif
    ) {
        //#if MC < 260100
        DyeColor color = dye.getDyeColor();
        //#else
        //$$ DyeColor color = itemStack.get(DataComponents.DYE);
        //#endif

        for (Map.Entry<TagKey<Block>, String> entry : TAG_SUFFIX_MAP.entrySet()) {
            if (state.is(entry.getKey())) return setBlock(color, entry.getValue());
        }

        String blockPath = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        return setBlock(color, blockPath);
    }

    private static Block setBlock(DyeColor color, String blockNames) {
        String targetName = color.getName() + blockName(blockNames);
        ResourceLocation blockId = ResourceLocation.tryBuild("minecraft", targetName);
        //#if MC >= 12103
        Block targetBlock = BuiltInRegistries.BLOCK.getValue(blockId);
        //#else
        //$$ Block targetBlock = BuiltInRegistries.BLOCK.get(blockId);
        //#endif
        return targetBlock != Blocks.AIR ? targetBlock : null;
    }

    private static String blockName(String string) {
        int index = string.indexOf('_');
        return string.substring(index);
    }

    private static BlockState inheritBlockProperties(BlockState oldState, BlockState newState) {
        BlockState resultState = newState;

        for (Property<?> oldProperty : oldState.getProperties()) {
            String propertyName = oldProperty.getName();
            Property<?> newProperty = newState.getBlock().getStateDefinition().getProperty(propertyName);
            if (newProperty != null) {
                resultState = copyPropertyValue(oldState, resultState, oldProperty, newProperty);
            }
        }
        return resultState;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState copyPropertyValue(BlockState oldState, BlockState newState, Property<?> oldProperty, Property<?> newProperty) {
        if (oldProperty.getValueClass().equals(newProperty.getValueClass())) {
            try {
                Property<T> typedOldProperty = (Property<T>) oldProperty;
                Property<T> typedNewProperty = (Property<T>) newProperty;

                T value = oldState.getValue(typedOldProperty);
                if (typedNewProperty.getPossibleValues().contains(value)) {
                    return newState.setValue(typedNewProperty, value);
                }
            } catch (ClassCastException e) {
                return newState;
            }
        }
        return newState;
    }

    private static CompoundTag readNbtFromBlockEntity(Level level, BlockState state, BlockPos blockPos) {
        CompoundTag nbt;

        if (state.is(BlockTags.SHULKER_BOXES)) {
            BlockEntity oldBlock = level.getBlockEntity(blockPos);
            if (oldBlock != null) {
                //#if MC > 12004
                nbt = oldBlock.saveWithoutMetadata(level.registryAccess());
                //#else
                //$$ nbt = oldBlock.saveWithoutMetadata();
                //#endif
                return nbt;
            }
        }
        return null;
    }

    private static void writeNbtFromBlockEntity(Level level, BlockEntity newBlock, CompoundTag nbt) {
        if (newBlock != null && nbt != null) {
            //#if MC <= 12004
            //$$ newBlock.load(nbt);
            //#elseif MC > 12004 && MC <= 12105
            newBlock.loadWithComponents(nbt, level.registryAccess());
            //#elseif MC > 12105
            //$$ try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(newBlock.problemPath(), LogUtils.getLogger())) {
            //$$    newBlock.loadWithComponents(TagValueInput.create(reporter, level.registryAccess(), nbt));
            //$$ }
            //#endif
        }
    }
}