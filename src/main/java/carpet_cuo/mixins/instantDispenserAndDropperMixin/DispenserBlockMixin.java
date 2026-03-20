package carpet_cuo.mixins.instantDispenserAndDropperMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
//#if MC >= 12102
import net.minecraft.world.block.WireOrientation;
//#endif
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {
    @Shadow
    protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @Final
    @Shadow
    public static BooleanProperty TRIGGERED;

    @Inject(
            method = "neighborUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"
            ),
            cancellable = true
    )
    private void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
                                //#if MC < 12102
                                //$$BlockPos sourcePos,
                                //#else
                                WireOrientation wireOrientation,
                                //#endif
                                boolean notify, CallbackInfo ci){
        if (Carpet_CuOSettings.instantDispenserAndDropper && !world.isClient()){
            scheduledTick(state, (ServerWorld) world, pos, Random.create());
            world.setBlockState(pos, state.with(TRIGGERED, true), 2);
            ci.cancel();
        }
    }
}
