package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import carpet_cuo.utils.Messenger;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
//#if MC > 12004
import net.minecraft.core.component.DataComponents;
//#else
//$$ import net.minecraft.nbt.CompoundTag;
//#endif
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CustomizeTheArrowOwner {
    public static void init() {
        UseEntityCallback.EVENT.register((player, level, interactionHand, entity, entityHitResult) -> {
            if (!Carpet_CuOSettings.customizeTheArrowOwner || !player.isShiftKeyDown() || entity instanceof Player) return InteractionResult.PASS;

            ItemStack stack = player.getMainHandItem();
            if (stack.is(Items.BOW)) {
                String UUID = entity.getStringUUID();
                //#if MC > 12004
                stack.set(DataComponents.CUSTOM_NAME, Messenger.s(String.format("%s [%s]", entity.getDisplayName().getString(), UUID)));
                //#else
                //$$ stack.setHoverName(Messenger.s(String.format("%s [%s]", entity.getDisplayName().getString(), UUID)));
                //#endif
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }
}
