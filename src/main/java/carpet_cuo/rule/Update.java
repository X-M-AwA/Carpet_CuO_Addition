package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
public class Update {
    public static void init(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.isSneaking()) return ActionResult.PASS;

            if (Carpet_CuOSettings.rightClickBlockUpdate && !world.isClient()) {
                BlockState state = world.getBlockState(hitResult.getBlockPos());
                world.updateNeighbors(hitResult.getBlockPos(), state.getBlock());

                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
