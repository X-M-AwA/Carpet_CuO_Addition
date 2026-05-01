package carpet_cuo.mixins.InstantFireBlockMixin;

import carpet_cuo.Carpet_CuOSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends BaseFireBlock {
    public FireBlockMixin(Properties properties, float f) {
        super(properties, f);
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"
            )
    )
    private void Tick(ServerLevel instance, BlockPos blockPos, Block block, int i, Operation<Void> original){
        if (!Carpet_CuOSettings.instantFireBlock) original.call(instance, blockPos, block, i);
    }

    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci){
        if (Carpet_CuOSettings.instantFireBlock && serverLevel.getBlockState(blockPos).is(Blocks.FIRE)) {
            serverLevel.scheduleTick(blockPos, this, 30 + randomSource.nextInt(10));
        }
    }
}
