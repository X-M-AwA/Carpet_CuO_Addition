package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOMod;
import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class EntityHighLight {
    public static void init(){
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!Carpet_CuOSettings.entityHighLight || player.isShiftKeyDown()) return InteractionResult.PASS;

            ItemStack stack = player.getMainHandItem();
            Scoreboard scoreboard = world.getScoreboard();

            return setColor(stack, scoreboard, entity, world);
        });
    }

    private static InteractionResult setColor(ItemStack stack, Scoreboard scoreboard, Entity entity, Level level){
        if (stack.getItem() instanceof DyeItem dyeItem){
            String ID = entity.getStringUUID();
            DyeColor color = dyeItem.getDyeColor();
            ChatFormatting formatting = COLOR_MAP.getOrDefault(color, ChatFormatting.WHITE);
            String Name = Carpet_CuOMod.MOD_ID+ ":" + formatting.name();
            PlayerTeam playerTeam = scoreboard.getPlayerTeam(Name);
            PlayerTeam oldTeam = entity.getTeam();

            if (playerTeam != null && playerTeam.getName().equals(Name)){
                if (playerTeam.getPlayers().contains(ID)){
                    if (!level.isClientSide()) scoreboard.removePlayerFromTeam(ID, playerTeam);
                    entity.setGlowingTag(false);
                    if (playerTeam.getPlayers().isEmpty()) scoreboard.removePlayerTeam(playerTeam);
                    return InteractionResult.SUCCESS;
                }
            }

            if (playerTeam == null){
                playerTeam = scoreboard.addPlayerTeam(Name);
                playerTeam.setColor(formatting);
            }

            if (!level.isClientSide()){
                scoreboard.addPlayerToTeam(ID, playerTeam);
                if (oldTeam != null && oldTeam.getPlayers().isEmpty()) scoreboard.removePlayerTeam(oldTeam);
            }
            entity.setGlowingTag(true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static final Map<DyeColor, ChatFormatting> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(DyeColor.WHITE, ChatFormatting.WHITE);
        COLOR_MAP.put(DyeColor.ORANGE, ChatFormatting.GOLD);
        COLOR_MAP.put(DyeColor.MAGENTA, ChatFormatting.LIGHT_PURPLE);
        COLOR_MAP.put(DyeColor.LIGHT_BLUE, ChatFormatting.AQUA);
        COLOR_MAP.put(DyeColor.YELLOW, ChatFormatting.YELLOW);
        COLOR_MAP.put(DyeColor.LIME, ChatFormatting.GREEN);
        COLOR_MAP.put(DyeColor.GRAY, ChatFormatting.DARK_GRAY);
        COLOR_MAP.put(DyeColor.LIGHT_GRAY, ChatFormatting.GRAY);
        COLOR_MAP.put(DyeColor.CYAN, ChatFormatting.DARK_AQUA);
        COLOR_MAP.put(DyeColor.PURPLE, ChatFormatting.DARK_PURPLE);
        COLOR_MAP.put(DyeColor.BLUE, ChatFormatting.BLUE);
        COLOR_MAP.put(DyeColor.GREEN, ChatFormatting.DARK_GREEN);
        COLOR_MAP.put(DyeColor.RED, ChatFormatting.RED);
        COLOR_MAP.put(DyeColor.BLACK, ChatFormatting.BLACK);
    }
}
