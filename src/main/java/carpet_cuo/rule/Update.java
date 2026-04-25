package carpet_cuo.rule;

import carpet_cuo.Carpet_CuOSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;

public class Update {
    public static void init(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!Carpet_CuOSettings.rightClickBlockUpdate.equals("false") && !player.isShiftKeyDown()) {
                BlockState blockState = world.getBlockState(hitResult.getBlockPos());
                BlockState state = state(blockState);
                Thread thread = new Thread(() -> {
                    if (state != null) world.setBlock(hitResult.getBlockPos(), state, 2);
                });

                if (Carpet_CuOSettings.rightClickBlockUpdate.equals("def")) {
                    world.updateNeighborsAt(hitResult.getBlockPos(), blockState.getBlock());
                    return InteractionResult.SUCCESS;
                }else if (Carpet_CuOSettings.rightClickBlockUpdate.equals("async") && blockState.is(Blocks.COPPER_ORE) || blockState.is(Blocks.DEEPSLATE_COPPER_ORE)) {
                    thread.start();
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    private static BlockState state(BlockState blockState){
        if (blockState.is(Blocks.COPPER_ORE)) return Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState();
        else if (blockState.is(Blocks.DEEPSLATE_COPPER_ORE)) return Blocks.COPPER_ORE.defaultBlockState();
        return null;
    }
}
