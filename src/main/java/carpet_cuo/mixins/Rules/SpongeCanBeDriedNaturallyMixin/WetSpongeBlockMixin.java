package carpet_cuo.mixins.Rules.SpongeCanBeDriedNaturallyMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
//#if MC >= 12111
//$$ import net.minecraft.world.attribute.EnvironmentAttributes;
//#endif
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.NeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WetSpongeBlock.class)
public abstract class WetSpongeBlockMixin extends Block {
    public WetSpongeBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "onPlace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        Biome biome = level.getBiome(blockPos).value();
        if (biome.getBaseTemperature() == 2.0F ||
                //#if MC < 12111
                level.dimensionType().ultraWarm()
                //#else
                //$$ (Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, blockPos)
                //#endif
        ) {
            this.drySponge(level, blockPos);
            ci.cancel();
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    //#if MC >= 12006
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
    //#else
    //$$ @SuppressWarnings("deprecation")
    //$$ public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
    //#endif
        Biome biome = serverLevel.getBiome(blockPos).value();
        this.tick(serverLevel, blockPos, biome);
    }

    @Unique
    private void tick(Level level, BlockPos blockPos, Biome biome) {
        if (Carpet_CuOSettings.spongeCanBeDriedNaturally
                && !level.isRainingAt(blockPos.above())
                && !this.hasWater(level, blockPos)
                && biome.getBaseTemperature() >= 0.15F
            ) {
            drySponge(level, blockPos);
        }
    }

    @Unique
    private void drySponge(Level level, BlockPos blockPos) {
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

    @Unique
    private boolean hasWater(Level level, BlockPos sourcePos) {
        for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
            BlockPos blockPos = sourcePos.relative(direction);
            if (level.getFluidState(blockPos).is(FluidTags.WATER)) return true;
        }
        return false;
    }
}
