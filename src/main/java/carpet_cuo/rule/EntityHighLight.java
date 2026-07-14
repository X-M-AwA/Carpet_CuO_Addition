package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
//#if MC >= 260102
//$$ import net.minecraft.core.component.DataComponents;
//#endif
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EntityHighLight {
    private static final Map<DyeColor, Integer> COLOR_MAP = new HashMap<>();

    public static void init() {
        UseEntityCallback.EVENT.register((player, world, interactionHand, entity, entityHitResult) -> {
            if (!Carpet_CuOSettings.entityHighLight || player.isShiftKeyDown() || entity instanceof Player) return InteractionResult.PASS;

            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof DyeItem dyeItem) {
                //#if MC < 260102
                int color = COLOR_MAP.get(dyeItem.getDyeColor());
                //#else
                //$$ int color = COLOR_MAP.get(stack.get(DataComponents.DYE));
                //#endif
                if (entity.hasGlowingTag() && entity.getTeamColor() == color) {
                    ((IEntityColor) entity).setHighlightColor(0xFFFFFF);
                    entity.setGlowingTag(false);
                    return InteractionResult.SUCCESS;
                }
                ((IEntityColor) entity).setHighlightColor(color);
                entity.setGlowingTag(true);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    static {
        COLOR_MAP.put(DyeColor.WHITE,0xF9FFFE);
        COLOR_MAP.put(DyeColor.ORANGE,0xF9801D);
        COLOR_MAP.put(DyeColor.MAGENTA,0xC74EBD);
        COLOR_MAP.put(DyeColor.LIGHT_BLUE,0x3AB3DA);
        COLOR_MAP.put(DyeColor.YELLOW,0xFED83D);
        COLOR_MAP.put(DyeColor.LIME,0x80C71F);
        COLOR_MAP.put(DyeColor.PINK,0xF38BAA);
        COLOR_MAP.put(DyeColor.GRAY,0x474F52);
        COLOR_MAP.put(DyeColor.LIGHT_GRAY,0x9D9D97);
        COLOR_MAP.put(DyeColor.CYAN,0x169C9C);
        COLOR_MAP.put(DyeColor.PURPLE,0x8932B8);
        COLOR_MAP.put(DyeColor.BLUE,0x3C44AA);
        COLOR_MAP.put(DyeColor.BROWN,0x835432);
        COLOR_MAP.put(DyeColor.GREEN,0x5E7C16);
        COLOR_MAP.put(DyeColor.RED,0xB02E26);
        COLOR_MAP.put(DyeColor.BLACK,0x1D1D21);
    }
}
