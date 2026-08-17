package carpet_cuo.rule.RustingCopperManually;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
//#if MC >= 12006
import net.minecraft.core.component.DataComponents;
//#else
//$$ import net.minecraft.world.item.alchemy.Potion;
//$$ import net.minecraft.world.item.alchemy.PotionUtils;
//#endif
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class RustingCopperManually {
    public static void init() {
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) -> {
            if (!Carpet_CuOSettings.rustingCopperManually || !player.isShiftKeyDown()) return InteractionResult.PASS;

            ItemStack itemStack = player.getMainHandItem();
            //#if MC >= 12006
            PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
            //#else
            //$$ Potion contents = PotionUtils.getPotion(itemStack);
            //#endif
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);

            //#if MC >= 12006
            if (contents != null && contents.is(Potions.WATER) && blockState.getBlock() instanceof WeatheringCopper copper) {
            //#else
            //$$ if (contents == Potions.WATER && blockState.getBlock() instanceof WeatheringCopper copper) {
            //#endif
                copper.getNext(blockState).ifPresent(state -> {
                    if (level.setBlock(blockPos, state, 3)) {
                        ItemStack emptyBottle = useItem(itemStack, player);
                        player.setItemInHand(InteractionHand.MAIN_HAND, emptyBottle);
                    }
                });
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    private static ItemStack useItem(ItemStack itemStack, Player player) {
        if (!player.isCreative()) {
            itemStack.shrink(1);
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        return itemStack;
    }
}
