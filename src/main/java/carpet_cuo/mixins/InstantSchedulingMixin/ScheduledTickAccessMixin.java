package carpet_cuo.mixins.InstantSchedulingMixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
//#if MC >= 12103
import net.minecraft.world.level.ScheduledTickAccess;
//#else
//$$ import net.minecraft.world.level.LevelAccessor;
//#endif
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.ticks.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import carpet_cuo.Carpet_CuOSettings;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

//#if MC >= 12103
@Mixin(ScheduledTickAccess.class)
//#else
//$$ @Mixin(LevelAccessor.class)
//#endif
public interface ScheduledTickAccessMixin {

    //方块计划刻
    @Unique
    private void scheduledBlockTick(BlockPos blockPos, CallbackInfo ci){
        if (Carpet_CuOSettings.instantScheduling && (this instanceof ServerLevel level)) {
            if (!level.isClientSide()) {
                BlockBehaviour.BlockStateBase stateBase = level.getBlockState(blockPos);
                if (stateBase.is(Blocks.FIRE)) return;
                stateBase.tick(level, blockPos, RandomSource.create());
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;ILnet/minecraft/world/ticks/TickPriority;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void scheduleBlockTickWithPriority(BlockPos blockPos, Block block, int delay, TickPriority priority, CallbackInfo ci){
        this.scheduledBlockTick(blockPos, ci);
    }

    @Inject(
            method = "scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void scheduleBlockTick(BlockPos blockPos, Block block, int delay, CallbackInfo ci){
        this.scheduledBlockTick(blockPos,  ci);
    }

    @ModifyArgs(
            method = "scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V",
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12103
                    target = "Lnet/minecraft/world/level/ScheduledTickAccess;createTick(Lnet/minecraft/core/BlockPos;Ljava/lang/Object;I)Lnet/minecraft/world/ticks/ScheduledTick;"
                    //#else
                    //$$ target = "Lnet/minecraft/world/level/LevelAccessor;createTick(Lnet/minecraft/core/BlockPos;Ljava/lang/Object;I)Lnet/minecraft/world/ticks/ScheduledTick;"
                    //#endif
            )
    )
    private void setDelay(Args args){
        if (Carpet_CuOSettings.instantScheduling && this instanceof ServerLevel level) {
            if (!level.isClientSide()) args.set(2, 1);
        }
    }

    //流体计划刻
    @Unique
    private void scheduledFluidTick(BlockPos blockPos, CallbackInfo ci){
        if (Carpet_CuOSettings.instantScheduling && this instanceof ServerLevel level) {
            if (!level.isClientSide()){
                //#if MC >= 12103
                BlockState blockState = level.getBlockState(blockPos);
                //#endif
                FluidState fluidState = level.getFluidState(blockPos);
                //#if MC >= 12103
                fluidState.tick(level, blockPos, blockState);
                //#else
                //$$ fluidState.tick(level, blockPos);
                //#endif
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/Fluid;ILnet/minecraft/world/ticks/TickPriority;)V",
            at = @At("HEAD")
    )
    private void scheduleFluidTickWithPriority(BlockPos blockPos, Fluid fluid, int delay, TickPriority priority, CallbackInfo ci){
        this.scheduledFluidTick(blockPos, ci);
    }

    @Inject(
            method = "scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/Fluid;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void scheduleFluidTick(BlockPos blockPos, Fluid fluid, int delay, CallbackInfo ci){
        this.scheduledFluidTick(blockPos, ci);
    }
}
