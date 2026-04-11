package carpet_cuo.mixins;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBlockMixin {
    @Inject(
            method = "isPushable",
            at = @At(
                    value = "INVOKE",
                    //#if MC < 260100
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
                    //#else
                    //$$ target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z",
                    //#endif
                    ordinal = 0
            ),
            cancellable = true
    )
    private static void injectCheck(BlockState state, Level world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(Blocks.END_PORTAL_FRAME) || state.is(Blocks.BEDROCK)) cir.setReturnValue(false);
    }

    @ModifyConstant(
            method = "moveBlocks",
            constant = @Constant(intValue = 18)
    )
    private static int setFlags(int constant){
        if (Carpet_CuOSettings.pistonBreakingBlockProducesUpdate) return 3;
        return constant;
    }
}

