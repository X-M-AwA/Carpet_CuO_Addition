package carpet_cuo.mixins.rules.OreFeatureCanSendBlockUpdate;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {
    @Unique
    private static final Direction[] UPDATE_SHAPE_ORDER;

    @Inject(
            method = "doPlace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;",
                    shift = At.Shift.AFTER
            )
    )
    private void doPlace(WorldGenLevel worldGenLevel,
                         RandomSource randomSource,
                         OreConfiguration oreConfiguration,
                         double d, double e, double f, double g, double h, double i, int j, int k, int l, int m, int n,
                         CallbackInfoReturnable<Boolean> cir,
                         @Local(ordinal = 0) BlockPos.MutableBlockPos mutableBlockPos,
                         @Local OreConfiguration.TargetBlockState targetBlockState
                         ) {
        if (Carpet_CuOSettings.oreFeatureCanSendBlockUpdate) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            for (Direction direction : UPDATE_SHAPE_ORDER) {
                pos.setWithOffset(mutableBlockPos, direction);
                if (worldGenLevel.getBlockState(pos).is(BlockTags.RAILS)) return;
                //#if MC >= 12103
                worldGenLevel.neighborShapeChanged(direction.getOpposite(), pos, mutableBlockPos, targetBlockState.state, 2, 512);
                //#else
                //$$ worldGenLevel.neighborShapeChanged(direction.getOpposite(), targetBlockState.state, pos, mutableBlockPos, 2, 512);
                //#endif
            }
        }
    }

    static {
        UPDATE_SHAPE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};
    }
}
