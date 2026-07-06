package carpet_cuo.mixins.rules.RepairTheMonsterAIMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GroundPathNavigation.class)
public abstract class GroundPathNavigationMixin extends PathNavigation {
    public GroundPathNavigationMixin(Mob mob, Level level) {
        super(mob, level);
    }

    @Inject(
            //#if MC <= 12108
            method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;",
            //#else
            //$$ method = "findSurfacePosition",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12103
                    target = "Lnet/minecraft/core/BlockPos;mutable()Lnet/minecraft/core/BlockPos$MutableBlockPos;",
                    ordinal = 1
                    //#else
                    //$$ target = "Lnet/minecraft/core/BlockPos;above()Lnet/minecraft/core/BlockPos;",
                    //$$ ordinal = 2
                    //#endif
            ),
            cancellable = true
    )
    //#if MC <= 12108
    private void createPath(BlockPos blockPos, int i, CallbackInfoReturnable<Path> cir) {
        if (Carpet_CuOSettings.repairTheMonsterAI) cir.setReturnValue(super.createPath(blockPos, i));
    }
    //#else
    //$$ private void createPath(LevelChunk levelChunk, BlockPos blockPos, int i, CallbackInfoReturnable<BlockPos> cir) {
    //$$     if (Carpet_CuOSettings.repairTheMonsterAI) cir.setReturnValue(blockPos);
    //$$ }
    //#endif
}
