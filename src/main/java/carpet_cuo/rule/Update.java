package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;

public class Update {
    public static void init(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.isShiftKeyDown()) return InteractionResult.PASS;

            if (Carpet_CuOSettings.rightClickBlockUpdate) {
                BlockState state = world.getBlockState(hitResult.getBlockPos());
                world.updateNeighborsAt(hitResult.getBlockPos(), state.getBlock());
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }
}
