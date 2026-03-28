package carpet_cuo.mixins.instantDispenserAndDropperMixin;

import carpet_cuo.Carpet_CuOSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
//#if MC >= 12102
import net.minecraft.world.level.redstone.Orientation;
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
    protected abstract void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random);

    @Final
    @Shadow
    public static BooleanProperty TRIGGERED;

    @Inject(
            method = "neighborChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"
            ),
            cancellable = true
    )
    private void neighborUpdate(BlockState state, Level world, BlockPos pos, Block block,
                                //#if MC < 12102
                                //$$BlockPos sourcePos,
                                //#else
                                Orientation orientation,
                                //#endif
                                boolean bl, CallbackInfo ci
    ){
        if (Carpet_CuOSettings.instantDispenserAndDropper && !world.isClientSide()){
            tick(state, (ServerLevel) world, pos, RandomSource.create());
            world.setBlock(pos, state.setValue(TRIGGERED, true), 2);
            ci.cancel();
        }
    }
}