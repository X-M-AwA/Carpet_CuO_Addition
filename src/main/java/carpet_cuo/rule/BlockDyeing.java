package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
//#if MC >= 260100
//$$ import net.minecraft.core.component.DataComponents;
//#endif
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
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

import java.util.HashMap;
import java.util.Map;

public class BlockDyeing {
    private static final Map<TagKey<Block>, String> TAG_SUFFIX_MAP = new HashMap<>(){{
        put(BlockTags.WOOL, "_wool");
        put(BlockTags.WOOL_CARPETS, "_carpet");
        put(BlockTags.CANDLES, "_candle");
        put(BlockTags.TERRACOTTA, "_terracotta");
        //#if MC > 12001
        put(BlockTags.CONCRETE_POWDER, "_concrete_powder");
        //#endif
    }};

    public static void init(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.isShiftKeyDown()) return InteractionResult.PASS;

            ItemStack stack = player.getMainHandItem();
            BlockState state = world.getBlockState(hitResult.getBlockPos());

            if (Carpet_CuOSettings.blockDyeing && stack.getItem() instanceof DyeItem dyeItem && isDyeableBlock(state) && !player.isSpectator()){
                Block targetBlock = getDyedBlock(state, dyeItem
                        //#if MC >= 260100
                        //$$ , stack
                        //#endif
                );
                if (targetBlock != null) {
                    BlockState newState = inheritBlockProperties(state, targetBlock.defaultBlockState());
                    world.setBlock(hitResult.getBlockPos(), newState, 3);
                    if (!player.isCreative() && newState != state) stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    private static boolean isDyeableBlock(BlockState state){
        return TAG_SUFFIX_MAP.keySet().stream().anyMatch(state::is)
                || BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().endsWith("_glazed_terracotta")
                || BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().endsWith("_concrete")
                || BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().endsWith("_stained_glass")
                //#if MC <=12001
                //|| BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().endsWith("_concrete_powder")
                //#endif
                || BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().endsWith("_stained_glass_pane");
    }

    private static Block getDyedBlock(BlockState state, DyeItem dye
                                      //#if MC >= 260100
                                      //$$ , ItemStack itemStack
                                      //#endif
    ){
        //#if MC < 260100
        DyeColor color = dye.getDyeColor();
        //#else
        //$$ DyeColor color = (DyeColor)itemStack.get(DataComponents.DYE);
        //#endif

        for (Map.Entry<TagKey<Block>, String> entry : TAG_SUFFIX_MAP.entrySet()) {
            if (state.is(entry.getKey())) return setBlock(color, entry.getValue());
        }

        String blockPath = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        if (isDyeableBlock(state)) return setBlock(color, blockPath);
        return null;
    }

    private static Block setBlock(DyeColor color, String blockNames){
        String blockName = blockName(blockNames);
        String targetName = color.getName() + blockName;
        ResourceLocation blockId = ResourceLocation.tryBuild("minecraft", targetName);
        //#if MC >= 12103
        Block targetBlock = BuiltInRegistries.BLOCK.getValue(blockId);
        //#else
        //$$ Block targetBlock = BuiltInRegistries.BLOCK.get(blockId);
        //#endif
        return targetBlock != Blocks.AIR ? targetBlock : null;
    }

    private static String blockName(String string){
        int index = string.indexOf('_');
        return string.substring(index);
    }

    private static BlockState inheritBlockProperties(BlockState oldState, BlockState newState){
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
    private static <T extends Comparable<T>> BlockState copyPropertyValue(
            BlockState oldState, BlockState newState,
            Property<?> oldProperty, Property<?> newProperty) {

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
}
