package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
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
                Component displayName = Component.literal(String.format("%s [%s]", entity.getDisplayName().getString(), UUID));
                stack.set(DataComponents.CUSTOM_NAME, displayName);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }
}
