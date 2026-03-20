
package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.DyeColor;

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
            ItemStack stack = player.getMainHandStack();
            BlockState state = world.getBlockState(hitResult.getBlockPos());

            if (Carpet_CuOSettings.blockDyeing && stack.getItem() instanceof DyeItem dyeItem && isDyeableBlock(state) && !player.isSpectator() && !world.isClient()){
                Block targetBlock = getDyedBlock(state, dyeItem);
                if (targetBlock != null) {
                    BlockState newState = inheritBlockProperties(state, targetBlock.getDefaultState());
                    world.setBlockState(hitResult.getBlockPos(), newState);
                    if (!player.isCreative()) stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }

    private static boolean isDyeableBlock(BlockState state){
        return TAG_SUFFIX_MAP.keySet().stream().anyMatch(state::isIn)
                || Registries.BLOCK.getId(state.getBlock()).getPath().endsWith("_glazed_terracotta")
                || Registries.BLOCK.getId(state.getBlock()).getPath().endsWith("_concrete")
                || Registries.BLOCK.getId(state.getBlock()).getPath().endsWith("_stained_glass")
                || Registries.BLOCK.getId(state.getBlock()).getPath().endsWith("_stained_glass_pane");
                //#if MC <=12001
                //|| Registries.BLOCK.getId(state.getBlock()).getPath().endsWith("_concrete_powder");
                //#endif
    }

    private static Block getDyedBlock(BlockState state, DyeItem dye){
        DyeColor color = dye.getColor();

        for (Map.Entry<TagKey<Block>, String> entry : TAG_SUFFIX_MAP.entrySet()) {
            if (state.isIn(entry.getKey())) {
                String targetName = color.getName() + entry.getValue();
                Identifier blockId = Identifier.of("minecraft", targetName);
                Block targetBlock = Registries.BLOCK.get(blockId);
                return targetBlock != Blocks.AIR ? targetBlock : null;
            }
        }

        String blockPath = Registries.BLOCK.getId(state.getBlock()).getPath();
        if (blockPath.endsWith("_glazed_terracotta")){
            String targetName = color.getName() + "_glazed_terracotta";
            Identifier blockId = Identifier.of("minecraft", targetName);
            Block targetBlock = Registries.BLOCK.get(blockId);
            return targetBlock != Blocks.AIR ? targetBlock : null;
        }else if (blockPath.endsWith("_concrete")){
            String targetName = color.getName() + "_concrete";
            Identifier blockId = Identifier.of("minecraft", targetName);
            Block targetBlock = Registries.BLOCK.get(blockId);
            return targetBlock != Blocks.AIR ? targetBlock : null;
        }else if (blockPath.endsWith("_stained_glass")){
            String targetName = color.getName() + "_stained_glass";
            Identifier blockId = Identifier.of("minecraft", targetName);
            Block targetBlock = Registries.BLOCK.get(blockId);
            return targetBlock != Blocks.AIR ? targetBlock : null;
        }else if (blockPath.endsWith("_stained_glass_pane")){
            String targetName = color.getName() + "_stained_glass_pane";
            Identifier blockId = Identifier.of("minecraft", targetName);
            Block targetBlock = Registries.BLOCK.get(blockId);
            return targetBlock != Blocks.AIR ? targetBlock : null;
        }
        //#if MC <= 12001
        //$$else if (blockPath.endsWith("_concrete_powder")) {
        //$$    String targetName = color.getName() + "_concrete_powder";
        //$$    Identifier blockId = Identifier.of("minecraft", targetName);
        //$$    Block targetBlock = Registries.BLOCK.get(blockId);
        //$$    return targetBlock != Blocks.AIR ? targetBlock : null;
        //$$}
        //#endif

        return null;
    }

    private static BlockState inheritBlockProperties(BlockState oldState, BlockState newState){
        BlockState resultState = newState;

        for (Property<?> oldProperty : oldState.getProperties()) {
            String propertyName = oldProperty.getName();
            Property<?> newProperty = newState.getBlock().getStateManager().getProperty(propertyName);
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

        if (oldProperty.getType().equals(newProperty.getType())) {
            try {
                Property<T> typedOldProperty = (Property<T>) oldProperty;
                Property<T> typedNewProperty = (Property<T>) newProperty;

                T value = oldState.get(typedOldProperty);
                if (typedNewProperty.getValues().contains(value)) {
                    return newState.with(typedNewProperty, value);
                }
            } catch (ClassCastException e) {
                return newState;
            }
        }
        return newState;
    }
}

