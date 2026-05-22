package carpet_cuo.wheel;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import java.util.Optional;

public class BlockHardnessModifiers {
    public static float getHardness(Block block, float defaultValue) {
        //末地传送门框架
        if (Carpet_CuOSettings.endPortalFrameCanBeMined && block == Blocks.END_PORTAL_FRAME) return Blocks.OBSIDIAN.defaultDestroyTime();
        //基岩
        if (Carpet_CuOSettings.bedrockCanBeMined && block == Blocks.BEDROCK) return Blocks.OBSIDIAN.defaultDestroyTime();
        return defaultValue;
    }
}