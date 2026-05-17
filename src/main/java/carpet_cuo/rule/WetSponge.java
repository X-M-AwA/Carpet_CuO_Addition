package carpet_cuo.rule;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
//#if MC >= 12111
//$$ import net.minecraft.world.attribute.EnvironmentAttributes;
//#endif

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WetSponge {
    private static final Map<ServerLevel, Map<BlockPos, Integer>> DRYING_MAP = new ConcurrentHashMap<>();
    private static final int MinDelay = 900;
    private static final int MaxDelay = 1800;

    public static void tick(ServerLevel level) {
        Map<BlockPos, Integer> map = DRYING_MAP.get(level);
        if (map == null || map.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<BlockPos, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = it.next();
            BlockPos pos = entry.getKey();
            int delay = entry.getValue();

            if (!level.isLoaded(pos)) {
                it.remove();
                continue;
            }

            BlockState blockState = level.getBlockState(pos);

            if (!(blockState.getBlock() instanceof WetSpongeBlock)) {
                it.remove();
                continue;
            }
            if (!level.isRainingAt(pos.above()) && !hasWater(level, pos)) delay--;

            if (delay <= 0) {
                if (!level.isRainingAt(pos.above())) drySponge(level, pos);
                it.remove();
            } else {
                entry.setValue(delay);
            }
        }
    }

    private static void drySponge(Level level, BlockPos blockPos) {
        level.setBlock(blockPos, Blocks.SPONGE.defaultBlockState(), 3);
        level.levelEvent(2009, blockPos, 0);
        level.playSound(null, blockPos,
                //#if MC > 12104
                SoundEvents.WET_SPONGE_DRIES
                //#else
                //$$ SoundEvents.FIRE_EXTINGUISH
                //#endif
                , SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
    }

    public static void Evaporate(Level level, BlockPos blockPos) {
        Biome biome = level.getBiome(blockPos).value();
        float T = biome.getBaseTemperature();
        int light = level.getBrightness(LightLayer.BLOCK, blockPos);

        if (biome.coldEnoughToSnow(blockPos
                //#if MC >= 12103
                , level.getSeaLevel()
                //#endif
        )) return;
        else if (T >= 2.0F ||
                //#if MC < 12111
                level.dimensionType().ultraWarm()
                //#else
                //$$ (Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, blockPos)
                //#endif
        ) {
            drySponge(level, blockPos);
            return;
        }

        if (level instanceof ServerLevel) DRYING_MAP.computeIfAbsent((ServerLevel) level, k -> new ConcurrentHashMap<>()).put(blockPos, calculateDelay((ServerLevel) level, T, light));
    }

    private static int calculateDelay(ServerLevel level, float temperature, int light) {
        float tempFactor = 1.0F - (temperature - 0.15F) / (2.0F - 0.15F) * 0.6F;
        tempFactor = Math.max(0.3F, Math.min(1.0F, tempFactor));
        int lightReduction = light * 30;

        int delay = (int) ((MinDelay + level.random.nextInt((MaxDelay - MinDelay + 1))) * tempFactor) - lightReduction;

        return Math.max(200, delay);
    }

    private static boolean hasWater(Level level, BlockPos blockPos) {
        BlockState[] water = new BlockState[]{
                level.getBlockState(blockPos.west()),
                level.getBlockState(blockPos.east()),
                level.getBlockState(blockPos.north()),
                level.getBlockState(blockPos.south()),
                level.getBlockState(blockPos.below()),
                level.getBlockState(blockPos.above()),
        };
        for (BlockState blockState : water) {
            if (blockState.is(Blocks.WATER) || blockState.is(Blocks.WATER_CAULDRON)) return true;
        }
        return false;
    }
}
