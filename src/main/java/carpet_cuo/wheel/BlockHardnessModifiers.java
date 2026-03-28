package carpet_cuo.wheel;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import java.util.Optional;

public class BlockHardnessModifiers {
    public static Optional<Float> getHardness(Block block, BlockGetter world, BlockPos pos) {
        //末地传送门框架
        if (Carpet_CuOSettings.endPortalFrameCanBeMined && block == Blocks.END_PORTAL_FRAME) return Optional.of(Blocks.OBSIDIAN.defaultDestroyTime());
        //基岩
        if (Carpet_CuOSettings.bedrockCanBeMined && block == Blocks.BEDROCK) return Optional.of(Blocks.OBSIDIAN.defaultDestroyTime());
        return Optional.empty();
    }
}