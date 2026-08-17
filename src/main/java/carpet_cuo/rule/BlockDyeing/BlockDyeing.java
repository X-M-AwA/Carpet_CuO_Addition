package carpet_cuo.rule.BlockDyeing;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.utils.NbtManager;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
//#if MC >= 260100
//$$ import net.minecraft.core.component.DataComponents;
//#endif
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionResult;
//#if MC >= 12108
//$$ import net.minecraft.util.ProblemReporter;
//$$ import com.mojang.logging.LogUtils;
//$$ import net.minecraft.world.level.storage.TagValueInput;
//#endif


public class BlockDyeing {
    public static void init() {
        UseBlockCallback.EVENT.register((player, world, interactionHand, hitResult) -> {
            if (!Carpet_CuOSettings.blockDyeing || player.isShiftKeyDown()) return InteractionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            ItemStack stack = player.getMainHandItem();
            BlockState state = world.getBlockState(pos);

            if (stack.getItem() instanceof DyeItem dyeItem && !player.isSpectator()) {
                Block targetBlock = getDyedBlock(state, dyeItem
                        //#if MC >= 260100
                        //$$ , stack
                        //#endif
                );
                if (!targetBlock.defaultBlockState().is(Blocks.AIR)) {
                    BlockState newState = inheritBlockProperties(state, targetBlock.defaultBlockState());
                    CompoundTag Nbt = NbtManager.readNbtFromBlockEntity(world, state, pos);

                    world.setBlock(pos, newState, 3);

                    NbtManager.writeNbtToBlockEntity(world, world.getBlockEntity(pos), Nbt);
                    if (!player.isCreative() && newState != state) stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
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

        String blockPath = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        String targetName = color.getName() + getBlockName(blockPath);
        ResourceLocation blockId = ResourceLocation.tryBuild("minecraft", targetName);
        //#if MC >= 12103
        return BuiltInRegistries.BLOCK.getValue(blockId);
        //#else
        //$$ return BuiltInRegistries.BLOCK.get(blockId);
        //#endif
    }

    private static String getBlockName(String string) {
        int index = string.indexOf('_');
        return index == -1 ? "_" + string : string.substring(index);
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
}